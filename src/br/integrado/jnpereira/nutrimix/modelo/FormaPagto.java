/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome="forma_pagto")
public class FormaPagto {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_forma")
    private Integer cdFormaPagto;
    @Coluna(nome = "ds_forma")
    private String dsFormaPagto;
    @Coluna(nome = "in_ativo")
    private Boolean inAtivo;

    public Integer getCdFormaPagto() {
        return cdFormaPagto;
    }

    public void setCdFormaPagto(Integer cdFormaPagto) {
        this.cdFormaPagto = cdFormaPagto;
    }

    public String getDsFormaPagto() {
        return dsFormaPagto;
    }

    public void setDsFormaPagto(String dsFormaPagto) {
        this.dsFormaPagto = dsFormaPagto;
    }

    public Boolean getInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(Boolean inAtivo) {
        this.inAtivo = inAtivo;
    }
    
    

}
