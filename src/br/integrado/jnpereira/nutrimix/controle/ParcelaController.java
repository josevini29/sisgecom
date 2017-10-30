package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ParcelaController {

    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat dfs = new DecimalFormat("0.00");
    Dao dao = new Dao();

    public ParcelaController() {
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void gerarParcelas(ContasPagarReceber conta) throws Exception {
        dao.autoCommit(false);
        CondicaoPagto condicao = new CondicaoPagto();
        condicao.setCdCondicao(conta.getCdCondicao());
        dao.get(condicao);
        for (Parcela parcela : getParcelas(condicao, conta.getVlConta())) {
            parcela.setCdConta(conta.getCdConta());
            dao.save(parcela);
        }
        
        //Pagar ultima parcela
    }

    public ArrayList<Parcela> getParcelas(CondicaoPagto condicao, Double vlTotal) {
        ArrayList<Parcela> parcelas = new ArrayList<>();
        Double vlParcela = Double.parseDouble(df.format(vlTotal / condicao.getQtParcelas()).replace(",", "."));
        Double vlSobra = Double.parseDouble(dfs.format(vlTotal - (vlParcela * condicao.getQtParcelas())).replace(",", "."));
        Date data = Data.getAgora();
        for (int nrParcela = 1; nrParcela <= condicao.getQtParcelas(); nrParcela++) {
            Parcela parcela = new Parcela();
            parcela.setCdParcela(nrParcela);
            if (condicao.getInEntrada() && nrParcela == 1) {
                parcela.setDtVencto(data);
            } else {
                data = dataMaisDias(data, condicao.getNrIntervalo());
                parcela.setDtVencto(data);
            }

            if (nrParcela == condicao.getQtParcelas()) {
                parcela.setVlParcela(Double.parseDouble(dfs.format(vlParcela + vlSobra).replace(",", ".")));
            } else {
                parcela.setVlParcela(Double.parseDouble(dfs.format(vlParcela).replace(",", ".")));
            }
            parcela.setInPaga(false);
            parcela.setVlMulta(0.00);
            parcela.setVlDesconto(0.00);
            parcelas.add(parcela);
        }
        return parcelas;
    }

    private Date dataMaisDias(Date data, int qtDias) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, +qtDias);
        return c.getTime();
    }

}
