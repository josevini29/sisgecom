package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "contas_pagar_receber")
public class ContasPagarReceber {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_conta")
    private Integer cdConta;
    @Coluna(nome = "dt_movto")
    private Date dtMovto;
    @Coluna(nome = "tp_movto")
    private String tpMovto;
    @Coluna(nome = "cd_condicao")
    private Integer cdCondicao;
    @Coluna(nome = "cd_forma")
    private Integer cdForma;
    @Coluna(nome = "cd_movto")
    private Integer cdMovto;
    @Coluna(nome = "cd_despesa")
    private Integer cdDespesa;
    @Coluna(nome = "vl_conta")
    private Double vlConta;
    @Coluna(nome = "st_conta")
    private String stConta;

    public Integer getCdConta() {
        return cdConta;
    }

    public void setCdConta(Integer cdConta) {
        this.cdConta = cdConta;
    }

    public String getTpMovto() {
        return tpMovto;
    }

    public void setTpMovto(String tpMovto) {
        this.tpMovto = tpMovto;
    }

    public Integer getCdCondicao() {
        return cdCondicao;
    }

    public void setCdCondicao(Integer cdCondicao) {
        this.cdCondicao = cdCondicao;
    }

    public Integer getCdForma() {
        return cdForma;
    }

    public void setCdForma(Integer cdForma) {
        this.cdForma = cdForma;
    }

    public Integer getCdMovto() {
        return cdMovto;
    }

    public void setCdMovto(Integer cdMovto) {
        this.cdMovto = cdMovto;
    }

    public Integer getCdDespesa() {
        return cdDespesa;
    }

    public void setCdDespesa(Integer cdDespesa) {
        this.cdDespesa = cdDespesa;
    }

    public Double getVlConta() {
        return vlConta;
    }

    public void setVlConta(Double vlConta) {
        this.vlConta = vlConta;
    }

    public String getStConta() {
        return stConta;
    }

    public void setStConta(String stConta) {
        this.stConta = stConta;
    }

    public Date getDtMovto() {
        return dtMovto;
    }

    public void setDtMovto(Date dtMovto) {
        this.dtMovto = dtMovto;
    }

}
