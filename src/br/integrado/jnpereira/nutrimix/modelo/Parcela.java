package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
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
    @Coluna(nome = "dt_vencto")
    private Date dtVencto;
    @Coluna(nome = "vl_parcela")
    private Double vlParcela;
    @Coluna(nome = "vl_multa")
    private Double vlMulta;
    @Coluna(nome = "vl_desconto")
    private Double vlDesconto;
    @Coluna(nome="in_paga")
    private Boolean inPaga;
    
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

    public Double getVlMulta() {
        return vlMulta;
    }

    public void setVlMulta(Double vlMulta) {
        this.vlMulta = vlMulta;
    }

    public Double getVlDesconto() {
        return vlDesconto;
    }

    public void setVlDesconto(Double vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public Boolean getInPaga() {
        return inPaga;
    }

    public void setInPaga(Boolean inPaga) {
        this.inPaga = inPaga;
    }
    
}
