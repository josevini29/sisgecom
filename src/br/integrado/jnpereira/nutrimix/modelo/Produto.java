package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "produto")
public class Produto {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_produto")
    private Integer cdProduto;
    @Coluna(nome = "cd_grupo")
    private Integer cdGrupo;
    @Coluna(nome = "cd_undpadrao")
    private String cdUndPadrao;
    @Coluna(nome = "cd_undpadcompra")
    private String cdUndPadraoCompra;
    @Coluna(nome = "qt_conversao")
    private Double qtConversao;
    @Coluna(nome = "nm_produto")
    private String dsProduto;
    @Coluna(nome = "qt_estqminimo")
    private Double qtEstoqMin;
    @Coluna(nome = "qt_estqatual")
    private Double qtEstqAtual;
    @Coluna(nome = "in_estoque")
    private Boolean inEstoque;
    @Coluna(nome = "in_consumo")
    private Boolean inConsumo;
    @Coluna(nome = "in_venda")
    private Boolean inVenda;
    @Coluna(nome = "vl_customedio")
    private Double vlCustoMedio;
    @Coluna(nome = "cd_usercad")
    private Integer cdUsuario;
    @Coluna(nome = "dt_cadastro")
    private Date dtCadastro;
    @Coluna(nome = "in_ativo")
    private Boolean inAtivo;

    public Integer getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Integer cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Integer getCdGrupo() {
        return cdGrupo;
    }

    public void setCdGrupo(Integer cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    public String getCdUndPadrao() {
        return cdUndPadrao;
    }

    public void setCdUndPadrao(String cdUndPadrao) {
        this.cdUndPadrao = cdUndPadrao;
    }

    public String getCdUndPadraoCompra() {
        return cdUndPadraoCompra;
    }

    public void setCdUndPadraoCompra(String cdUndPadraoCompra) {
        this.cdUndPadraoCompra = cdUndPadraoCompra;
    }

    public Double getQtConversao() {
        return qtConversao;
    }

    public void setQtConversao(Double qtConversao) {
        this.qtConversao = qtConversao;
    }

    public String getDsProduto() {
        return dsProduto;
    }

    public void setDsProduto(String dsProduto) {
        this.dsProduto = dsProduto;
    }

    public Double getQtEstoqMin() {
        return qtEstoqMin;
    }

    public void setQtEstoqMin(Double qtEstoqMin) {
        this.qtEstoqMin = qtEstoqMin;
    }

    public Double getQtEstqAtual() {
        return qtEstqAtual;
    }

    public void setQtEstqAtual(Double qtEstqAtual) {
        this.qtEstqAtual = qtEstqAtual;
    }

    public Boolean getInEstoque() {
        return inEstoque;
    }

    public void setInEstoque(Boolean inEstoque) {
        this.inEstoque = inEstoque;
    }

    public Boolean getInConsumo() {
        return inConsumo;
    }

    public void setInConsumo(Boolean inConsumo) {
        this.inConsumo = inConsumo;
    }

    public Boolean getInVenda() {
        return inVenda;
    }

    public void setInVenda(Boolean inVenda) {
        this.inVenda = inVenda;
    }

    public Double getVlCustoMedio() {
        return vlCustoMedio;
    }

    public void setVlCustoMedio(Double vlCustoMedio) {
        this.vlCustoMedio = vlCustoMedio;
    }

    public Integer getCdUsuario() {
        return cdUsuario;
    }

    public void setCdUsuario(Integer cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Boolean getInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(Boolean inAtivo) {
        this.inAtivo = inAtivo;
    }

   
}
