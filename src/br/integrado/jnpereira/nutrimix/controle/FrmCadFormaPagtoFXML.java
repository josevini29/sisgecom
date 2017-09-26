package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.FormaPagto;
import br.integrado.jnpereira.nutrimix.dao.Dao;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FrmCadFormaPagtoFXML implements Initializable {

    @FXML
    private TextField codFormaPagto;
    @FXML
    private TextField dsFormaPagto;
    @FXML
    private CheckBox cbxAtivo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<FormaPagtoHit> listFormaPagto = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYFormaPagto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codFormaPagto.setVisible(false);
        dsFormaPagto.setVisible(false);
        cbxAtivo.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listFormaPagto.iterator();
        for (int i = 0; it.hasNext(); i++) {
            FormaPagtoHit b = (FormaPagtoHit) it.next();
            try {
                if (b.codFormaPagto.getText().equals("")) {
                    if (!b.dsFormaPagto.getText().equals("")) {
                        FormaPagto forma = new FormaPagto();
                        forma.setDsFormaPagto(b.dsFormaPagto.getText());
                        forma.setInAtivo(true);
                        dao.save(forma);
                    }
                } else {
                    if (!b.dsFormaPagto.getText().equals("")) {
                        if (b.isExcluir) {
                            /*long contador = dao.getCountWhere(new Despesa(), "WHERE $cdFormaPagto$ = " + Integer.parseInt(b.codFormaPagto.getText()));
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) despesa(s) com esse forma vinculado, código: " + b.codFormaPagto.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }*/
                            //FormaPagto forma = new FormaPagto();
                            //forma.setCdFormaPagto(Integer.parseInt(b.codFormaPagto.getText()));
                            //dao.delete(forma);
                        } else if (b.isAlterado) {
                            FormaPagto forma = new FormaPagto();
                            forma.setCdFormaPagto(Integer.parseInt(b.codFormaPagto.getText()));
                            forma.setDsFormaPagto(b.dsFormaPagto.getText());
                            forma.setInAtivo(b.cbxAtivo.isSelected());
                            dao.update(forma);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Tipo Forma de Pagamento.\n" + ex.toString());
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
        for (FormaPagtoHit b : listFormaPagto) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYFormaPagto = codFormaPagto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listFormaPagto.iterator();
        for (int i = 0; it.hasNext(); i++) {
            FormaPagtoHit b = (FormaPagtoHit) it.next();
            if (!b.isExcluir) {
                b.codFormaPagto.setEditable(codFormaPagto.isEditable());
                b.codFormaPagto.setPrefHeight(codFormaPagto.getHeight());
                b.codFormaPagto.setPrefWidth(codFormaPagto.getWidth());
                b.codFormaPagto.setLayoutX(codFormaPagto.getLayoutX());
                b.codFormaPagto.setLayoutY(LayoutYFormaPagto);
                b.codFormaPagto.getStyleClass().addAll(codFormaPagto.getStyleClass());
                b.dsFormaPagto.setEditable(dsFormaPagto.isEditable());
                b.dsFormaPagto.setPrefHeight(dsFormaPagto.getHeight());
                b.dsFormaPagto.setPrefWidth(dsFormaPagto.getWidth());
                b.dsFormaPagto.setLayoutX(dsFormaPagto.getLayoutX());
                b.dsFormaPagto.setLayoutY(LayoutYFormaPagto);
                b.dsFormaPagto.getStyleClass().addAll(dsFormaPagto.getStyleClass());
                b.cbxAtivo.setPrefHeight(cbxAtivo.getHeight());
                b.cbxAtivo.setPrefWidth(cbxAtivo.getWidth());
                b.cbxAtivo.setLayoutX(cbxAtivo.getLayoutX());
                b.cbxAtivo.setLayoutY(LayoutYFormaPagto + 5);
                b.cbxAtivo.getStyleClass().addAll(cbxAtivo.getStyleClass());
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYFormaPagto);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYFormaPagto);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codFormaPagto);
                painel.getChildren().add(b.dsFormaPagto);
                painel.getChildren().add(b.cbxAtivo);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYFormaPagto += (codFormaPagto.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYFormaPagto
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> formas = dao.getAllWhere(new FormaPagto(), "ORDER BY $cdFormaPagto$ ASC");
            listFormaPagto.clear();
            if (formas.isEmpty()) {
                TextField codFormaPagto = new TextField();
                codFormaPagto.setText("");
                TextField dsFormaPagto = new TextField();
                dsFormaPagto.setText("");
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(true);
                FormaPagtoHit formaHit = new FormaPagtoHit(codFormaPagto, dsFormaPagto, cbxAtivo);
                listFormaPagto.add(formaHit);
            }
            for (Object obj : formas) {
                FormaPagto forma = (FormaPagto) obj;
                TextField codFormaPagto = new TextField();
                codFormaPagto.setText(String.valueOf(forma.getCdFormaPagto()));
                TextField dsFormaPagto = new TextField();
                dsFormaPagto.setText(forma.getDsFormaPagto());
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(forma.getInAtivo());
                FormaPagtoHit formaHit = new FormaPagtoHit(codFormaPagto, dsFormaPagto, cbxAtivo);
                listFormaPagto.add(formaHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(FormaPagtoHit forma, int posicao, int total) {
        FuncaoCampo.mascaraTexto(forma.dsFormaPagto, 45);
        /*forma.codFormaPagto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listFormaPagto.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    FormaPagtoHit b = (FormaPagtoHit) it.next();
                    if (b.codFormaPagto.getText().equals(forma.codFormaPagto.getText()) && i != posicao && !b.isExcluir && !b.codFormaPagto.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        forma.codFormaPagto.requestFocus();
                    }
                }
            }
        });*/
 /*forma.dsFormaPagto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        forma.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codFormaPagto = new TextField();
            codFormaPagto.setText("");
            TextField dsFormaPagto = new TextField();
            dsFormaPagto.setText("");
            CheckBox cbxAtivo = new CheckBox();
            cbxAtivo.setSelected(true);
            FormaPagtoHit b = new FormaPagtoHit(codFormaPagto, dsFormaPagto, cbxAtivo);
            listFormaPagto.add(posicao + 1, b);
            atualizaLista();
        });

        forma.btnRem.setOnAction((ActionEvent event) -> {
            if (!forma.codFormaPagto.getText().equals("")) {
                Alerta.AlertaError("Não permitido!", "Não é possivel excluir um item salvo, apenas inativar.");
                return;
            }
            if (total == 1) {
                TextField codFormaPagto = new TextField();
                codFormaPagto.setText("");
                TextField dsFormaPagto = new TextField();
                dsFormaPagto.setText("");
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(true);
                FormaPagtoHit b = new FormaPagtoHit(codFormaPagto, dsFormaPagto, cbxAtivo);
                listFormaPagto.add(b);
            }
            listFormaPagto.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class FormaPagtoHit {

        public TextField codFormaPagto;
        public TextField dsFormaPagto;
        public CheckBox cbxAtivo;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public FormaPagtoHit(TextField codFormaPagto, TextField dsFormaPagto, CheckBox cbxAtivo) {
            this.codFormaPagto = codFormaPagto;
            this.dsFormaPagto = dsFormaPagto;
            this.cbxAtivo = cbxAtivo;
            this.cbxAtivo.setText("Ativo");
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsFormaPagto.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });

            this.cbxAtivo.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });

            this.cbxAtivo.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                this.isAlterado = true;
            });
        }
    }

}
