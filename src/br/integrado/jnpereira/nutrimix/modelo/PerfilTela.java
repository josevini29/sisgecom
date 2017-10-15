package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "perfil_tela")
public class PerfilTela {

    @Id
    @Coluna(nome = "cd_perfil")
    private Integer cdPerfil;
    @Id
    @Coluna(nome = "cd_tela")
    private String cdTela;

    public Integer getCdPerfil() {
        return cdPerfil;
    }

    public void setCdPerfil(Integer cdPerfil) {
        this.cdPerfil = cdPerfil;
    }

    public String getCdTela() {
        return cdTela;
    }

    public void setCdTela(String cdTela) {
        this.cdTela = cdTela;
    }
    
    

}
