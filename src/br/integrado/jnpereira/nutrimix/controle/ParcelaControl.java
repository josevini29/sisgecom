package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ParcelaControl {

    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat dfs = new DecimalFormat("0.00");
    Dao dao = new Dao();

    public ParcelaControl() {
        dao.autoCommit(false);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void gerarParcelas(ContasPagarReceber conta, FechamentoCaixa fechamento) throws Exception {
        CondicaoPagto condicao = new CondicaoPagto();
        condicao.setCdCondicao(conta.getCdCondicao());
        dao.get(condicao);
        for (Parcela parcela : getParcelas(condicao, conta.getVlConta())) {
            parcela.setCdConta(conta.getCdConta());
            parcela.setTpMovto(conta.getTpMovto());
            parcela.setCdUserMovto(MenuControl.usuarioAtivo);
            parcela.setDtUltMovto(Data.getAgora());
            dao.save(parcela);

            if (parcela.getCdParcela() == 1 && condicao.getInEntrada()) { //Pagamento no caixa no momento da venda
                parcela.setCdForPagto(conta.getCdForma());
                parcela.setVlPagto(parcela.getVlParcela());
                parcela.setDtPagto(conta.getDtMovto());
                parcela.setVlDesconto(0.00);
                parcela.setVlMulta(0.00);
                dao.update(parcela);
                CaixaControler caixa = new CaixaControler();
                caixa.geraMovtoCaixaParcela(parcela, fechamento);
            }
        }

    }

    public void geraParcelaEstorno(Parcela parcela, FechamentoCaixa fechamentoCaixa) throws Exception {
        if (parcela.getDtPagto() != null) {
            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setCdConta(parcela.getCdConta());
            dao.get(conta);

            Parcela parcelaEstorno = new Parcela(); //Parcela de estorno
            long vCdParcela = dao.getCountWhere(new Parcela(), " WHERE $cdConta$ = " + conta.getCdConta());
            vCdParcela++;
            parcelaEstorno.setCdConta(parcela.getCdConta());
            parcelaEstorno.setCdParcela(Integer.parseInt(String.valueOf(vCdParcela)));
            String vTpMovto = parcela.getTpMovto().equals("S") ? "E" : "S"; //Movimento inverso
            parcelaEstorno.setTpMovto(vTpMovto);
            parcelaEstorno.setDtVencto(parcela.getDtVencto());
            parcelaEstorno.setVlParcela(parcela.getVlParcela());
            parcelaEstorno.setDtPagto(Data.getAgora());
            parcelaEstorno.setVlPagto(parcela.getVlPagto());
            parcelaEstorno.setVlDesconto(0.00);
            parcelaEstorno.setVlMulta(0.00);
            parcelaEstorno.setCdForPagto(parcela.getCdForPagto());
            parcelaEstorno.setInCancelada(false);
            parcelaEstorno.setCdUserMovto(parcela.getCdUserMovto());
            parcelaEstorno.setDtUltMovto(Data.getAgora());
            parcelaEstorno.setCdParEstorno(parcela.getCdParcela());
            dao.save(parcelaEstorno);
            
            CaixaControler caixa = new CaixaControler();
            caixa.geraMovtoCaixaParcela(parcelaEstorno, fechamentoCaixa);

            Parcela parcelaNova = new Parcela(); //Parcela Nova
            parcelaNova.setCdConta(parcela.getCdConta());
            long vCdParcelaNova = dao.getCountWhere(new Parcela(), " WHERE $cdConta$ = " + conta.getCdConta());
            vCdParcelaNova++;
            parcelaNova.setCdParcela(Integer.parseInt(String.valueOf(vCdParcelaNova)));
            parcelaNova.setTpMovto(parcela.getTpMovto());
            parcelaNova.setDtVencto(parcela.getDtVencto());
            parcelaNova.setVlParcela(parcela.getVlParcela());
            parcelaNova.setInCancelada(false);
            parcelaNova.setCdUserMovto(parcela.getCdUserMovto());
            parcelaNova.setDtUltMovto(Data.getAgora());
            dao.save(parcelaNova);
            
            if (conta.getStConta().equals("2")){ //Caso a conta esteje encerrada
                conta.setStConta("1");
                dao.update(conta);
            }
        }
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
            parcela.setInCancelada(false);
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

    public void encerrarConta(ContasPagarReceber conta) throws Exception {
        boolean vInEncerra = true;
        String where = "WHERE $cdConta$ = " + conta.getCdConta() + " AND $inCancelada$ = 'F'";
        ArrayList<Object> parcelas = dao.getAllWhere(new Parcela(), where);
        for (Object obj : parcelas) {
            Parcela parcela = (Parcela) obj;
            if (parcela.getDtPagto() == null) {
                vInEncerra = false;
            }
        }
        if (vInEncerra) {
            conta.setStConta("2");
            dao.update(conta);
        }
    }

}
