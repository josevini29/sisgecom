package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "movto_caixa")
public class MovtoCaixa {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_movto_caixa")
    private Integer cdMovtoCaixa;
    @Coluna(nome = "tp_movto_caixa")
    private String tpMovtoCaixa;
    @Coluna(nome = "dt_movto")
    private Date dtMovto;
    @Coluna(nome = "vl_movto")
    private Double vlMovto;
    @Coluna(nome = "cd_forpagto")
    private Integer cdFormaPagto;
    @Coluna(nome = "cd_parcela")
    private Integer cdParcela;
    @Coluna(nome = "cd_conta")
    private Integer cdConta;
    @Coluna(nome = "cd_ajuste")
    private Integer cdAjuste;
    @Coluna(nome = "cd_fechamento")
    private Integer cdFechamento;

    public Integer getCdMovtoCaixa() {
        return cdMovtoCaixa;
    }

    public void setCdMovtoCaixa(Integer cdMovtoCaixa) {
        this.cdMovtoCaixa = cdMovtoCaixa;
    }

    public String getTpMovtoCaixa() {
        return tpMovtoCaixa;
    }

    public void setTpMovtoCaixa(String tpMovtoCaixa) {
        this.tpMovtoCaixa = tpMovtoCaixa;
    }

    public Date getDtMovto() {
        return dtMovto;
    }

    public void setDtMovto(Date dtMovto) {
        this.dtMovto = dtMovto;
    }

    public Double getVlMovto() {
        return vlMovto;
    }

    public void setVlMovto(Double vlMovto) {
        this.vlMovto = vlMovto;
    }

    public Integer getCdFormaPagto() {
        return cdFormaPagto;
    }

    public void setCdFormaPagto(Integer cdFormaPagto) {
        this.cdFormaPagto = cdFormaPagto;
    }

    public Integer getCdParcela() {
        return cdParcela;
    }

    public void setCdParcela(Integer cdParcela) {
        this.cdParcela = cdParcela;
    }

    public Integer getCdConta() {
        return cdConta;
    }

    public void setCdConta(Integer cdConta) {
        this.cdConta = cdConta;
    }
    
    public Integer getCdAjuste() {
        return cdAjuste;
    }

    public void setCdAjuste(Integer cdAjuste) {
        this.cdAjuste = cdAjuste;
    }

    public Integer getCdFechamento() {
        return cdFechamento;
    }

    public void setCdFechamento(Integer cdFechamento) {
        this.cdFechamento = cdFechamento;
    }

   

}
