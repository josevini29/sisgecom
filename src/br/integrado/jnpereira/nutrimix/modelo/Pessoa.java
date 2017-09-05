package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "pessoa")
public class Pessoa {

    @Id
    @AutoIncrement
    @Coluna(nome = "cd_pessoa")
    private Integer cdPessoa;
    @Coluna(nome = "tp_pessoa")
    private String tpPessoa;
    @Coluna(nome = "nr_cpfcnpj")
    private String nrCpfCnpj;
    @Coluna(nome = "nm_pessoa")
    private String dsPessoa;
    @Coluna(nome = "ds_apelido")
    private String dsApelido;
    @Coluna(nome = "nr_rg")
    private String nrRg;
    @Coluna(nome = "tp_sexo")
    private String tpSexo;
    @Coluna(nome = "ds_email")
    private String dsEmail;
    @Coluna(nome = "tp_estadcivil")
    private String tpCivil;
    @Coluna(nome = "dt_nasc")
    private Date dtNasc;
    @Coluna(nome = "nr_inscriestad")
    private String nrInscEstad;

    public Integer getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(Integer cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public String getTpPessoa() {
        return tpPessoa;
    }

    public void setTpPessoa(String tpPessoa) {
        this.tpPessoa = tpPessoa;
    }

    public String getNrCpfCnpj() {
        return nrCpfCnpj;
    }

    public void setNrCpfCnpj(String nrCpfCnpj) {
        this.nrCpfCnpj = nrCpfCnpj;
    }

    public String getDsPessoa() {
        return dsPessoa;
    }

    public void setDsPessoa(String dsPessoa) {
        this.dsPessoa = dsPessoa;
    }

    public String getDsApelido() {
        return dsApelido;
    }

    public void setDsApelido(String dsApelido) {
        this.dsApelido = dsApelido;
    }

    public String getNrRg() {
        return nrRg;
    }

    public void setNrRg(String nrRg) {
        this.nrRg = nrRg;
    }

    public String getTpSexo() {
        return tpSexo;
    }

    public void setTpSexo(String tpSexo) {
        this.tpSexo = tpSexo;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getTpCivil() {
        return tpCivil;
    }

    public void setTpCivil(String tpCivil) {
        this.tpCivil = tpCivil;
    }

    public Date getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(Date dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getNrInscEstad() {
        return nrInscEstad;
    }

    public void setNrInscEstad(String nrInscEstad) {
        this.nrInscEstad = nrInscEstad;
    }

}
