/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome="venda_compra_produto")
public class VendaCompraProduto {
    
    @Id
    @Coluna(nome="cd_movto")
    private Integer cdMovto;
    @Id
    @Coluna(nome="cd_produto")
    private Integer cdProduto;
    @Coluna(nome="qt_unitario")
    private Double qtUnitario;
    @Coluna(nome="vl_unitario")
    private Double vlUnitario;

    public Integer getCdMovto() {
        return cdMovto;
    }

    public void setCdMovto(Integer cdMovto) {
        this.cdMovto = cdMovto;
    }

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Double getVlUnitario() {
        return vlUnitario;
    }

    public void setVlUnitario(Double vlUnitario) {
        this.vlUnitario = vlUnitario;
    }

    public Double getQtUnitario() {
        return qtUnitario;
    }

    public void setQtUnitario(Double qtUnitario) {
        this.qtUnitario = qtUnitario;
    }
      
}
