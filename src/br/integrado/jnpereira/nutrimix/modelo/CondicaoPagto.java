package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "condicao")
public class CondicaoPagto {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_condicao")
    private Integer cdCondicao;
    @Coluna(nome = "ds_condicao")
    private String dsCondicao;
    @Coluna(nome = "qt_parcelas")
    private Integer qtParcelas;
    @Coluna(nome = "in_entrada")
    private Boolean inEntrada;
    @Coluna(nome = "nr_intervalo")
    private Integer nrIntervalo;
    @Coluna(nome = "in_ativo")
    private Boolean inAtivo;

    public Integer getCdCondicao() {
        return cdCondicao;
    }

    public void setCdCondicao(Integer cdCondicao) {
        this.cdCondicao = cdCondicao;
    }

    public String getDsCondicao() {
        return dsCondicao;
    }

    public void setDsCondicao(String dsCondicao) {
        this.dsCondicao = dsCondicao;
    }

    public Integer getQtParcelas() {
        return qtParcelas;
    }

    public void setQtParcelas(Integer qtParcelas) {
        this.qtParcelas = qtParcelas;
    }

    public Boolean getInEntrada() {
        return inEntrada;
    }

    public void setInEntrada(Boolean inEntrada) {
        this.inEntrada = inEntrada;
    }

    public Integer getNrIntervalo() {
        return nrIntervalo;
    }

    public void setNrIntervalo(Integer nrIntervalo) {
        this.nrIntervalo = nrIntervalo;
    }

    public Boolean getInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(Boolean inAtivo) {
        this.inAtivo = inAtivo;
    }

}
