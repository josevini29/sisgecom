package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome="pedido_produto")
public class PedidoProduto {

    @Id
    @Coluna(nome="cd_pedido")
    private Integer cdPedido;
    @Id
    @Coluna(nome="cd_produto")
    private Integer cdProduto;
    @Coluna(nome="dt_preventrega")
    private Date dtPrevEntrega;
    @Coluna(nome="qt_produto")
    private Double qtProduto;
    @Coluna(nome="vl_unitario")
    private Double vlUnitario;
    @Coluna(nome="qt_entregue")
    private Double qtEntregue;

    public Integer getCdPedido() {
        return cdPedido;
    }

    public void setCdPedido(Integer cdPedido) {
        this.cdPedido = cdPedido;
    }

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Date getDtPrevEntrega() {
        return dtPrevEntrega;
    }

    public void setDtPrevEntrega(Date dtPrevEntrega) {
        this.dtPrevEntrega = dtPrevEntrega;
    }

    public Double getQtProduto() {
        return qtProduto;
    }

    public void setQtProduto(Double qtProduto) {
        this.qtProduto = qtProduto;
    }

    public Double getVlUnitario() {
        return vlUnitario;
    }

    public void setVlUnitario(Double vlUnitario) {
        this.vlUnitario = vlUnitario;
    }

    public Double getQtEntregue() {
        return qtEntregue;
    }

    public void setQtEntregue(Double qtEntregue) {
        this.qtEntregue = qtEntregue;
    }
    
    
}
