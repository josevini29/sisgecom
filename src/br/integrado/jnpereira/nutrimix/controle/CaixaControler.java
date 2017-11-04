package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.MovtoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.util.ArrayList;
import java.util.Date;

public class CaixaControler {
    
    Dao dao = new Dao();
    
    public CaixaControler(){
        dao.autoCommit(false);        
    }
    
    public FechamentoCaixa getCaixaAberto(Date pDtFechamento) throws Exception{
        String where = "WHERE $dtAbertura$ BETWEEN '"+Data.AmericaToBrasilSemHora(pDtFechamento)+" 00:00:00' AND '"+Data.AmericaToBrasilSemHora(pDtFechamento)+" 23:59:59'"
                + "AND $dtFechamento$ IS NULL";
        ArrayList<Object> fec =  dao.getAllWhere(new FechamentoCaixa(), where);
        if (fec.isEmpty()){
             throw new Exception("Caixa para o dia "+Data.AmericaToBrasilSemHora(pDtFechamento)+" está fechado ou não foi aberto.");            
        }
        return (FechamentoCaixa) fec.get(0);
    }
    
    public void geraMovtoCaixaParcela(Parcela parcela, FechamentoCaixa fechamento) throws Exception{
        MovtoCaixa caixa = new MovtoCaixa();
        caixa.setTpMovtoCaixa(parcela.getTpMovto());
        caixa.setDtMovto(Data.getAgora());
        caixa.setCdFormaPagto(parcela.getCdForPagto());
        caixa.setCdFechamento(fechamento.getCdFechamento());
        caixa.setCdConta(parcela.getCdConta());
        caixa.setCdParcela(parcela.getCdParcela());
        dao.save(caixa);
    }
    
}
