package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.UnidadePadrao;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
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

public class FrmCadUnidPadraoFXML implements Initializable {

    @FXML
    private TextField codUnidadePadrao;
    @FXML
    private TextField dsUnidadePadrao;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<UnidadePadraoHit> listUnidadePadrao = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYUnidadePadrao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codUnidadePadrao.setVisible(false);
        dsUnidadePadrao.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listUnidadePadrao.iterator();
        for (int i = 0; it.hasNext(); i++) {
            UnidadePadraoHit b = (UnidadePadraoHit) it.next();
            b.codUnidadePadrao.setText(b.codUnidadePadrao.getText().replace(" ", ""));
            try {
                if (b.isNovo) {
                    if (!b.codUnidadePadrao.getText().equals("") && !b.dsUnidadePadrao.getText().equals("")) {
                        UnidadePadrao unidade = new UnidadePadrao();
                        unidade.setCdUnidadePadrao(b.codUnidadePadrao.getText().toUpperCase());
                        unidade.setDsUnidadePadrao(b.dsUnidadePadrao.getText());
                        dao.save(unidade);
                    }
                } else {
                    if (!b.codUnidadePadrao.getText().equals("") && !b.dsUnidadePadrao.getText().equals("")) {
                        if (b.isExcluir) {
                            long contador = dao.getCountWhere(new Produto(), "WHERE $cdUndPadrao$ = '" + b.codUnidadePadrao.getText() + "' OR " + "$cdUndPadraoCompra$ = '" + b.codUnidadePadrao.getText() + "'");
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) produto(s) com essa unidade vinculado, unidade: " + b.codUnidadePadrao.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }
                            UnidadePadrao unidade = new UnidadePadrao();
                            unidade.setCdUnidadePadrao(b.codUnidadePadrao.getText());
                            dao.delete(unidade);
                        } else if (b.isAlterado) {
                            UnidadePadrao unidade = new UnidadePadrao();
                            unidade.setCdUnidadePadrao(b.codUnidadePadrao.getText());
                            unidade.setDsUnidadePadrao(b.dsUnidadePadrao.getText());
                            dao.update(unidade);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar UnidadePadrao.\n" + ex.toString());
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
        for (UnidadePadraoHit b : listUnidadePadrao) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYUnidadePadrao = codUnidadePadrao.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listUnidadePadrao.iterator();
        for (int i = 0; it.hasNext(); i++) {
            UnidadePadraoHit b = (UnidadePadraoHit) it.next();
            if (!b.isExcluir) {
                b.codUnidadePadrao.setEditable(codUnidadePadrao.isEditable());
                if (b.isNovo) {
                    b.codUnidadePadrao.setEditable(true);
                }
                b.codUnidadePadrao.setPrefHeight(codUnidadePadrao.getHeight());
                b.codUnidadePadrao.setPrefWidth(codUnidadePadrao.getWidth());
                b.codUnidadePadrao.setLayoutX(codUnidadePadrao.getLayoutX());
                b.codUnidadePadrao.setLayoutY(LayoutYUnidadePadrao);
                b.dsUnidadePadrao.setEditable(dsUnidadePadrao.isEditable());
                b.dsUnidadePadrao.setPrefHeight(dsUnidadePadrao.getHeight());
                b.dsUnidadePadrao.setPrefWidth(dsUnidadePadrao.getWidth());
                b.dsUnidadePadrao.setLayoutX(dsUnidadePadrao.getLayoutX());
                b.dsUnidadePadrao.setLayoutY(LayoutYUnidadePadrao);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYUnidadePadrao);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYUnidadePadrao);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codUnidadePadrao);
                painel.getChildren().add(b.dsUnidadePadrao);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYUnidadePadrao += (codUnidadePadrao.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYUnidadePadrao
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> unidades = dao.getAllWhere(new UnidadePadrao(), null);
            listUnidadePadrao.clear();
            if (unidades.isEmpty()) {
                TextField codUnidadePadrao = new TextField();
                codUnidadePadrao.setText("");
                TextField dsUnidadePadrao = new TextField();
                dsUnidadePadrao.setText("");
                UnidadePadraoHit unidadeHit = new UnidadePadraoHit(codUnidadePadrao, dsUnidadePadrao);
                unidadeHit.isNovo = true;
                listUnidadePadrao.add(unidadeHit);
            }
            for (Object obj : unidades) {
                UnidadePadrao unidade = (UnidadePadrao) obj;
                TextField codUnidadePadrao = new TextField();
                codUnidadePadrao.setText(String.valueOf(unidade.getCdUnidadePadrao()));
                TextField dsUnidadePadrao = new TextField();
                dsUnidadePadrao.setText(unidade.getDsUnidadePadrao());
                UnidadePadraoHit unidadeHit = new UnidadePadraoHit(codUnidadePadrao, dsUnidadePadrao);
                listUnidadePadrao.add(unidadeHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(UnidadePadraoHit unidade, int posicao, int total) {
        FuncaoCampo.mascaraTexto(unidade.codUnidadePadrao, 10);
        FuncaoCampo.mascaraTexto(unidade.dsUnidadePadrao, 45);
        unidade.codUnidadePadrao.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listUnidadePadrao.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    UnidadePadraoHit b = (UnidadePadraoHit) it.next();
                    if (b.codUnidadePadrao.getText().replace(" ", "").equalsIgnoreCase(unidade.codUnidadePadrao.getText().replace(" ", "")) && i != posicao && !b.codUnidadePadrao.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        unidade.codUnidadePadrao.requestFocus();
                    }
                }
            }
        });
        /*unidade.dsUnidadePadrao.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        unidade.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codUnidadePadrao = new TextField();
            codUnidadePadrao.setText("");
            TextField dsUnidadePadrao = new TextField();
            dsUnidadePadrao.setText("");
            UnidadePadraoHit b = new UnidadePadraoHit(codUnidadePadrao, dsUnidadePadrao);
            b.isNovo = true;
            listUnidadePadrao.add(posicao + 1, b);
            atualizaLista();
        });

        unidade.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                TextField codUnidadePadrao = new TextField();
                codUnidadePadrao.setText("");
                TextField dsUnidadePadrao = new TextField();
                dsUnidadePadrao.setText("");
                UnidadePadraoHit b = new UnidadePadraoHit(codUnidadePadrao, dsUnidadePadrao);
                listUnidadePadrao.add(b);
            }
            listUnidadePadrao.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class UnidadePadraoHit {

        public TextField codUnidadePadrao;
        public TextField dsUnidadePadrao;
        public Button btnAdd;
        public Button btnRem;
        public boolean isNovo = false;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public UnidadePadraoHit(TextField codUnidadePadrao, TextField dsUnidadePadrao) {
            this.codUnidadePadrao = codUnidadePadrao;
            this.dsUnidadePadrao = dsUnidadePadrao;
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsUnidadePadrao.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
        }
    }

}
