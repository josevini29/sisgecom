package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome="pedido")
public class Pedido {
    
    @Id
    @AutoIncrement
    @Coluna(nome="cd_pedido")
    private Integer cdPedido;
    @Coluna(nome="dt_pedido")
    private Date dtPedido;
    @Coluna(nome="st_pedido")
    private String stPedido;
    @Coluna(nome="cd_fornecedor")
    private Integer cdFornecedor;
    @Coluna(nome="cd_usercad")
    private Integer cdUserCad;
    @Coluna(nome="dt_cadastro")
    private Date dtCadastro;

    public Integer getCdPedido() {
        return cdPedido;
    }

    public void setCdPedido(Integer cdPedido) {
        this.cdPedido = cdPedido;
    }

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtPedido) {
        this.dtPedido = dtPedido;
    }

    public String getStPedido() {
        return stPedido;
    }

    public void setStPedido(String stPedido) {
        this.stPedido = stPedido;
    }

    public Integer getCdFornecedor() {
        return cdFornecedor;
    }

    public void setCdFornecedor(Integer cdFornecedor) {
        this.cdFornecedor = cdFornecedor;
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
