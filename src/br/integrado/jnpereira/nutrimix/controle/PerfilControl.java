/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.dao.Senha;
import br.integrado.jnpereira.nutrimix.modelo.Perfil;
import br.integrado.jnpereira.nutrimix.modelo.PerfilTela;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Jose Vinicius
 */
public class PerfilControl implements Initializable {

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdPerfil;
    @FXML
    TextField dsPerfil;

    @FXML
    ListView<Telas> lvTela;
    @FXML
    ListView<Telas> lvTelaAcesso;

    Perfil perfil;
    Dao dao = new Dao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdPerfil);
        FuncaoCampo.mascaraTexto(dsPerfil, 30);
        cdPerfil.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoPerfil();
            }
        });
    }

    public void iniciaTela() {
        carregaTelas();
    }

    private void validaCodigoPerfil() {
        if (!cdPerfil.getText().equals("") & perfil == null) {
            try {
                perfil = new Perfil();
                perfil.setCdPerfil(Integer.parseInt(cdPerfil.getText()));
                dao.get(perfil);

                dsPerfil.setText(perfil.getDsPerfil());

                cdPerfil.setEditable(false);
                carregaTelas();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                perfil = null;
                cdPerfil.requestFocus();
            }
        }
    }

    private void carregaTelas() {
        lvTela.getItems().clear();
        lvTelaAcesso.getItems().clear();
        Tela tela = new Tela();
        for (Field field : tela.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Senha.class)) {
                Telas telas = new Telas();

                try {
                    telas.cdTela = field.getName();
                    telas.dsTela = (String) Array.get((Object[]) field.get(new Object()), 1);
                } catch (Exception ex) {
                }
                if (isAcesso(telas.cdTela)) {
                    lvTelaAcesso.getItems().add(telas);
                } else {
                    lvTela.getItems().add(telas);
                }
            }
        }
    }

    private boolean isAcesso(String cdTela) {
        if (perfil == null) {
            return false;
        }
        try {
            PerfilTela telaPerfil = new PerfilTela();
            telaPerfil.setCdPerfil(perfil.getCdPerfil());
            telaPerfil.setCdTela(cdTela);
            dao.get(telaPerfil);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @FXML
    public void pesquisarPerfil() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Perfil(), "cdPerfil", "dsPerfil", null, "Lista de Perfil");
        if (valor != null) {
            cdPerfil.setText(valor);
            perfil = null;
            validaCodigoPerfil();
        }
    }

    @FXML
    public void addAcesso() {
        Telas tela = lvTela.getSelectionModel().getSelectedItem();
        if (tela == null) {
            Alerta.AlertaInfo("Aviso!", "Selecione um item.");
            return;
        }
        lvTela.getItems().remove(tela);
        lvTelaAcesso.getItems().add(tela);
    }

    @FXML
    public void remAcesso() {
        Telas tela = lvTelaAcesso.getSelectionModel().getSelectedItem();
        if (tela == null) {
            Alerta.AlertaInfo("Aviso!", "Selecione um item.");
            return;
        }
        lvTelaAcesso.getItems().remove(tela);
        lvTela.getItems().add(tela);
    }

    @FXML
    public void salvar() {
        if (dsPerfil.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo descrição do perfil é obrigatório.");
            return;
        }
        try {
            dao.autoCommit(false);
            if (perfil == null) {
                perfil = new Perfil();
                perfil.setDsPerfil(dsPerfil.getText());
                dao.save(perfil);
            } else {
                perfil.setDsPerfil(dsPerfil.getText());
                dao.update(perfil);
            }

            String where = "WHERE $cdPerfil$ = " + perfil.getCdPerfil();
            ArrayList<Object> arrayRemove = dao.getAllWhere(new PerfilTela(), where);
            for (Object obj : arrayRemove) {
                PerfilTela perfilTela = (PerfilTela) obj;
                dao.delete(perfilTela);
            }

            for (Telas telas : lvTelaAcesso.getItems()) {
                PerfilTela perfilTela = new PerfilTela();
                perfilTela.setCdPerfil(perfil.getCdPerfil());
                perfilTela.setCdTela(telas.cdTela);
                dao.save(perfilTela);
            }

            dao.commit();

            cdPerfil.setText(perfil.getCdPerfil().toString());
            perfil = null;
            validaCodigoPerfil();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao salvar produto.\n" + ex.toString());
            dao.rollback();
        }

        Alerta.AlertaInfo("Concluído", "Perfil salvo com sucesso!");

    }

    @FXML
    public void limpar() {
        perfil = null;
        FuncaoCampo.limparCampos(painel);
        cdPerfil.setEditable(true);
        cdPerfil.requestFocus();
        carregaTelas();
    }

    public class Telas {

        String cdTela;
        String dsTela;

        @Override
        public String toString() {
            return dsTela;
        }
    }
}
