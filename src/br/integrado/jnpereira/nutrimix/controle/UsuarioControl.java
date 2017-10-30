/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Funcionario;
import br.integrado.jnpereira.nutrimix.modelo.Perfil;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jose Vinicius
 */
public class UsuarioControl implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    AnchorPane painel;
    @FXML
    TextField cdUsuario;
    @FXML
    TextField dsLogin;
    @FXML
    CheckBox inAtivo;
    @FXML
    TextField cdPerfil;
    @FXML
    TextField dsPerfil;
    @FXML
    TextField cdFuncionario;
    @FXML
    TextField dsFuncionario;
    @FXML
    Button btnSenhaReset;
    @FXML
    Label lblCadastro;

    Usuario usuario;
    Dao dao = new Dao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdUsuario);
        FuncaoCampo.mascaraTexto(dsLogin, 15);
        FuncaoCampo.mascaraNumeroInteiro(cdPerfil);
        FuncaoCampo.mascaraNumeroInteiro(cdFuncionario);
        btnSenhaReset.setVisible(false);
        cdUsuario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoUsuario();
            }
        });
        cdPerfil.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoPerfil();
            }
        });
        cdFuncionario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoFuncionario();
            }
        });
    }

    public void iniciaTela() {

    }

    @FXML
    public void pesquisarUsuario() {
        if (usuario != null){
            return;
        }
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Usuario(), "cdUsuario", "dsLogin", "AND $cdUsuario$ != 0", "Lista de Usuário");
        if (valor != null) {
            cdUsuario.setText(valor);
            validaCodigoUsuario();
        }
    }

    @FXML
    public void pesquisarPerfil() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Perfil(), "cdPerfil", "dsPerfil", null, "Lista de Perfil");
        if (valor != null) {
            cdPerfil.setText(valor);
            validaCodigoPerfil();
        }
    }

    @FXML
    public void pesquisarFuncionario() {
        if (usuario != null){
            return;
        }
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Funcionario(), true);
        if (valor != null) {
            cdFuncionario.setText(valor);
            validaCodigoFuncionario();
        }
    }

    private void validaCodigoUsuario() {
        if (!cdUsuario.getText().equals("") & usuario == null) {
            try {
                if (Integer.parseInt(cdUsuario.getText()) == 0) {
                    Alerta.AlertaError("Campo inválido!", "Usuário inválido!");
                    cdUsuario.requestFocus();
                    return;
                }

                usuario = new Usuario();
                usuario.setCdUsuario(Integer.parseInt(cdUsuario.getText()));
                dao.get(usuario);

                dsLogin.setText(usuario.getDsLogin());
                inAtivo.setSelected(usuario.getInAtivo());

                cdPerfil.setText(usuario.getCdPerfil().toString());
                Perfil perfil = new Perfil();
                perfil.setCdPerfil(Integer.parseInt(cdPerfil.getText()));
                dao.get(perfil);
                dsPerfil.setText(perfil.getDsPerfil());

                cdFuncionario.setText(usuario.getCdFuncionario().toString());
                Funcionario func = new Funcionario();
                func.setCdFuncionario(Integer.parseInt(cdFuncionario.getText()));
                dao.get(func);
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(func.getCdPessoa());
                dao.get(pessoa);
                dsFuncionario.setText(pessoa.getDsPessoa());

                lblCadastro.setText(Numero.getCadastro(usuario.getCdUsercad(), usuario.getDtCadastro()));

                btnSenhaReset.setVisible(true);
                cdFuncionario.setEditable(false);
                cdFuncionario.getStyleClass().addAll("texto_estatico_center");
                cdUsuario.setEditable(false);
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                usuario = null;
                cdUsuario.requestFocus();
            }
        }
    }

    private void validaCodigoPerfil() {
        if (!cdPerfil.getText().equals("")) {
            try {
                Perfil perfil = new Perfil();
                perfil.setCdPerfil(Integer.parseInt(cdPerfil.getText()));
                dao.get(perfil);
                dsPerfil.setText(perfil.getDsPerfil());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                dsPerfil.setText("");
                cdPerfil.requestFocus();
            }
        }
    }

    private void validaCodigoFuncionario() {
        if (!cdFuncionario.getText().equals("") & usuario == null) {
            try {
                Funcionario func = new Funcionario();
                func.setCdFuncionario(Integer.parseInt(cdFuncionario.getText()));
                dao.get(func);
                if (func.getDtDemissao() != null) {
                    Alerta.AlertaError("Negado!", "Funcionário já demitido!");
                    cdFuncionario.requestFocus();
                    return;
                }
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(func.getCdPessoa());
                dao.get(pessoa);
                dsFuncionario.setText(pessoa.getDsPessoa());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                dsFuncionario.setText("");
                cdFuncionario.requestFocus();
            }
        }
    }

    @FXML
    public void salvar() {
        if (dsLogin.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Descrição do Login é obrigatório.");
            return;
        }
        if (cdPerfil.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Perfil é obrigatório.");
            return;
        }
        if (cdFuncionario.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Funcionário é obrigatório.");
            return;
        }

        boolean vInNovo = false;
        try {
            if (usuario == null) {
                long cont = dao.getCountWhere(new Usuario(), "WHERE $cdFuncionario$ = " + cdFuncionario.getText());
                if (cont > 0) {
                    Alerta.AlertaWarning("Não permitido", "Já existe um usuário cadastrado para este funcionário.");
                    return;
                }
            }

            dao.autoCommit(false);
            if (usuario == null) {
                vInNovo = true;
                usuario = new Usuario();
                usuario.setDsLogin(dsLogin.getText());
                usuario.setCdPerfil(Integer.parseInt(cdPerfil.getText()));
                usuario.setCdFuncionario(Integer.parseInt(cdFuncionario.getText()));
                usuario.setCdUsercad(MenuControl.usuarioAtivo);
                usuario.setDtCadastro(Data.getAgora());
                usuario.setDsSenha("123456");
                usuario.setInAtivo(inAtivo.isSelected());
                dao.save(usuario);
            } else {
                usuario.setDsLogin(dsLogin.getText());
                usuario.setCdPerfil(Integer.parseInt(cdPerfil.getText()));
                usuario.setInAtivo(inAtivo.isSelected());
                dao.update(usuario);
            }
            dao.commit();
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação!", ex.getMessage());
            dao.rollback();
        }

        cdUsuario.setText(usuario.getCdUsuario().toString());
        usuario = null;
        validaCodigoUsuario();
        if (vInNovo) {
            Alerta.AlertaInfo("Concluído", "Usuário salvo com sucesso!\nSenha: " + usuario.getDsSenha());
        } else {
            Alerta.AlertaInfo("Concluído", "Usuário salvo com sucesso!");
        }
    }

    @FXML
    public void limpar() {
        usuario = null;
        FuncaoCampo.limparCampos(painel);
        cdUsuario.setEditable(true);
        cdFuncionario.setEditable(true);
        cdFuncionario.getStyleClass().removeAll();
        cdFuncionario.getStyleClass().addAll("codigo");
        inAtivo.setSelected(true);
        btnSenhaReset.setVisible(false);
        cdUsuario.requestFocus();
    }

    @FXML
    public void senhaReset() {
        if (Alerta.AlertaConfirmation("Reset de Senha", "Deseja realmente realizar o reset da senha deste usuário?")) {
            try {
                dao.autoCommit(false);
                usuario.setDsSenha("123456");
                dao.update(usuario);
                dao.commit();
                Alerta.AlertaInfo("Senha alterada!", "Nova senha: 123456");
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação!", ex.getMessage());
                dao.rollback();
            }
        }
    }
}
