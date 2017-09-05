
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome="preco")
public class PrecoProduto {
    
    @Id
    @Coluna(nome="cd_produto")
    private Integer cdProduto;
    @Coluna(nome="dt_inicio")
    private Date dtPreco;
    @Coluna(nome="vl_venda")
    private Double vlPreco;

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Date getDtPreco() {
        return dtPreco;
    }

    public void setDtPreco(Date dtPreco) {
        this.dtPreco = dtPreco;
    }

    public Double getVlPreco() {
        return vlPreco;
    }

    public void setVlPreco(Double vlPreco) {
        this.vlPreco = vlPreco;
    }    
    
}
