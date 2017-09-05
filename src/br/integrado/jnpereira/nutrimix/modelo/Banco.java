package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "banco")
public class Banco {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_banco")
    private Integer cdBanco;
    @Coluna(nome = "ds_banco")
    private String dsBanco;

    public Integer getCdBanco() {
        return cdBanco;
    }

    public void setCdBanco(Integer cdBanco) {
        this.cdBanco = cdBanco;
    }

    public String getDsBanco() {
        return dsBanco;
    }

    public void setDsBanco(String dsBanco) {
        this.dsBanco = dsBanco;
    }

}
