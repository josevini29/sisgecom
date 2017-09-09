package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "movto_estoque")
public class MovtoEstoque {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_estqmovto")
    private Integer cdEstqMovto;
    @Coluna(nome = "tp_movto")
    private String tpMovto;
    @Coluna(nome = "cd_produto")
    private Integer cdProduto;
    @Coluna(nome = "dt_movto")
    private Date dtMovto;
    @Coluna(nome = "qt_movto")
    private Double qtMovto;
    @Coluna(nome = "vl_item")
    private Double vlItem;
    @Coluna(nome = "qt_estoque")
    private Double qtEstoque;
    @Coluna(nome = "vl_customedio")
    private Double vlCustoMedio;
    @Coluna(nome = "cd_ajuste")
    private Integer cdAjuste;
    @Coluna(nome = "cd_movcompvend")
    private Integer cdMovCompVend;
    @Coluna(nome="cd_usercancel")
    private Integer cdUserCancel;
    @Coluna(nome="dt_cancelado")
    private Date dtCancelado;
    @Coluna(nome = "in_cancelado")
    private Boolean inCancelado;

    public Integer getCdEstqMovto() {
        return cdEstqMovto;
    }

    public void setCdEstqMovto(Integer cdEstqMovto) {
        this.cdEstqMovto = cdEstqMovto;
    }

    public String getTpMovto() {
        return tpMovto;
    }

    public void setTpMovto(String tpMovto) {
        this.tpMovto = tpMovto;
    }

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Date getDtMovto() {
        return dtMovto;
    }

    public void setDtMovto(Date dtMovto) {
        this.dtMovto = dtMovto;
    }

    public Double getQtMovto() {
        return qtMovto;
    }

    public void setQtMovto(Double qtMovto) {
        this.qtMovto = qtMovto;
    }

    public Double getVlItem() {
        return vlItem;
    }

    public void setVlItem(Double vlItem) {
        this.vlItem = vlItem;
    }

    public Double getQtEstoque() {
        return qtEstoque;
    }

    public void setQtEstoque(Double qtEstoque) {
        this.qtEstoque = qtEstoque;
    }

    public Integer getCdAjuste() {
        return cdAjuste;
    }

    public void setCdAjuste(Integer cdAjuste) {
        this.cdAjuste = cdAjuste;
    }

    public Integer getCdMovCompVend() {
        return cdMovCompVend;
    }

    public void setCdMovCompVend(Integer cdMovCompVend) {
        this.cdMovCompVend = cdMovCompVend;
    }

    public Boolean getInCancelado() {
        return inCancelado;
    }

    public void setInCancelado(Boolean inCancelado) {
        this.inCancelado = inCancelado;
    }

    public Double getVlCustoMedio() {
        return vlCustoMedio;
    }

    public void setVlCustoMedio(Double vlCustoMedio) {
        this.vlCustoMedio = vlCustoMedio;
    }

    public Integer getCdUserCancel() {
        return cdUserCancel;
    }

    public void setCdUserCancel(Integer cdUserCancel) {
        this.cdUserCancel = cdUserCancel;
    }

    public Date getDtCancelado() {
        return dtCancelado;
    }

    public void setDtCancelado(Date dtCancelado) {
        this.dtCancelado = dtCancelado;
    }

}
