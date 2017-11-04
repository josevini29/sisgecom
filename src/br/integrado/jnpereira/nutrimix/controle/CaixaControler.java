package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;

public class CaixaControler {
    
    Dao dao = new Dao();
    
    public CaixaControler(){
        dao.autoCommit(false);
    }
    
    public void geraMovtoCaixa(Parcela parcela){
        
    }
    
}
