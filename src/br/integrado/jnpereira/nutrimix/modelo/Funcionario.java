package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "funcionario")
public class Funcionario {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_funcionario")
    private Integer cdFuncionario;
    @Coluna(nome = "cd_pessoa")
    private Integer cdPessoa;
    @Coluna(nome = "dt_admissao")
    private Date dtAdmissao;
    @Coluna(nome = "dt_demissao")
    private Date dtDemissao;
    @Coluna(nome = "nr_pis")
    private String nrPis;
    @Coluna(nome = "ds_cargo")
    private String dsCargo;
    @Coluna(nome = "cd_usercad")
    private Integer cdUserCad;
    @Coluna(nome = "dt_cadastro")
    private Date dtCadastro;

    public Integer getCdFuncionario() {
        return cdFuncionario;
    }

    public void setCdFuncionario(Integer cdFuncionario) {
        this.cdFuncionario = cdFuncionario;
    }

    public Integer getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(Integer cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public Date getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(Date dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public Date getDtDemissao() {
        return dtDemissao;
    }

    public void setDtDemissao(Date dtDemissao) {
        this.dtDemissao = dtDemissao;
    }

    public String getNrPis() {
        return nrPis;
    }

    public void setNrPis(String nrPis) {
        this.nrPis = nrPis;
    }

    public String getDsCargo() {
        return dsCargo;
    }

    public void setDsCargo(String dsCargo) {
        this.dsCargo = dsCargo;
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
