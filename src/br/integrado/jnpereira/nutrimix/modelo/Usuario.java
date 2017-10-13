package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;

@Tabela(nome = "usuario")
public class Usuario {

    @Id
    @Coluna(nome = "cd_usuario")
    private Integer cdUsuario;
    @Coluna(nome = "cd_perfil")
    private Integer cdPerfil;
    @Coluna(nome = "ds_login")
    private String dsLogin;
    @Coluna(nome = "ds_senha")
    private String dsSenha;
    @Coluna(nome = "cd_usercad")
    private Integer cdUsercad;
    @Coluna(nome = "dt_cadastro")
    private Date dtCadastro;
    @Coluna(nome = "in_ativo")
    private Boolean inAtivo;
    @Coluna(nome="cd_funcionario")
    private Integer cdFuncionario;

    public Integer getCdUsuario() {
        return cdUsuario;
    }

    public void setCdUsuario(Integer cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    public Integer getCdPerfil() {
        return cdPerfil;
    }

    public void setCdPerfil(Integer cdPerfil) {
        this.cdPerfil = cdPerfil;
    }

    public String getDsLogin() {
        return dsLogin;
    }

    public void setDsLogin(String dsLogin) {
        this.dsLogin = dsLogin;
    }

    public String getDsSenha() {
        return dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public Integer getCdUsercad() {
        return cdUsercad;
    }

    public void setCdUsercad(Integer cdUsercad) {
        this.cdUsercad = cdUsercad;
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

    public Integer getCdFuncionario() {
        return cdFuncionario;
    }

    public void setCdFuncionario(Integer cdFuncionario) {
        this.cdFuncionario = cdFuncionario;
    }

    
}
