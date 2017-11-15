package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.AjusteCaixa;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.MovtoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Data;
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
