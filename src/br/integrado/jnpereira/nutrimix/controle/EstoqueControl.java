/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.modelo.PrecoProduto;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.util.ArrayList;

public class EstoqueControl {

    public final static String SAIDA = "S";
    public final static String ENTRADA = "E";

    private final Dao dao = new Dao();

    public void geraMovtoEstoque(MovtoEstoque movto) throws Exception {
        dao.autoCommit(false);

        Produto produto = new Produto();
        produto.setCdProduto(movto.getCdProduto());
        dao.get(produto);

        if (!produto.getInEstoque()) { //apenas produtos com controle de estoque
            return;
        }

        if (movto.getInCancelado() && movto.getCdEstqMovto() != null) {
            cancelaMovto(movto, produto);
        } else if (!movto.getInCancelado() && movto.getCdEstqMovto() == null) {
            geraMovto(movto, produto);
        } else {
            throw new Exception("Tipo de movimento de estoque não esperado.");
        }

    }

    private void geraMovto(MovtoEstoque movto, Produto produto) throws Exception {

        movto.setQtEstoque(produto.getQtEstqAtual());
        movto.setVlCustoMedio(produto.getVlCustoMedio());
        movto.setInCancelado(false);

        if (movto.getTpMovto().equals(ENTRADA)) {
            if (movto.getVlItem() == null) {
                movto.setVlItem(0.00);
            }
            double vlTotal = ((produto.getQtEstqAtual() * produto.getVlCustoMedio())
                    + (movto.getQtMovto() * movto.getVlItem()));
            produto.setVlCustoMedio(divisao(vlTotal, (produto.getQtEstqAtual() + movto.getQtMovto())));
            produto.setQtEstqAtual(produto.getQtEstqAtual() + movto.getQtMovto());
        } else {
            produto.setQtEstqAtual(produto.getQtEstqAtual() - movto.getQtMovto());
        }

        dao.update(produto);
        dao.save(movto);
    }

    private void cancelaMovto(MovtoEstoque movto, Produto produto) throws Exception {
        dao.get(movto);
        movto.setInCancelado(true);
        movto.setCdUserCancel(MenuControl.usuarioAtivo);
        movto.setDtCancelado(Data.getAgora());

        if (movto.getTpMovto().equals(ENTRADA)) {
            if (movto.getVlItem() != null) { //Calcula o custo médio
                double vlTotal = ((produto.getQtEstqAtual() * produto.getVlCustoMedio())
                        - (movto.getQtMovto() * movto.getVlItem()));
                produto.setVlCustoMedio(divisao(vlTotal, (produto.getQtEstqAtual() - movto.getQtMovto())));
            }
            produto.setQtEstqAtual(produto.getQtEstqAtual() - movto.getQtMovto());
            dao.update(movto);
            //refazerMovtoEstoque(movto); //refaz o movimento de estoque                  
        } else {
            dao.update(movto);
            produto.setQtEstqAtual(produto.getQtEstqAtual() + movto.getQtMovto());
        }

        dao.update(produto);
    }

    private void refazerMovtoEstoque(MovtoEstoque movto) throws Exception {
        String where = "WHERE $cdProduto$ = " + movto.getCdProduto() + " AND $inCancelado$ = 'F' ORDER BY $cdEstqMovto$ ASC";
        ArrayList<Object> movtos = dao.getAllWhere(new MovtoEstoque(), where);
        double qtEstoqueInicial = 0.00;
        double vlCustoMedioInicial = 0.00;
        for (Object obj : movtos) {
            MovtoEstoque mov = (MovtoEstoque) obj;
            if (mov.getCdEstqMovto() < movto.getCdEstqMovto()) { //Ajuste anterior ao que irá ser estornado
                if (mov.getTpMovto().equals(ENTRADA)) {
                    qtEstoqueInicial = mov.getQtEstoque() + mov.getQtMovto();
                    if (movto.getVlItem() != null) {
                        vlCustoMedioInicial = divisao(qtEstoqueInicial, ((mov.getVlItem() * mov.getQtMovto()) + (mov.getQtEstoque() * mov.getVlCustoMedio())));
                    }
                } else {
                    qtEstoqueInicial = mov.getQtEstoque() - mov.getQtMovto();
                }
            }
            if (mov.getCdEstqMovto() > movto.getCdEstqMovto()) {
                mov.setQtEstoque(qtEstoqueInicial);
                mov.setVlCustoMedio(vlCustoMedioInicial);
                if (mov.getTpMovto().equals(ENTRADA)) {
                    qtEstoqueInicial = mov.getQtEstoque() + mov.getQtMovto();
                    if (movto.getVlItem() != null) {
                        vlCustoMedioInicial = divisao(qtEstoqueInicial, ((mov.getVlItem() * mov.getQtMovto()) + (mov.getQtEstoque() * mov.getVlCustoMedio())));
                    }
                } else {
                    qtEstoqueInicial = mov.getQtEstoque() - mov.getQtMovto();
                }
            }
            dao.update(mov);
        }
    }

    private Double divisao(Double v1, Double v2) {
        if (v1 == null | v2 == null) {
            return 0.00;
        }
        if (v1 == 0 | v2 == 0) {
            return 0.00;
        }
        return v1 / v2;
    }

    public Double getUltimoPreco(Integer cdProduto) throws Exception {
        String where = "WHERE $cdProduto$ = " + cdProduto + " ORDER BY $dtPreco$ DESC";
        ArrayList<Object> precos = dao.getAllWhere(new PrecoProduto(), where);
        if (precos.isEmpty()) {
            return null;
        }
        PrecoProduto preco = (PrecoProduto) precos.get(0);
        return preco.getVlPreco();
    }

    //Tipo de Ajuste
    public static ArrayList<TipoAjusteEstoque> getAllTipoAjuste() {
        ArrayList<TipoAjusteEstoque> tipos = new ArrayList<>();
        tipos.add(new TipoAjusteEstoque(1, "Consumo Interno", SAIDA));
        tipos.add(new TipoAjusteEstoque(2, "Ajuste (Entrada)", ENTRADA));
        tipos.add(new TipoAjusteEstoque(3, "Ajuste (Saída)", SAIDA));
        tipos.add(new TipoAjusteEstoque(4, "Desperdício", SAIDA));
        tipos.add(new TipoAjusteEstoque(5, "Validade Vencida", SAIDA));
        tipos.add(new TipoAjusteEstoque(6, "Quebra/CAT", SAIDA));
        tipos.add(new TipoAjusteEstoque(7, "Roubo", SAIDA));
        tipos.add(new TipoAjusteEstoque(8, "Bonificação", ENTRADA));
        tipos.add(new TipoAjusteEstoque(9, "Doação", SAIDA));
        return tipos;
    }

    public static TipoAjusteEstoque getTipoAjusteEstoque(int id) {
        return getAllTipoAjuste().get(id - 1);
    }

    public static class TipoAjusteEstoque {

        private Integer cdTpAjuste;
        private String dsTpAjuste;
        private String tpAjuste;

        public TipoAjusteEstoque(Integer cdTpAjuste, String dsTpAjuste, String tpAjuste) {
            this.cdTpAjuste = cdTpAjuste;
            this.dsTpAjuste = dsTpAjuste;
            this.tpAjuste = tpAjuste;
        }

        public Integer getCdTpAjuste() {
            return cdTpAjuste;
        }

        public void setCdTpAjuste(Integer cdTpAjuste) {
            this.cdTpAjuste = cdTpAjuste;
        }

        public String getDsTpAjuste() {
            return dsTpAjuste;
        }

        public void setDsTpAjuste(String dsTpAjuste) {
            this.dsTpAjuste = dsTpAjuste;
        }

        public String getTpAjuste() {
            return tpAjuste;
        }

        public void setTpAjuste(String tpAjuste) {
            this.tpAjuste = tpAjuste;
        }

        @Override
        public String toString() {
            return getDsTpAjuste();
        }
    }
    //Tipo de Ajuste

    //Tipo de Movimento de Estoque
    public static ArrayList<TipoMovto> getAllTipoMovtoEstoque() {
        ArrayList<TipoMovto> tipos = new ArrayList<>();
        tipos.add(new TipoMovto(null, ""));
        tipos.add(new TipoMovto(1, "Compra"));
        tipos.add(new TipoMovto(2, "Venda"));
        tipos.add(new TipoMovto(3, "Ajuste"));
        return tipos;
    }

    public static TipoMovto getTipoMovtoEstoque(int id) {
        return getAllTipoMovtoEstoque().get(id);
    }

    public static ArrayList<TipoMovto> getAllTipoMovtoCaixa() {
        ArrayList<TipoMovto> tipos = new ArrayList<>();
        tipos.add(new TipoMovto(null, ""));
        tipos.add(new TipoMovto(1, "Compra"));
        tipos.add(new TipoMovto(2, "Venda"));
        tipos.add(new TipoMovto(3, "Despesa"));
        tipos.add(new TipoMovto(4, "Ajuste"));
        return tipos;
    }

    public static TipoMovto getTipoMovtoCaixa(int id) {
        return getAllTipoMovtoCaixa().get(id);
    }

    public static class TipoMovto {

        private Integer cdTpMovto;
        private String dsTpMovto;

        public TipoMovto(Integer cdTpMovto, String dsTpMovto) {
            this.cdTpMovto = cdTpMovto;
            this.dsTpMovto = dsTpMovto;
        }

        @Override
        public String toString() {
            return getDsTpMovto();
        }

        public Integer getCdTpMovto() {
            return cdTpMovto;
        }

        public void setCdTpMovto(Integer cdTpMovto) {
            this.cdTpMovto = cdTpMovto;
        }

        public String getDsTpMovto() {
            return dsTpMovto;
        }

        public void setDsTpMovto(String dsTpMovto) {
            this.dsTpMovto = dsTpMovto;
        }

    }
    //Tip de Movimento de Estoque

    public static String getDsEntraSaida(String tpMovto) {
        if (tpMovto.equals("E")) {
            return "Entrada ▼";
        } else if (tpMovto.equals("S")) {
            return "Saída     ▲";
        }
        return null;
    }
}
