package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "tipo_despesa")
public class TipoDespesa {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_tpdespesa")
    private Integer cdTipoDespesa;
    @Coluna(nome = "ds_tpdespesa")
    private String dsTipoDespesa;
    @Coluna(nome = "in_ativo")
    private Boolean inAtivo;

    public Integer getCdTipoDespesa() {
        return cdTipoDespesa;
    }

    public void setCdTipoDespesa(Integer cdTipoDespesa) {
        this.cdTipoDespesa = cdTipoDespesa;
    }

    public String getDsTipoDespesa() {
        return dsTipoDespesa;
    }

    public void setDsTipoDespesa(String dsTipoDespesa) {
        this.dsTipoDespesa = dsTipoDespesa;
    }

    public Boolean getInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(Boolean inAtivo) {
        this.inAtivo = inAtivo;
    }

}
