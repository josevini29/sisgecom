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

@Tabela(nome="perfil")
public class Perfil {
    
    @Id
    @AutoIncrement
    @Coluna(nome="cd_perfil")
    private Integer cdPerfil;
    @Coluna(nome="ds_perfil")
    private String dsPerfil;

    public Integer getCdPerfil() {
        return cdPerfil;
    }

    public void setCdPerfil(Integer cdPerfil) {
        this.cdPerfil = cdPerfil;
    }

    public String getDsPerfil() {
        return dsPerfil;
    }

    public void setDsPerfil(String dsPerfil) {
        this.dsPerfil = dsPerfil;
    }
    
    
    
}
