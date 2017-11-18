package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.AjusteCaixa;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Despesa;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.MovtoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.util.ArrayList;
import java.util.Date;

public class CaixaControl {

    Dao dao = new Dao();
    public final static String SAIDA = "S";
    public final static String ENTRADA = "E";

    public CaixaControl() {
        dao.autoCommit(false);
    }

    public FechamentoCaixa getCaixaAberto(Date pDtFechamento) throws Exception {
        String where = "WHERE $dtAbertura$ BETWEEN '" + Data.AmericaToBrasilSemHora(pDtFechamento) + " 00:00:00' AND '" + Data.AmericaToBrasilSemHora(pDtFechamento) + " 23:59:59'"
                + "AND $dtFechamento$ IS NULL";
        ArrayList<Object> fec = dao.getAllWhere(new FechamentoCaixa(), where);
        if (fec.isEmpty()) {
            throw new Exception("Caixa para o dia " + Data.AmericaToBrasilSemHora(pDtFechamento) + " está fechado ou não foi aberto.");
        }
        return (FechamentoCaixa) fec.get(0);
    }

    public void geraMovtoCaixaParcela(Parcela parcela, FechamentoCaixa fechamento) throws Exception {
        MovtoCaixa caixa = new MovtoCaixa();
        caixa.setTpMovtoCaixa(parcela.getTpMovto());
        caixa.setDtMovto(Data.getAgora());
        caixa.setCdFormaPagto(parcela.getCdForPagto());
        caixa.setCdFechamento(fechamento.getCdFechamento());
        caixa.setCdConta(parcela.getCdConta());
        caixa.setCdParcela(parcela.getCdParcela());
        caixa.setVlMovto(parcela.getVlPagto());
        dao.save(caixa);
    }

    public void geraMovtoAjuste(AjusteCaixa ajuste, FechamentoCaixa fechamento) throws Exception {
        int vCdTpAjuste = Integer.parseInt(ajuste.getTpAjuste());
        MovtoCaixa caixa = new MovtoCaixa();
        caixa.setTpMovtoCaixa(getAllTipoAjuste().get(vCdTpAjuste - 1).getTpAjuste());
        caixa.setDtMovto(Data.getAgora());
        caixa.setCdFormaPagto(ajuste.getCdForPagto());
        caixa.setCdFechamento(fechamento.getCdFechamento());
        caixa.setCdAjuste(ajuste.getCdAjuste());
        caixa.setVlMovto(ajuste.getVlAjuste());
        dao.save(caixa);
    }

    public void excluirDespesa(Despesa despesa) throws Exception {
        ContasPagarReceber conta;
        try {
            conta = (ContasPagarReceber) dao.getAllWhere(new ContasPagarReceber(), "WHERE $cdDespesa$ = " + despesa.getCdDespesa()).get(0);
            excluirConta(conta);
            dao.delete(despesa);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private void excluirConta(ContasPagarReceber conta) throws Exception {
        Tela tela = new Tela();
        if (!tela.validaAcessoOperacao(Tela.EXCLUSAO_DESPESAS)) {
            throw new Exception("Usuário sem acesso a está operação.");
        }

        ArrayList<Object> parcelas = dao.getAllWhere(new Parcela(), "WHERE $cdConta$ = " + conta.getCdConta());
        boolean vInAtiva = false;
        for (Object obj : parcelas) {
            Parcela parcela = (Parcela) obj;
            if (parcela.getDtPagto() != null && parcela.getCdParEstorno() == null) {
                String where = "WHERE $cdConta$ = " + conta.getCdConta() + " AND $cdParEstorno$ = " + parcela.getCdParcela();
                long vCdParEstorno = dao.getCountWhere(new Parcela(), where);
                if (vCdParEstorno == 0) {
                    vInAtiva = true;
                    break;
                }
                where = "WHERE $cdConta$ = " + conta.getCdConta() + " AND $cdParcela$ = " + parcela.getCdParcela();
                MovtoCaixa movtoCaixa = (MovtoCaixa) dao.getAllWhere(new MovtoCaixa(), where).get(0);
                FechamentoCaixa fechamento = new FechamentoCaixa();
                fechamento.setCdFechamento(movtoCaixa.getCdFechamento());
                dao.get(fechamento);
                if (fechamento.getDtFechamento() != null) {
                    vInAtiva = true;
                    return;
                }
            }
        }

        if (vInAtiva) {
            throw new Exception("Este movimento já tem parcela paga para um caixa já fechado.");
        }

        for (Object obj : parcelas) {
            Parcela parcela = (Parcela) obj;
            if (parcela.getDtPagto() != null) {
                String where = "WHERE $cdConta$ = " + conta.getCdConta() + " AND $cdParcela$ = " + parcela.getCdParcela();
                MovtoCaixa movtoCaixa = (MovtoCaixa) dao.getAllWhere(new MovtoCaixa(), where).get(0);
                dao.delete(movtoCaixa);
            }
            dao.delete(parcela);
        }
        dao.delete(conta);
    }

    //Tipo de Ajuste
    public static ArrayList<TipoAjusteCaixa> getAllTipoAjuste() {
        ArrayList<TipoAjusteCaixa> tipos = new ArrayList<>();
        tipos.add(new TipoAjusteCaixa(1, "Retirada de Valor", SAIDA));
        tipos.add(new TipoAjusteCaixa(2, "Depósito de Valor", ENTRADA));
        tipos.add(new TipoAjusteCaixa(3, "Roubo", SAIDA));
        tipos.add(new TipoAjusteCaixa(4, "Cédulas Falsificadas", SAIDA));
        tipos.add(new TipoAjusteCaixa(5, "Ajuste (Saída)", SAIDA));
        tipos.add(new TipoAjusteCaixa(6, "Ajuste (Entrada)", ENTRADA));
        return tipos;
    }

    public static class TipoAjusteCaixa {

        private Integer cdTpAjuste;
        private String dsTpAjuste;
        private String tpAjuste;

        public TipoAjusteCaixa(Integer cdTpAjuste, String dsTpAjuste, String tpAjuste) {
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

}
