package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "telefone")
public class Telefone {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_telefone")
    private Integer cdTelefone;
    @Id
    @Coluna(nome = "cd_pessoa")
    private Integer cdPessoa;
    @Coluna(nome = "tp_telefone")
    private String tpTelefone;
    @Coluna(nome = "tp_uso")
    private String tpUso;
    @Coluna(nome = "nr_ddd")
    private String nrDdd;
    @Coluna(nome = "nr_telefone")
    private String nrTelefone;

    public Integer getCdTelefone() {
        return cdTelefone;
    }

    public void setCdTelefone(Integer cdTelefone) {
        this.cdTelefone = cdTelefone;
    }

    public Integer getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(Integer cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public String getTpTelefone() {
        return tpTelefone;
    }

    public void setTpTelefone(String tpTelefone) {
        this.tpTelefone = tpTelefone;
    }

    public String getTpUso() {
        return tpUso;
    }

    public void setTpUso(String tpUso) {
        this.tpUso = tpUso;
    }

    public String getNrDdd() {
        return nrDdd;
    }

    public void setNrDdd(String nrDdd) {
        this.nrDdd = nrDdd;
    }

    public String getNrTelefone() {
        return nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        if (nrTelefone.length() == 8) {
            this.nrTelefone = "0" + nrTelefone;
        } else {
            this.nrTelefone = nrTelefone;
        }
    }

}
