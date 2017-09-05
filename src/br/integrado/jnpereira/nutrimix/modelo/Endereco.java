package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "endereco")
public class Endereco {

    @Id
    @Coluna(nome = "cd_pessoa")
    private Integer cdPessoa;
    @Id
    @AutoIncrement
    @Coluna(nome = "cd_endereco")
    private Integer cdEndereco;
    @Coluna(nome = "cd_cidade")
    private Integer cdCidade;
    @Coluna(nome = "tp_endereco")
    private Integer tpEndereco;
    @Coluna(nome = "ds_logradouro")
    private String dsLogradouro;
    @Coluna(nome = "ds_complemento")
    private String dsComplemeto;
    @Coluna(nome = "nr_imovel")
    private String nrImovel;
    @Coluna(nome = "nm_bairro")
    private String dsBairro;
    @Coluna(nome = "cd_cep")
    private String cdCep;

    public Integer getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(Integer cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public Integer getCdEndereco() {
        return cdEndereco;
    }

    public void setCdEndereco(Integer cdEndereco) {
        this.cdEndereco = cdEndereco;
    }

    public Integer getCdCidade() {
        return cdCidade;
    }

    public void setCdCidade(Integer cdCidade) {
        this.cdCidade = cdCidade;
    }

    public Integer getTpEndereco() {
        return tpEndereco;
    }

    public void setTpEndereco(Integer tpEndereco) {
        this.tpEndereco = tpEndereco;
    }

    public String getDsLogradouro() {
        return dsLogradouro;
    }

    public void setDsLogradouro(String dsLogradouro) {
        this.dsLogradouro = dsLogradouro;
    }

    public String getDsComplemeto() {
        return dsComplemeto;
    }

    public void setDsComplemeto(String dsComplemeto) {
        this.dsComplemeto = dsComplemeto;
    }

    public String getNrImovel() {
        return nrImovel;
    }

    public void setNrImovel(String nrImovel) {
        this.nrImovel = nrImovel;
    }

    public String getDsBairro() {
        return dsBairro;
    }

    public void setDsBairro(String dsBairro) {
        this.dsBairro = dsBairro;
    }

    public String getCdCep() {
        return cdCep;
    }

    public void setCdCep(String cdCep) {
        this.cdCep = cdCep;
    }
}
