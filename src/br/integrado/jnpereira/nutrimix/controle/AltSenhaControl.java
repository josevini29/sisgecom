/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class AltSenhaControl implements Initializable {
    @FXML
    AnchorPane painel;
    @FXML
    PasswordField dsSenhaAtual;
    @FXML
    PasswordField dsSenhaNova1;
    @FXML
    PasswordField dsSenhaNova2;
    
    Dao dao = new Dao();
    
    public Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraTexto(dsSenhaAtual, 6);
        FuncaoCampo.mascaraTexto(dsSenhaNova1, 6);
        FuncaoCampo.mascaraTexto(dsSenhaNova2, 6);
    }
    
    public void iniciaTela() {
        
    }
    
    @FXML
    public void salvar() {
        if (dsSenhaAtual.getText().equals("")) {
            Alerta.AlertaError("Campo inválido!", "Senha atual é obrigatória");
            dsSenhaAtual.requestFocus();
            return;
        }
        if (dsSenhaNova1.getText().equals("")) {
            Alerta.AlertaError("Campo inválido!", "Nova Senha é obrigatória");
            dsSenhaNova1.requestFocus();
            return;
        }
        
        if (dsSenhaNova1.getText().length() != 6) {
            Alerta.AlertaError("Campo inválido!", "Nova Senha deve conter 6 caracteres.");
            dsSenhaNova1.requestFocus();
            return;
        }
        
        if (dsSenhaNova2.getText().equals("")) {
            Alerta.AlertaError("Campo inválido!", "Nova Senha é obrigatória");
            dsSenhaNova2.requestFocus();
            return;
        }
        
        if (!dsSenhaNova1.getText().equals(dsSenhaNova2.getText())) {
            Alerta.AlertaError("Campo inválido!", "Campos de Nova Senha estão diferentes.");
            dsSenhaNova1.requestFocus();
            return;
        }
        try {
            dao.autoCommit(false);
            String where = " WHERE $cdUsuario$ = " + MenuControl.usuarioAtivo + " AND $dsSenha$ = '" + dsSenhaAtual.getText() + "'";
            ArrayList<Object> userArray = dao.getAllWhere(new Usuario(), where);
            if (userArray.isEmpty()) {
                Alerta.AlertaError("Campo inválido!", "Senha Atual incorreta.");
                return;
            }
            Usuario usuario = new Usuario();
            usuario.setCdUsuario(MenuControl.usuarioAtivo);
            dao.get(usuario);
            usuario.setDsSenha(dsSenhaNova1.getText());
            dao.update(usuario);
            dao.commit();
            Alerta.AlertaInfo("concluído!", "Senha Alterada!");
            stage.close();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.toString());
            stage.close();
        }
    }
    
    @FXML
    public void limpar() {
        FuncaoCampo.limparCampos(painel);
    }
    
}
