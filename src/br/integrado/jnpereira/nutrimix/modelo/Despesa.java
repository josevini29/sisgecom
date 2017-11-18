    
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome="despesa")
public class Despesa {
    
    @Id
    @AutoIncrement
    @Coluna(nome = "cd_despesa")
    private Integer cdDespesa;
    @Coluna(nome = "cd_tpdespesa")
    private Integer cdTipoDespesa;
    @Coluna(nome = "dt_despesa")
    private Date dtDespesa;
    @Coluna(nome = "vl_despesa")
    private Double vlDespesa;
    @Coluna(nome = "ds_obs")
    private String dsObs;
    @Coluna(nome="cd_usercad")
    private Integer cdUserCad;
    @Coluna(nome="dt_cadastro")
    private Date dtCadastro;

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

    public Integer getCdDespesa() {
        return cdDespesa;
    }

    public void setCdDespesa(Integer cdDespesa) {
        this.cdDespesa = cdDespesa;
    }

    public Integer getCdTipoDespesa() {
        return cdTipoDespesa;
    }

    public void setCdTipoDespesa(Integer cdTipoDespesa) {
        this.cdTipoDespesa = cdTipoDespesa;
    }

    public Date getDtDespesa() {
        return dtDespesa;
    }

    public void setDtDespesa(Date dtDespesa) {
        this.dtDespesa = dtDespesa;
    }

    public Double getVlDespesa() {
        return vlDespesa;
    }

    public void setVlDespesa(Double vlDespesa) {
        this.vlDespesa = vlDespesa;
    }

    public String getDsObs() {
        return dsObs;
    }

    public void setDsObs(String dsObs) {
        this.dsObs = dsObs;
    }

}
