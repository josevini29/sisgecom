package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FrmLoginFXML implements Initializable {

    @FXML
    TextField dsLogin;
    @FXML
    PasswordField dsSenha;

    private Stage stage;
    Dao dao = new Dao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraTexto(dsLogin, 15);
        FuncaoCampo.mascaraTexto(dsSenha, 6);
    }

    public void iniciaTela() {
        
    }

    @FXML
    public void login() {
        try {
            if (dsLogin.getText().equals("")) {
                Alerta.AlertaError("Acesso Negado!", "Login é obrigatório!");
                return;
            }

            if (dsSenha.getText().equals("")) {
                Alerta.AlertaError("Acesso Negado!", "Senha é obrigatório!");
                return;
            }

            String where = " WHERE $dsLogin$ = '" + dsLogin.getText().toLowerCase() + "' AND $dsSenha$ = '" + dsSenha.getText() + "'";
            ArrayList<Object> userArray = dao.getAllWhere(new Usuario(), where);
            if (userArray.isEmpty()) {
                Alerta.AlertaError("Acesso Negado!", "Usuário não existe ou senha incorreta.");
                return;
            }
            Usuario usuario = (Usuario) userArray.get(0);
            if (!usuario.getInAtivo()) {
                Alerta.AlertaError("Acesso Negado!", "Usuário inativo.");
                return;
            }
            FrmMenuFXML.usuarioAtivo = usuario.getCdUsuario();
            Tela tela = new Tela();
            tela.abrirMenu();
            stage.close();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.toString());
            System.exit(0);
        }
    }

    @FXML
    public void sair() {
        System.exit(0);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
