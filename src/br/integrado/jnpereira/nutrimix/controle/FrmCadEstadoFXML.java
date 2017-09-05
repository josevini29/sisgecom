package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.Estado;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Cidade;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FrmCadEstadoFXML implements Initializable {

    @FXML
    private TextField codEstado;
    @FXML
    private TextField dsEstado;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<EstadoHit> listEstado = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codEstado.setVisible(false);
        dsEstado.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listEstado.iterator();
        for (int i = 0; it.hasNext(); i++) {
            EstadoHit b = (EstadoHit) it.next();
            b.codEstado.setText(b.codEstado.getText().replace(" ", ""));
            try {
                if (b.isNovo) {
                    if (!b.codEstado.getText().equals("") && !b.dsEstado.getText().equals("")) {
                        Estado estado = new Estado();
                        estado.setCdEstado(b.codEstado.getText().toUpperCase());
                        estado.setDsEstado(b.dsEstado.getText());
                        dao.save(estado);
                    }
                } else {
                    if (!b.codEstado.getText().equals("") && !b.dsEstado.getText().equals("")) {
                        if (b.isExcluir) {
                            long contador = dao.getCountWhere(new Cidade(), "WHERE $cdEstado$ = '" + b.codEstado.getText() + "'");
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) cidade(s) com esse estado vinculado, estado: " + b.codEstado.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }
                            Estado estado = new Estado();
                            estado.setCdEstado(b.codEstado.getText());
                            dao.delete(estado);
                        } else if (b.isAlterado) {
                            Estado estado = new Estado();
                            estado.setCdEstado(b.codEstado.getText());
                            estado.setDsEstado(b.dsEstado.getText());
                            dao.update(estado);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Estado.\n" + ex.toString());
                return;
            }
        }
        dao.commit();
        Alerta.AlertaInfo("Concluído", "Registros salvos com sucesso!");
        iniciaTela();
    }

    @FXML
    public void cancelar() {
        iniciaTela();
    }

    public void atualizaLista() {
        int total = 0;
        for (EstadoHit b : listEstado) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYEstado = codEstado.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listEstado.iterator();
        for (int i = 0; it.hasNext(); i++) {
            EstadoHit b = (EstadoHit) it.next();
            if (!b.isExcluir) {
                b.codEstado.setEditable(codEstado.isEditable());
                if (b.isNovo) {
                    b.codEstado.setEditable(true);
                }
                b.codEstado.setPrefHeight(codEstado.getHeight());
                b.codEstado.setPrefWidth(codEstado.getWidth());
                b.codEstado.setLayoutX(codEstado.getLayoutX());
                b.codEstado.setLayoutY(LayoutYEstado);
                b.dsEstado.setEditable(dsEstado.isEditable());
                b.dsEstado.setPrefHeight(dsEstado.getHeight());
                b.dsEstado.setPrefWidth(dsEstado.getWidth());
                b.dsEstado.setLayoutX(dsEstado.getLayoutX());
                b.dsEstado.setLayoutY(LayoutYEstado);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYEstado);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYEstado);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codEstado);
                painel.getChildren().add(b.dsEstado);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYEstado += (codEstado.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYEstado
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> estados = dao.getAllWhere(new Estado(), null);
            listEstado.clear();
            if (estados.isEmpty()) {
                TextField codEstado = new TextField();
                codEstado.setText("");
                TextField dsEstado = new TextField();
                dsEstado.setText("");
                EstadoHit estadoHit = new EstadoHit(codEstado, dsEstado);
                estadoHit.isNovo = true;
                listEstado.add(estadoHit);
            }
            for (Object obj : estados) {
                Estado estado = (Estado) obj;
                TextField codEstado = new TextField();
                codEstado.setText(String.valueOf(estado.getCdEstado()));
                TextField dsEstado = new TextField();
                dsEstado.setText(estado.getDsEstado());
                EstadoHit estadoHit = new EstadoHit(codEstado, dsEstado);
                listEstado.add(estadoHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(EstadoHit estado, int posicao, int total) {
        FuncaoCampo.mascaraTexto(estado.codEstado, 2);
        FuncaoCampo.mascaraTexto(estado.dsEstado, 50);
        estado.codEstado.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listEstado.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    EstadoHit b = (EstadoHit) it.next();
                    if (b.codEstado.getText().replace(" ", "").equalsIgnoreCase(estado.codEstado.getText().replace(" ", "")) && i != posicao && !b.codEstado.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        estado.codEstado.requestFocus();
                    }
                }
            }
        });
        /*estado.dsEstado.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        estado.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codEstado = new TextField();
            codEstado.setText("");
            TextField dsEstado = new TextField();
            dsEstado.setText("");
            EstadoHit b = new EstadoHit(codEstado, dsEstado);
            b.isNovo = true;
            listEstado.add(posicao + 1, b);
            atualizaLista();
        });

        estado.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                TextField codEstado = new TextField();
                codEstado.setText("");
                TextField dsEstado = new TextField();
                dsEstado.setText("");
                EstadoHit b = new EstadoHit(codEstado, dsEstado);
                listEstado.add(b);
            }
            listEstado.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class EstadoHit {

        public TextField codEstado;
        public TextField dsEstado;
        public Button btnAdd;
        public Button btnRem;
        public boolean isNovo = false;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public EstadoHit(TextField codEstado, TextField dsEstado) {
            this.codEstado = codEstado;
            this.dsEstado = dsEstado;
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsEstado.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
        }
    }

}
