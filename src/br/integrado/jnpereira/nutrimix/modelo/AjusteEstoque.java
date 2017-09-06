package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "ajuste_estoque")
public class AjusteEstoque {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_ajuste")
    private Integer cdAjuste;
    @Coluna(nome = "dt_ajuste")
    private Date dtAjuste;
    @Coluna(nome = "tp_ajuste")
    private Integer tpAjuste;
    @Coluna(nome = "ds_obs")
    private String dsObs;
    @Coluna(nome = "cd_usercad")
    private Integer cdUserCad;
    @Coluna(nome = "dt_cadastro")
    private Date dtCadastro;

    public Integer getCdAjuste() {
        return cdAjuste;
    }

    public void setCdAjuste(Integer cdAjuste) {
        this.cdAjuste = cdAjuste;
    }

    public Date getDtAjuste() {
        return dtAjuste;
    }

    public void setDtAjuste(Date dtAjuste) {
        this.dtAjuste = dtAjuste;
    }

    public Integer getTpAjuste() {
        return tpAjuste;
    }

    public void setTpAjuste(Integer tpAjuste) {
        this.tpAjuste = tpAjuste;
    }

    public String getDsObs() {
        return dsObs;
    }

    public void setDsObs(String dsObs) {
        this.dsObs = dsObs;
    }

    public Integer getCdUserCad() {
        return cdUserCad;
    }

    public void setCdUserCad(Integer cdUserCad) {
        this.cdUserCad = cdUserCad;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

   
    
}
