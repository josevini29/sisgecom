/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;

@Tabela(nome = "conta")
public class Conta {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_conta")
    private Integer cdConta;
    @Id
    @Coluna(nome = "cd_fornecedor")
    private Integer cdFornecedor;
    @Coluna(nome = "cd_banco")
    private Integer cdBanco;
    @Coluna(nome = "nr_agencia")
    private String nrAgencia;
    @Coluna(nome = "nr_Conta")
    private String nrConta;

    public Integer getCdConta() {
        return cdConta;
    }

    public void setCdConta(Integer cdConta) {
        this.cdConta = cdConta;
    }

    public Integer getCdFornecedor() {
        return cdFornecedor;
    }

    public void setCdFornecedor(Integer cdFornecedor) {
        this.cdFornecedor = cdFornecedor;
    }

    public Integer getCdBanco() {
        return cdBanco;
    }

    public void setCdBanco(Integer cdBanco) {
        this.cdBanco = cdBanco;
    }

    public String getNrAgencia() {
        return nrAgencia;
    }

    public void setNrAgencia(String nrAgencia) {
        this.nrAgencia = nrAgencia;
    }

    public String getNrConta() {
        return nrConta;
    }

    public void setNrConta(String nrConta) {
        this.nrConta = nrConta;
    }

}
