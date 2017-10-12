package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class FrmLoginFXML implements Initializable {

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void iniciaTela() {

    }

    @FXML
    public void login() {
        try {
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
