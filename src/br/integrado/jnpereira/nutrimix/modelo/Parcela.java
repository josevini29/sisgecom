package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "parcela")
public class Parcela {

    @Id
    @Coluna(nome = "cd_conta")
    private Integer cdConta;
    @Id
    @Coluna(nome = "cd_parcela")
    private Integer cdParcela;
    @Coluna(nome = "tp_movto")
    private String tpMovto;
    @Coluna(nome = "dt_vencto")
    private Date dtVencto;
    @Coluna(nome = "vl_parcela")
    private Double vlParcela;
    @Coluna(nome = "dt_pagto")
    private Date dtPagto;
    @Coluna(nome = "vl_pago")
    private Double vlPagto;
    @Coluna(nome = "vl_desconto")
    private Double vlDesconto;
    @Coluna(nome = "vl_multa")
    private Double vlMulta;
    @Coluna(nome = "cd_forpagto")
    private Integer cdForPagto;
    @Coluna(nome = "in_cancelada")
    private Boolean inCancelada;
    @Coluna(nome = "cd_usermovto")
    private Integer cdUserMovto;
    @Coluna(nome = "dt_ultmovto")
    private Date dtUltMovto;
    @Coluna(nome = "cd_parestorno")
    private Integer cdParEstorno;

    public String getTpMovto() {
        return tpMovto;
    }

    public void setTpMovto(String tpMovto) {
        this.tpMovto = tpMovto;
    }

    public Integer getCdConta() {
        return cdConta;
    }

    public void setCdConta(Integer cdConta) {
        this.cdConta = cdConta;
    }

    public Integer getCdParcela() {
        return cdParcela;
    }

    public void setCdParcela(Integer cdParcela) {
        this.cdParcela = cdParcela;
    }

    public Date getDtVencto() {
        return dtVencto;
    }

    public void setDtVencto(Date dtVencto) {
        this.dtVencto = dtVencto;
    }

    public Double getVlParcela() {
        return vlParcela;
    }

    public void setVlParcela(Double vlParcela) {
        this.vlParcela = vlParcela;
    }

    public Date getDtPagto() {
        return dtPagto;
    }

    public void setDtPagto(Date dtPagto) {
        this.dtPagto = dtPagto;
    }

    public Double getVlPagto() {
        return vlPagto;
    }

    public void setVlPagto(Double vlPagto) {
        this.vlPagto = vlPagto;
    }

    public Double getVlDesconto() {
        return vlDesconto;
    }

    public void setVlDesconto(Double vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public Double getVlMulta() {
        return vlMulta;
    }

    public void setVlMulta(Double vlMulta) {
        this.vlMulta = vlMulta;
    }

    public Integer getCdForPagto() {
        return cdForPagto;
    }

    public void setCdForPagto(Integer cdForPagto) {
        this.cdForPagto = cdForPagto;
    }

    public Integer getCdUserMovto() {
        return cdUserMovto;
    }

    public void setCdUserMovto(Integer cdUserMovto) {
        this.cdUserMovto = cdUserMovto;
    }

    public Date getDtUltMovto() {
        return dtUltMovto;
    }

    public void setDtUltMovto(Date dtUltMovto) {
        this.dtUltMovto = dtUltMovto;
    }

    public Boolean getInCancelada() {
        return inCancelada;
    }

    public void setInCancelada(Boolean inCancelada) {
        this.inCancelada = inCancelada;
    }

    public Integer getCdParEstorno() {
        return cdParEstorno;
    }

    public void setCdParEstorno(Integer cdParEstorno) {
        this.cdParEstorno = cdParEstorno;
    }

}
