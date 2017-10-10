package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class FrmLoginFXML implements Initializable {

    public Stage stage;

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
        } catch (IOException ex) {
            Alerta.AlertaError("Erro!", ex.toString());
            System.exit(0);
        }
    }

    @FXML
    public void sair() {
        System.exit(0);
    }

}
