package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "ajuste_caixa")
public class AjusteCaixa {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_ajuste")
    private Integer cdAjuste;
    @Coluna(nome="cd_forpagto")
    private Integer cdForPagto;
    @Coluna(nome = "tp_ajuste")
    private String tpAjuste;
    @Coluna(nome = "vl_ajuste")
    private Double vlAjuste;
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

    public String getTpAjuste() {
        return tpAjuste;
    }

    public void setTpAjuste(String tpAjuste) {
        this.tpAjuste = tpAjuste;
    }

    public Double getVlAjuste() {
        return vlAjuste;
    }

    public void setVlAjuste(Double vlAjuste) {
        this.vlAjuste = vlAjuste;
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

    public Integer getCdForPagto() {
        return cdForPagto;
    }

    public void setCdForPagto(Integer cdForPagto) {
        this.cdForPagto = cdForPagto;
    }
    
    

}
