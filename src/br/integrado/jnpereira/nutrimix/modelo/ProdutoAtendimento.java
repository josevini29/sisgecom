
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome="produto_atendimento")
public class ProdutoAtendimento {
 
    @Id
    @Coluna(nome="cd_atend")
    private Integer cdAtend;
    @Id
    @Coluna(nome="cd_produto")
    private Integer cdProduto;
    @Coluna(nome="qt_produto")
    private Double qtProduto;
    @Coluna(nome="qtPaga")
    private Double qtPaga;
    @Coluna(nome="st_item")
    private String stItem;
    @Coluna(nome="cd_usercad")
    private Integer cdUserCad;
    @Coluna(nome="dt_cadastro")
    private Date dtCadastro;

    public Integer getCdAtend() {
        return cdAtend;
    }

    public void setCdAtend(Integer cdAtend) {
        this.cdAtend = cdAtend;
    }

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Double getQtProduto() {
        return qtProduto;
    }

    public void setQtProduto(Double qtProduto) {
        this.qtProduto = qtProduto;
    }

    public Double getQtPaga() {
        return qtPaga;
    }

    public void setQtPaga(Double qtPaga) {
        this.qtPaga = qtPaga;
    }

    public String getStItem() {
        return stItem;
    }

    public void setStItem(String stItem) {
        this.stItem = stItem;
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
