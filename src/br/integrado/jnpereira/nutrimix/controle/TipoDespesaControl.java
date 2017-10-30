package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.TipoDespesa;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Despesa;
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

public class TipoDespesaControl implements Initializable {

    @FXML
    private TextField codTipoDespesa;
    @FXML
    private TextField dsTipoDespesa;
    @FXML
    private CheckBox cbxAtivo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<TipoDespesaHit> listTipoDespesa = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYTipoDespesa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codTipoDespesa.setVisible(false);
        dsTipoDespesa.setVisible(false);
        cbxAtivo.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listTipoDespesa.iterator();
        for (int i = 0; it.hasNext(); i++) {
            TipoDespesaHit b = (TipoDespesaHit) it.next();
            try {
                if (b.codTipoDespesa.getText().equals("")) {
                    if (!b.dsTipoDespesa.getText().equals("")) {
                        TipoDespesa tipo = new TipoDespesa();
                        tipo.setDsTipoDespesa(b.dsTipoDespesa.getText());
                        tipo.setInAtivo(true);
                        dao.save(tipo);
                    }
                } else {
                    if (!b.dsTipoDespesa.getText().equals("")) {
                        if (b.isExcluir) {
                            long contador = dao.getCountWhere(new Despesa(), "WHERE $cdTipoDespesa$ = " + Integer.parseInt(b.codTipoDespesa.getText()));
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) despesa(s) com esse tipo vinculado, código: " + b.codTipoDespesa.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }
                            TipoDespesa tipo = new TipoDespesa();
                            tipo.setCdTipoDespesa(Integer.parseInt(b.codTipoDespesa.getText()));
                            dao.delete(tipo);
                        } else if (b.isAlterado) {
                            TipoDespesa tipo = new TipoDespesa();
                            tipo.setCdTipoDespesa(Integer.parseInt(b.codTipoDespesa.getText()));
                            tipo.setDsTipoDespesa(b.dsTipoDespesa.getText());
                            tipo.setInAtivo(b.cbxAtivo.isSelected());
                            dao.update(tipo);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Tipo Despesa.\n" + ex.toString());
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
        for (TipoDespesaHit b : listTipoDespesa) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYTipoDespesa = codTipoDespesa.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listTipoDespesa.iterator();
        for (int i = 0; it.hasNext(); i++) {
            TipoDespesaHit b = (TipoDespesaHit) it.next();
            if (!b.isExcluir) {
                b.codTipoDespesa.setEditable(codTipoDespesa.isEditable());
                b.codTipoDespesa.setPrefHeight(codTipoDespesa.getHeight());
                b.codTipoDespesa.setPrefWidth(codTipoDespesa.getWidth());
                b.codTipoDespesa.setLayoutX(codTipoDespesa.getLayoutX());
                b.codTipoDespesa.setLayoutY(LayoutYTipoDespesa);
                b.dsTipoDespesa.setEditable(dsTipoDespesa.isEditable());
                b.dsTipoDespesa.setPrefHeight(dsTipoDespesa.getHeight());
                b.dsTipoDespesa.setPrefWidth(dsTipoDespesa.getWidth());
                b.dsTipoDespesa.setLayoutX(dsTipoDespesa.getLayoutX());
                b.dsTipoDespesa.setLayoutY(LayoutYTipoDespesa);
                b.cbxAtivo.setPrefHeight(cbxAtivo.getHeight());
                b.cbxAtivo.setPrefWidth(cbxAtivo.getWidth());
                b.cbxAtivo.setLayoutX(cbxAtivo.getLayoutX());
                b.cbxAtivo.setLayoutY(LayoutYTipoDespesa + 5);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYTipoDespesa);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYTipoDespesa);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codTipoDespesa);
                painel.getChildren().add(b.dsTipoDespesa);
                painel.getChildren().add(b.cbxAtivo);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYTipoDespesa += (codTipoDespesa.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYTipoDespesa
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> tipos = dao.getAllWhere(new TipoDespesa(), "ORDER BY $cdTipoDespesa$ ASC");
            listTipoDespesa.clear();
            if (tipos.isEmpty()) {
                TextField codTipoDespesa = new TextField();
                codTipoDespesa.setText("");
                TextField dsTipoDespesa = new TextField();
                dsTipoDespesa.setText("");
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(true);
                TipoDespesaHit tipoHit = new TipoDespesaHit(codTipoDespesa, dsTipoDespesa, cbxAtivo);
                listTipoDespesa.add(tipoHit);
            }
            for (Object obj : tipos) {
                TipoDespesa tipo = (TipoDespesa) obj;
                TextField codTipoDespesa = new TextField();
                codTipoDespesa.setText(String.valueOf(tipo.getCdTipoDespesa()));
                TextField dsTipoDespesa = new TextField();
                dsTipoDespesa.setText(tipo.getDsTipoDespesa());
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(tipo.getInAtivo());
                TipoDespesaHit tipoHit = new TipoDespesaHit(codTipoDespesa, dsTipoDespesa, cbxAtivo);
                listTipoDespesa.add(tipoHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(TipoDespesaHit tipo, int posicao, int total) {
        FuncaoCampo.mascaraTexto(tipo.dsTipoDespesa, 45);
        /*tipo.codTipoDespesa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listTipoDespesa.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    TipoDespesaHit b = (TipoDespesaHit) it.next();
                    if (b.codTipoDespesa.getText().equals(tipo.codTipoDespesa.getText()) && i != posicao && !b.isExcluir && !b.codTipoDespesa.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        tipo.codTipoDespesa.requestFocus();
                    }
                }
            }
        });*/
 /*tipo.dsTipoDespesa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        tipo.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codTipoDespesa = new TextField();
            codTipoDespesa.setText("");
            TextField dsTipoDespesa = new TextField();
            dsTipoDespesa.setText("");
            CheckBox cbxAtivo = new CheckBox();
            cbxAtivo.setSelected(true);
            TipoDespesaHit b = new TipoDespesaHit(codTipoDespesa, dsTipoDespesa, cbxAtivo);
            listTipoDespesa.add(posicao + 1, b);
            atualizaLista();
        });

        tipo.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                TextField codTipoDespesa = new TextField();
                codTipoDespesa.setText("");
                TextField dsTipoDespesa = new TextField();
                dsTipoDespesa.setText("");
                CheckBox cbxAtivo = new CheckBox();
                cbxAtivo.setSelected(true);
                TipoDespesaHit b = new TipoDespesaHit(codTipoDespesa, dsTipoDespesa, cbxAtivo);
                listTipoDespesa.add(b);
            }
            listTipoDespesa.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class TipoDespesaHit {

        public TextField codTipoDespesa;
        public TextField dsTipoDespesa;
        public CheckBox cbxAtivo;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public TipoDespesaHit(TextField codTipoDespesa, TextField dsTipoDespesa, CheckBox cbxAtivo) {
            this.codTipoDespesa = codTipoDespesa;
            this.dsTipoDespesa = dsTipoDespesa;
            this.cbxAtivo = cbxAtivo;
            this.cbxAtivo.setText("Ativo");
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsTipoDespesa.textProperty().addListener((obs, velho, novo) -> {
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
