package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "fechamento_caixa")
public class FechamentoCaixa {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_fechamento")
    private Integer cdFechamento;
    @Coluna(nome = "dt_abertura")
    private Date dtAbertura;
    @Coluna(nome = "dt_fechamento")
    private Date dtFechamento;
    @Coluna(nome = "vl_inicial")
    private Double vlInicial;
    @Coluna(nome = "vl_final")
    private Double vlFinal;
    @Coluna(nome = "cd_useraber")
    private Integer cdUserAber;
    @Coluna(nome = "cd_userfech")
    private Integer cdUserFech;

    public Integer getCdFechamento() {
        return cdFechamento;
    }

    public void setCdFechamento(Integer cdFechamento) {
        this.cdFechamento = cdFechamento;
    }

    public Date getDtAbertura() {
        return dtAbertura;
    }

    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }

    public Double getVlInicial() {
        return vlInicial;
    }

    public void setVlInicial(Double vlInicial) {
        this.vlInicial = vlInicial;
    }

    public Double getVlFinal() {
        return vlFinal;
    }

    public void setVlFinal(Double vlFinal) {
        this.vlFinal = vlFinal;
    }

    public Integer getCdUserAber() {
        return cdUserAber;
    }

    public void setCdUserAber(Integer cdUserAber) {
        this.cdUserAber = cdUserAber;
    }

    public Integer getCdUserFech() {
        return cdUserFech;
    }

    public void setCdUserFech(Integer cdUserFech) {
        this.cdUserFech = cdUserFech;
    }

}
