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

public class ProdutoController {

    public final static String SAIDA = "S";
    public final static String ENTRADA = "E";

    private final Dao dao = new Dao();

    public void geraMovtoEstoque(MovtoEstoque movto) throws Exception {

        Produto produto = new Produto();
        produto.setCdProduto(movto.getCdProduto());
        dao.get(produto);

        if (movto.getInCancelado() && movto.getCdEstqMovto() != null) {
            cancelaMovto(movto, produto);
        } else if (!movto.getInCancelado() && movto.getCdEstqMovto() == null) {
            geraMovto(movto, produto);
        } else {
            throw new Exception("Tipo de movimento de estoque não esperado.");
        }

    }

    private void cancelaMovto(MovtoEstoque movto, Produto produto) throws Exception {
        dao.get(movto);
        movto.setInCancelado(true);
        movto.setCdUserCancel(FrmMenuFXML.usuarioAtivo);
        movto.setDtCancelado(Data.getAgora());

        if (movto.getTpMovto().equals("E")) {
            produto.setQtEstqAtual(produto.getQtEstqAtual() - movto.getQtMovto());
        } else {
            if (movto.getVlItem() != null) { //Calcula o custo médio
                double vlTotal = ((produto.getQtEstqAtual() * produto.getQtEstqAtual())
                        - (movto.getQtMovto() * movto.getVlItem()));
                produto.setVlCustoMedio(vlTotal / (produto.getQtEstqAtual() - movto.getQtMovto()));
            }
            produto.setQtEstqAtual(produto.getQtEstqAtual() + movto.getQtMovto());
        }

        dao.update(produto);
        dao.update(movto);
    }

    private void geraMovto(MovtoEstoque movto, Produto produto) throws Exception {

        movto.setQtEstoque(produto.getQtEstqAtual());
        movto.setVlCustoMedio(produto.getVlCustoMedio());
        movto.setInCancelado(false);

        if (movto.getTpMovto().equals("E")) {
            if (movto.getVlItem() != null) { //Calcula o custo médio
                double vlTotal = ((produto.getQtEstqAtual() * produto.getQtEstqAtual())
                        + (movto.getQtMovto() * movto.getVlItem()));
                produto.setVlCustoMedio(vlTotal / (produto.getQtEstqAtual() + movto.getQtMovto()));
            }
            produto.setQtEstqAtual(produto.getQtEstqAtual() + movto.getQtMovto());
        } else {
            produto.setQtEstqAtual(produto.getQtEstqAtual() - movto.getQtMovto());
        }

        dao.update(produto);
        dao.save(movto);
    }

    public Double getUltimoPreco(Integer cdProduto) throws Exception {
        String where = "WHERE $cdProduto$ = " + cdProduto + " ORDER BY $dtPreco$ DESC";
        ArrayList<Object> precos = dao.getAllWhere(new PrecoProduto(), where);
        if (precos.isEmpty()){
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
    public static ArrayList<TipoMovtoEstoque> getAllTipoMovtoEstoque() {
        ArrayList<TipoMovtoEstoque> tipos = new ArrayList<>();
        tipos.add(new TipoMovtoEstoque(null, ""));
        tipos.add(new TipoMovtoEstoque(1, "Compra"));
        tipos.add(new TipoMovtoEstoque(2, "Venda"));
        tipos.add(new TipoMovtoEstoque(3, "Ajuste"));
        return tipos;
    }

    public static TipoMovtoEstoque getTipoMovtoEstoque(int id) {
        return getAllTipoMovtoEstoque().get(id);
    }

    public static class TipoMovtoEstoque {

        private Integer cdTpMovto;
        private String dsTpMovto;

        public TipoMovtoEstoque(Integer cdTpMovto, String dsTpMovto) {
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
            return "Entrada";
        } else if (tpMovto.equals("S")) {
            return "Saída";
        }
        return null;
    }
}
