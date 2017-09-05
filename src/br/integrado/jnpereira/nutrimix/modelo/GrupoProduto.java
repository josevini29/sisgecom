package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome="grupo_produto")
public class GrupoProduto {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_grupo")
    private Integer cdGrupo;
    @Coluna(nome = "nm_grupo")
    private String dsGrupo;

    public Integer getCdGrupo() {
        return cdGrupo;
    }

    public void setCdGrupo(Integer cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    public String getDsGrupo() {
        return dsGrupo;
    }

    public void setDsGrupo(String dsGrupo) {
        this.dsGrupo = dsGrupo;
    }
    
    
}
