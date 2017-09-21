package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.Atendimento;
import br.integrado.jnpereira.nutrimix.modelo.AtendimentoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class FrmCadAtendimentoFXML implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdAtend;
    @FXML
    TextField dtAtend;
    @FXML
    TextField nrMesa;
    @FXML
    ChoiceBox tpSituacao;
    @FXML
    Label lblCadastro;

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdProduto;
    @FXML
    Button btnPesqProd;
    @FXML
    TextField dsProduto;
    @FXML
    TextField qtProduto;
    @FXML
    TextField qtPaga;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    @FXML
    Label lblCadProd;

    public Object param;
    double LayoutYAtend;
    ArrayList<AtendProdHit> listAtendProd = new ArrayList<>();
    Dao dao = new Dao();
    Atendimento atendimento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdAtend);
        FuncaoCampo.mascaraNumeroInteiro(nrMesa);
        FuncaoCampo.mascaraData(dtAtend);
        TrataCombo.setValueComboStAtendimento(tpSituacao, "1");
        tpSituacao.setDisable(true);
        cdAtend.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAtendimento();
            }
        });
        dtAtend.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAtendimento();
            }
        });
    }

    public void iniciaTela() {
        if (param != null) {
            Atendimento atend = (Atendimento) param;
            cdAtend.setText(atend.getCdAtend().toString());
            dtAtend.setText(Data.AmericaToBrasilSemHora(atend.getDtAtend()));
            validaCodigoAtendimento();
        }
    }

    private void validaCodigoAtendimento() {
        if (!cdAtend.getText().equals("") & !dtAtend.getText().equals("") & atendimento == null) {
            try {
                atendimento = new Atendimento();
                atendimento.setCdAtend(Integer.parseInt(cdAtend.getText()));
                atendimento.setDtAtend(Data.StringToDate(dtAtend.getText()));
                dao.get(atendimento);
                nrMesa.setText(atendimento.getNrMesa().toString());
                TrataCombo.setValueComboStAtendimento(tpSituacao, atendimento.getStAtend());
                tpSituacao.setDisable(false);
                lblCadastro.setText(Numero.getCadastro(atendimento.getCdUserCad(), atendimento.getDtCadastro()));
                cdAtend.setEditable(false);
                dtAtend.setEditable(false);
                atualizaAtendProd();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                atendimento = null;
                dtAtend.requestFocus();
            }
        }
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        atendimento = null;
        listAtendProd = new ArrayList<>();
        FuncaoCampo.limparCampos(anchor);
        atualizaAtendProd();
        lblCadastro.setText("");
        cdAtend.setEditable(true);
        dtAtend.setEditable(true);
        TrataCombo.setValueComboStAtendimento(tpSituacao, "1");
        tpSituacao.setDisable(true);
    }

    public void atualizaAtendProd() {
        try {
            ArrayList<Object> atends = new ArrayList<>();
            if (atendimento != null) {
                String where = "WHERE $cdAtend$ = " + atendimento.getCdAtend()
                        + " AND $dtAtend$ = '" + Data.BrasilToAmericaSemHora(atendimento.getDtAtend()) + "' ORDER BY $cdProduto$ ASC";
                atends = dao.getAllWhere(new AtendimentoProduto(), where);
                listAtendProd.clear();
            }
            if (atends.isEmpty()) {
                AtendProdHit atendHit = new AtendProdHit();
                listAtendProd.add(atendHit);
            }
            for (Object obj : atends) {
                AtendimentoProduto atendProd = (AtendimentoProduto) obj;
                AtendProdHit atendHit = new AtendProdHit();
                atendHit.cdProduto.setText(atendProd.getCdProduto().toString());
                Produto produto = new Produto();
                produto.setCdProduto(atendProd.getCdProduto());
                dao.get(produto);
                atendHit.dsProduto.setText(produto.getDsProduto());
                atendHit.qtProduto.setText(atendProd.getQtProduto().toString());
                atendHit.qtPaga.setText(atendProd.getQtPaga().toString());
                atendHit.lblCadProd.setText(Numero.getCadastro(atendProd.getCdUserCad(), atendProd.getDtCadastro()));
                atendHit.atendProd = atendProd;
                listAtendProd.add(atendHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void atualizaLista() {
        int total = 0;
        for (AtendProdHit b : listAtendProd) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYAtend = cdProduto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listAtendProd.iterator();
        for (int i = 0; it.hasNext(); i++) {
            AtendProdHit b = (AtendProdHit) it.next();
            if (!b.isExcluir) {
                b.cdProduto.setEditable(cdProduto.isEditable());
                if (b.atendProd != null) {
                    b.cdProduto.setEditable(false);
                }
                b.cdProduto.setPrefHeight(cdProduto.getHeight());
                b.cdProduto.setPrefWidth(cdProduto.getWidth());
                b.cdProduto.setLayoutX(cdProduto.getLayoutX());
                b.cdProduto.setLayoutY(LayoutYAtend);
                b.cdProduto.getStyleClass().addAll(this.cdProduto.getStyleClass());
                b.dsProduto.setEditable(dsProduto.isEditable());
                b.dsProduto.setPrefHeight(dsProduto.getHeight());
                b.dsProduto.setPrefWidth(dsProduto.getWidth());
                b.dsProduto.setLayoutX(dsProduto.getLayoutX());
                b.dsProduto.setLayoutY(LayoutYAtend);
                b.dsProduto.getStyleClass().addAll(this.dsProduto.getStyleClass());
                b.qtProduto.setEditable(qtProduto.isEditable());
                b.qtProduto.setPrefHeight(qtProduto.getHeight());
                b.qtProduto.setPrefWidth(qtProduto.getWidth());
                b.qtProduto.setLayoutX(qtProduto.getLayoutX());
                b.qtProduto.setLayoutY(LayoutYAtend);
                b.qtProduto.getStyleClass().addAll(this.qtProduto.getStyleClass());
                b.qtPaga.setEditable(qtPaga.isEditable());
                b.qtPaga.setPrefHeight(qtPaga.getHeight());
                b.qtPaga.setPrefWidth(qtPaga.getWidth());
                b.qtPaga.setLayoutX(qtPaga.getLayoutX());
                b.qtPaga.setLayoutY(LayoutYAtend);
                b.qtPaga.getStyleClass().addAll(this.qtPaga.getStyleClass());
                b.lblCadProd.setPrefHeight(lblCadProd.getHeight());
                b.lblCadProd.setPrefWidth(lblCadProd.getWidth());
                b.lblCadProd.setLayoutX(lblCadProd.getLayoutX());
                b.lblCadProd.setLayoutY(LayoutYAtend + 30);
                b.lblCadProd.getStyleClass().addAll(this.lblCadProd.getStyleClass());
                b.btnPesqProd.setPrefHeight(btnPesqProd.getHeight());
                b.btnPesqProd.setPrefWidth(btnPesqProd.getWidth());
                b.btnPesqProd.setLayoutX(btnPesqProd.getLayoutX());
                b.btnPesqProd.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnPesqProd, IconButtonHit.ICON_PESQUISA);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.cdProduto);
                painel.getChildren().add(b.btnPesqProd);
                painel.getChildren().add(b.dsProduto);
                painel.getChildren().add(b.qtProduto);
                painel.getChildren().add(b.qtPaga);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                painel.getChildren().add(b.lblCadProd);
                LayoutYAtend += (cdProduto.getHeight() + 17);
            }
            addValidacao(b, i, total);
        }
        painel.setPrefHeight(LayoutYAtend + 10);
    }

    public void addValidacao(AtendProdHit atendProdHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(atendProdHit.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(atendProdHit.qtProduto);

        atendProdHit.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codBanco = new TextField();
            codBanco.setText("");
            TextField dsBanco = new TextField();
            dsBanco.setText("");
            AtendProdHit b = new AtendProdHit();
            listAtendProd.add(posicao + 1, b);
            atualizaLista();
        });

        atendProdHit.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                AtendProdHit b = new AtendProdHit();
                listAtendProd.add(b);
            }
            listAtendProd.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class AtendProdHit {

        AtendimentoProduto atendProd;
        TextField cdProduto = new TextField();
        Button btnPesqProd = new Button();
        TextField dsProduto = new TextField();
        TextField qtProduto = new TextField();
        TextField qtPaga = new TextField();
        Button btnAdd = new Button();
        Button btnRem = new Button();
        Label lblCadProd = new Label();
        public boolean isExcluir = false;
        public boolean isAlterado = false;
    }

}
