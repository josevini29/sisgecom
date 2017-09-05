
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome="unidade_padrao")
public class UnidadePadrao {
    
    @Id
    @Coluna(nome="cd_undpadrao")
    private String cdUnidadePadrao;
    @Coluna(nome="ds_undpadrao")
    private String dsUnidadePadrao;

    public String getCdUnidadePadrao() {
        return cdUnidadePadrao;
    }

    public void setCdUnidadePadrao(String cdUnidadePadrao) {
        this.cdUnidadePadrao = cdUnidadePadrao;
    }

    public String getDsUnidadePadrao() {
        return dsUnidadePadrao;
    }

    public void setDsUnidadePadrao(String dsUnidadePadrao) {
        this.dsUnidadePadrao = dsUnidadePadrao;
    }
    
}
