package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
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

public class CondicaoPagtoControl implements Initializable {

    @FXML
    private TextField codCondicaoPagto;
    @FXML
    private TextField dsCondicaoPagto;
    @FXML
    private TextField qtParcelas;
    @FXML
    private TextField nrIntervalo;
    @FXML
    private CheckBox inEntrada;
    @FXML
    private CheckBox inAtivo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<CondicaoPagtoHit> listCondicaoPagto = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYCondicaoPagto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codCondicaoPagto.setVisible(false);
        dsCondicaoPagto.setVisible(false);
        qtParcelas.setVisible(false);
        nrIntervalo.setVisible(false);
        inEntrada.setVisible(false);
        inAtivo.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listCondicaoPagto.iterator();
        for (int i = 0; it.hasNext(); i++) {
            CondicaoPagtoHit b = (CondicaoPagtoHit) it.next();
            try {
                if (b.codCondicaoPagto.getText().equals("")) {
                    if (!b.dsCondicaoPagto.getText().equals("")) {
                        CondicaoPagto forma = new CondicaoPagto();
                        forma.setDsCondicao(b.dsCondicaoPagto.getText());
                        forma.setDsCondicao(b.dsCondicaoPagto.getText());
                        forma.setQtParcelas(Integer.parseInt(b.qtParcelas.getText()));
                        forma.setNrIntervalo(Integer.parseInt(b.nrIntervalo.getText()));
                        forma.setInEntrada(b.inEntrada.isSelected());
                        forma.setInAtivo(b.inAtivo.isSelected());
                        dao.save(forma);
                    }
                } else {
                    if (!b.dsCondicaoPagto.getText().equals("")) {
                        if (b.isExcluir) {
                            /*long contador = dao.getCountWhere(new Despesa(), "WHERE $cdCondicaoPagto$ = " + Integer.parseInt(b.codCondicaoPagto.getText()));
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) despesa(s) com esse forma vinculado, código: " + b.codCondicaoPagto.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }*/
                            //CondicaoPagto forma = new CondicaoPagto();
                            //forma.setCdCondicaoPagto(Integer.parseInt(b.codCondicaoPagto.getText()));
                            //dao.delete(forma);
                        } else if (b.isAlterado) {
                            CondicaoPagto forma = new CondicaoPagto();
                            forma.setCdCondicao(Integer.parseInt(b.codCondicaoPagto.getText()));
                            forma.setDsCondicao(b.dsCondicaoPagto.getText());
                            forma.setQtParcelas(Integer.parseInt(b.qtParcelas.getText()));
                            forma.setNrIntervalo(Integer.parseInt(b.nrIntervalo.getText()));
                            forma.setInEntrada(b.inEntrada.isSelected());
                            forma.setInAtivo(b.inAtivo.isSelected());
                            dao.update(forma);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Condição de Pagamento.\n" + ex.toString());
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
        for (CondicaoPagtoHit b : listCondicaoPagto) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYCondicaoPagto = codCondicaoPagto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listCondicaoPagto.iterator();
        for (int i = 0; it.hasNext(); i++) {
            CondicaoPagtoHit b = (CondicaoPagtoHit) it.next();
            if (!b.isExcluir) {
                b.codCondicaoPagto.setEditable(codCondicaoPagto.isEditable());
                b.codCondicaoPagto.setPrefHeight(codCondicaoPagto.getHeight());
                b.codCondicaoPagto.setPrefWidth(codCondicaoPagto.getWidth());
                b.codCondicaoPagto.setLayoutX(codCondicaoPagto.getLayoutX());
                b.codCondicaoPagto.setLayoutY(LayoutYCondicaoPagto);
                b.codCondicaoPagto.getStyleClass().addAll(codCondicaoPagto.getStyleClass());
                b.dsCondicaoPagto.setEditable(dsCondicaoPagto.isEditable());
                b.dsCondicaoPagto.setPrefHeight(dsCondicaoPagto.getHeight());
                b.dsCondicaoPagto.setPrefWidth(dsCondicaoPagto.getWidth());
                b.dsCondicaoPagto.setLayoutX(dsCondicaoPagto.getLayoutX());
                b.dsCondicaoPagto.setLayoutY(LayoutYCondicaoPagto);
                b.dsCondicaoPagto.getStyleClass().addAll(dsCondicaoPagto.getStyleClass());
                b.qtParcelas.setEditable(qtParcelas.isEditable());
                b.qtParcelas.setPrefHeight(qtParcelas.getHeight());
                b.qtParcelas.setPrefWidth(qtParcelas.getWidth());
                b.qtParcelas.setLayoutX(qtParcelas.getLayoutX());
                b.qtParcelas.setLayoutY(LayoutYCondicaoPagto);
                b.qtParcelas.getStyleClass().addAll(qtParcelas.getStyleClass());
                b.nrIntervalo.setEditable(nrIntervalo.isEditable());
                b.nrIntervalo.setPrefHeight(nrIntervalo.getHeight());
                b.nrIntervalo.setPrefWidth(nrIntervalo.getWidth());
                b.nrIntervalo.setLayoutX(nrIntervalo.getLayoutX());
                b.nrIntervalo.setLayoutY(LayoutYCondicaoPagto);
                b.nrIntervalo.getStyleClass().addAll(nrIntervalo.getStyleClass());
                b.inEntrada.setPrefHeight(inEntrada.getHeight());
                b.inEntrada.setPrefWidth(inEntrada.getWidth());
                b.inEntrada.setLayoutX(inEntrada.getLayoutX());
                b.inEntrada.setLayoutY(LayoutYCondicaoPagto + 5);
                b.inEntrada.getStyleClass().addAll(inEntrada.getStyleClass());
                b.inAtivo.setPrefHeight(inAtivo.getHeight());
                b.inAtivo.setPrefWidth(inAtivo.getWidth());
                b.inAtivo.setLayoutX(inAtivo.getLayoutX());
                b.inAtivo.setLayoutY(LayoutYCondicaoPagto + 5);
                b.inAtivo.getStyleClass().addAll(inAtivo.getStyleClass());
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYCondicaoPagto);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYCondicaoPagto);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codCondicaoPagto);
                painel.getChildren().add(b.dsCondicaoPagto);
                painel.getChildren().add(b.qtParcelas);
                painel.getChildren().add(b.nrIntervalo);
                painel.getChildren().add(b.inEntrada);
                painel.getChildren().add(b.inAtivo);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYCondicaoPagto += (codCondicaoPagto.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYCondicaoPagto
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> formas = dao.getAllWhere(new CondicaoPagto(), "ORDER BY $cdCondicao$ ASC");
            listCondicaoPagto.clear();
            if (formas.isEmpty()) {
                TextField codCondicaoPagto = new TextField();
                codCondicaoPagto.setText("");
                TextField dsCondicaoPagto = new TextField();
                dsCondicaoPagto.setText("");
                TextField qtParcelas = new TextField();
                qtParcelas.setText("");
                TextField nrIntervalo = new TextField();
                nrIntervalo.setText("");
                CheckBox inEntrada = new CheckBox();
                CheckBox inAtivo = new CheckBox();
                inAtivo.setSelected(true);
                CondicaoPagtoHit formaHit = new CondicaoPagtoHit(codCondicaoPagto, dsCondicaoPagto, qtParcelas, nrIntervalo, inEntrada, inAtivo);
                listCondicaoPagto.add(formaHit);
            }
            for (Object obj : formas) {
                CondicaoPagto forma = (CondicaoPagto) obj;
                TextField codCondicaoPagto = new TextField();
                codCondicaoPagto.setText(forma.getCdCondicao().toString());
                TextField dsCondicaoPagto = new TextField();
                dsCondicaoPagto.setText(forma.getDsCondicao());
                TextField qtParcelas = new TextField();
                qtParcelas.setText(forma.getQtParcelas().toString());
                TextField nrIntervalo = new TextField();
                nrIntervalo.setText(forma.getNrIntervalo().toString());
                CheckBox inEntrada = new CheckBox();
                inEntrada.setSelected(forma.getInEntrada());
                CheckBox inAtivo = new CheckBox();
                inAtivo.setSelected(forma.getInAtivo());
                CondicaoPagtoHit formaHit = new CondicaoPagtoHit(codCondicaoPagto, dsCondicaoPagto, qtParcelas, nrIntervalo, inEntrada, inAtivo);
                listCondicaoPagto.add(formaHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(CondicaoPagtoHit forma, int posicao, int total) {
        FuncaoCampo.mascaraTexto(forma.dsCondicaoPagto, 45);
        /*forma.codCondicaoPagto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listCondicaoPagto.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    CondicaoPagtoHit b = (CondicaoPagtoHit) it.next();
                    if (b.codCondicaoPagto.getText().equals(forma.codCondicaoPagto.getText()) && i != posicao && !b.isExcluir && !b.codCondicaoPagto.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        forma.codCondicaoPagto.requestFocus();
                    }
                }
            }
        });*/
 /*forma.dsCondicaoPagto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        forma.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codCondicaoPagto = new TextField();
            codCondicaoPagto.setText("");
            TextField dsCondicaoPagto = new TextField();
            dsCondicaoPagto.setText("");
            TextField qtParcelas = new TextField();
            qtParcelas.setText("");
            TextField nrIntervalo = new TextField();
            nrIntervalo.setText("");
            CheckBox inEntrada = new CheckBox();
            CheckBox inAtivo = new CheckBox();
            inAtivo.setSelected(true);
            CondicaoPagtoHit b = new CondicaoPagtoHit(codCondicaoPagto, dsCondicaoPagto, qtParcelas, nrIntervalo, inEntrada, inAtivo);
            listCondicaoPagto.add(posicao + 1, b);
            atualizaLista();
        });

        forma.btnRem.setOnAction((ActionEvent event) -> {
            if (!forma.codCondicaoPagto.getText().equals("")) {
                Alerta.AlertaError("Não permitido!", "Não é possivel excluir um item salvo, apenas inativar.");
                return;
            }
            if (total == 1) {
                TextField codCondicaoPagto = new TextField();
                codCondicaoPagto.setText("");
                TextField dsCondicaoPagto = new TextField();
                dsCondicaoPagto.setText("");
                TextField qtParcelas = new TextField();
                qtParcelas.setText("");
                TextField nrIntervalo = new TextField();
                nrIntervalo.setText("");
                CheckBox inEntrada = new CheckBox();
                CheckBox inAtivo = new CheckBox();
                inAtivo.setSelected(true);
                CondicaoPagtoHit b = new CondicaoPagtoHit(codCondicaoPagto, dsCondicaoPagto, qtParcelas, nrIntervalo, inEntrada, inAtivo);
                listCondicaoPagto.add(b);
            }
            listCondicaoPagto.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class CondicaoPagtoHit {

        public TextField codCondicaoPagto;
        public TextField dsCondicaoPagto;
        public TextField qtParcelas;
        public TextField nrIntervalo;
        public CheckBox inEntrada;
        public CheckBox inAtivo;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public CondicaoPagtoHit(TextField codCondicaoPagto, TextField dsCondicaoPagto, TextField qtParcelas, TextField nrIntervalo, CheckBox inEntrada, CheckBox inAtivo) {
            this.codCondicaoPagto = codCondicaoPagto;
            this.dsCondicaoPagto = dsCondicaoPagto;
            this.qtParcelas = qtParcelas;
            this.nrIntervalo = nrIntervalo;
            this.inEntrada = inEntrada;
            this.inEntrada.setText("Entrada");
            this.inAtivo = inAtivo;
            this.inAtivo.setText("Ativo");
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsCondicaoPagto.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
            this.qtParcelas.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
            this.nrIntervalo.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
            /*this.inAtivo.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });*/
            this.inEntrada.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                this.isAlterado = true;
            });
            this.inAtivo.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                this.isAlterado = true;
            });
        }
    }

}
