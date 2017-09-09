/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.util.ArrayList;

public class EstoqueController {

    public final static String SAIDA = "S";
    public final static String ENTRADA = "E";

    private final Dao dao = new Dao();

    public void geraMovtoEstoque(MovtoEstoque movto) throws Exception {

        if (movto.getInCancelado() && movto.getCdEstqMovto() != null) {
            cancelaMovto(movto);
        } else if (!movto.getInCancelado() && movto.getCdEstqMovto() == null) {
            geraMovto(movto);
        } else {
            throw new Exception("Tipo de movimento de estoque não esperado.");
        }

    }

    private void cancelaMovto(MovtoEstoque movto) throws Exception {
        dao.get(movto);
        movto.setInCancelado(true);
        movto.setCdUserCancel(FrmMenuFXML.usuarioAtivo);
        movto.setDtCancelado(Data.getAgora());

        Produto produto = new Produto();
        produto.setCdProduto(movto.getCdProduto());
        dao.get(produto);

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

    private void geraMovto(MovtoEstoque movto) throws Exception {
        Produto produto = new Produto();
        produto.setCdProduto(movto.getCdProduto());
        dao.get(produto);

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

    public static ArrayList<TipoAjusteEstoque> getAllTipoAjuste() {
        ArrayList<TipoAjusteEstoque> tipos = new ArrayList<>();
        tipos.add(new TipoAjusteEstoque(1, "Consumo Interno", SAIDA));
        tipos.add(new TipoAjusteEstoque(2, "Correção de Quantidade (Entrada)", ENTRADA));
        tipos.add(new TipoAjusteEstoque(3, "Correção de Quantidade (Saída)", SAIDA));
        tipos.add(new TipoAjusteEstoque(4, "Desperdício", SAIDA));
        tipos.add(new TipoAjusteEstoque(5, "Validade Vencida", SAIDA));
        tipos.add(new TipoAjusteEstoque(6, "Quebra/CAT", SAIDA));
        tipos.add(new TipoAjusteEstoque(7, "Roubo", SAIDA));
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
}
