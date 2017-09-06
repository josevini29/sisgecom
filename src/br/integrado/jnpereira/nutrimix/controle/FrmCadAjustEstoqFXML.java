package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.modelo.AjusteEstoque;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FrmCadAjustEstoqFXML implements Initializable {

    @FXML
    TextField cdAjuste;
    @FXML
    TextField dtAjuste;
    @FXML
    ChoiceBox tpAjuste;
    @FXML
    TextArea dsObs;
    @FXML
    Label lblCadastro;

    @FXML
    TextField cdProduto;
    @FXML
    Button btnPesqProd;
    @FXML
    TextField dsProduto;
    @FXML
    TextField qtEstoque;
    @FXML
    TextField qtAjuste;
    @FXML
    TextField vlItem;
    @FXML
    TextField vlTotalItem;
    @FXML
    CheckBox inCancelado;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    @FXML
    AnchorPane painelAjustes;
    double LayoutYAjustes;

    Dao dao = new Dao();
    AjusteEstoque ajuste;
    ArrayList<AjusteMovtoHit> listMovto = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdAjuste);
        dtAjuste.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        TrataCombo.setValueComboTpAjustEstoq(tpAjuste, null);
        FuncaoCampo.mascaraTexto(dsObs, 150);
        cdAjuste.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAjuste();
            }
        });
    }

    public void iniciaTela() {
        atualizaMovtoHit();
    }

    private void validaCodigoAjuste() {
        if (!cdAjuste.getText().equals("") & ajuste == null) {
            try {
                ajuste = new AjusteEstoque();
                ajuste.setCdAjuste(Integer.parseInt(cdAjuste.getText()));
                dao.get(ajuste);
                dtAjuste.setText(Data.AmericaToBrasilSemHora(ajuste.getDtAjuste()));
                TrataCombo.setValueComboTpAjustEstoq(tpAjuste, ajuste.getTpAjuste().toString());
                dsObs.setText(ajuste.getDsObs());
                lblCadastro.setText(Numero.getCadastro(ajuste.getCdUserCad(), ajuste.getDtCadastro()));
                cdAjuste.setEditable(false);
                atualizaMovtoHit();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                ajuste = null;
                cdAjuste.requestFocus();
                return;
            }
        }
    }

    //Inicio codigo da lista de movimentos de produto
    public void atualizaMovtoHit() {
        try {
            ArrayList<Object> movAjustes = new ArrayList<>();
            if (ajuste != null) {
                movAjustes = dao.getAllWhere(new MovtoEstoque(), "WHERE $cdAjuste$ = " + ajuste.getCdAjuste() + " ORDER BY $cdEstqMovto$ ASC");
            }
            listMovto.clear();
            if (movAjustes.isEmpty()) {
                listMovto.add(getAjusteNovo());
            }
            for (Object obj : movAjustes) {
                MovtoEstoque movEstoq = (MovtoEstoque) obj;
                TextField cdProduto = new TextField();
                TextField dsProduto = new TextField();
                TextField qtEstoque = new TextField();
                TextField qtAjuste = new TextField();
                TextField vlItem = new TextField();
                TextField vlTotalItem = new TextField();
                cdProduto.setText(movEstoq.getCdProduto().toString());
                Produto prod = new Produto();
                prod.setCdProduto(movEstoq.getCdProduto());
                dao.get(prod);
                dsProduto.setText(prod.getDsProduto());
                qtEstoque.setText(movEstoq.getQtEstoque().toString());
                qtAjuste.setText(movEstoq.getQtMovto().toString());
                vlItem.setText(movEstoq.getVlItem().toString());
                vlTotalItem.setText(String.valueOf(movEstoq.getQtMovto() * movEstoq.getVlItem()));
                inCancelado.setSelected(movEstoq.getInCancelado());

                AjusteMovtoHit ajusteHit = new AjusteMovtoHit();
                ajusteHit.cdEstoqMovto = movEstoq.getCdEstqMovto();
                ajusteHit.cdAjuste = movEstoq.getCdAjuste();
                ajusteHit.cdProduto = cdProduto;
                ajusteHit.dsProduto = dsProduto;
                ajusteHit.qtEstoque = qtEstoque;
                ajusteHit.qtAjuste = qtAjuste;
                ajusteHit.vlItem = vlItem;
                ajusteHit.vlTotalItem = vlTotalItem;
                ajusteHit.inCancelado.setSelected(movEstoq.getInCancelado());
                ajusteHit.inCancelado.setDisable(ajusteHit.inCancelado.isSelected());

                listMovto.add(ajusteHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaListaAjustes();
    }

    public void atualizaListaAjustes() {
        int total = listMovto.size();

        LayoutYAjustes = this.cdProduto.getLayoutY();
        painelAjustes.getChildren().clear();
        Iterator it = listMovto.iterator();
        for (int i = 0; it.hasNext(); i++) {
            AjusteMovtoHit b = (AjusteMovtoHit) it.next();
            b.cdProduto.setPrefHeight(this.cdProduto.getHeight());
            b.cdProduto.setPrefWidth(this.cdProduto.getWidth());
            b.cdProduto.setLayoutX(this.cdProduto.getLayoutX());
            b.cdProduto.setLayoutY(LayoutYAjustes);
            b.cdProduto.getStyleClass().addAll(this.cdProduto.getStyleClass());
            b.dsProduto.setPrefHeight(this.dsProduto.getHeight());
            b.dsProduto.setPrefWidth(this.dsProduto.getWidth());
            b.dsProduto.setLayoutX(this.dsProduto.getLayoutX());
            b.dsProduto.setEditable(this.dsProduto.isEditable());
            b.dsProduto.setLayoutY(LayoutYAjustes);
            b.qtEstoque.setPrefHeight(this.qtEstoque.getHeight());
            b.qtEstoque.setPrefWidth(this.qtEstoque.getWidth());
            b.qtEstoque.setLayoutX(this.qtEstoque.getLayoutX());
            b.qtEstoque.setEditable(this.qtEstoque.isEditable());
            b.qtEstoque.setLayoutY(LayoutYAjustes);
            b.qtAjuste.setPrefHeight(this.qtAjuste.getHeight());
            b.qtAjuste.setPrefWidth(this.qtAjuste.getWidth());
            b.qtAjuste.setLayoutX(this.qtAjuste.getLayoutX());
            b.qtAjuste.setEditable(this.qtAjuste.isEditable());
            b.qtAjuste.setLayoutY(LayoutYAjustes);
            b.vlItem.setPrefHeight(this.vlItem.getHeight());
            b.vlItem.setPrefWidth(this.vlItem.getWidth());
            b.vlItem.setLayoutX(this.vlItem.getLayoutX());
            b.vlItem.setEditable(this.vlItem.isEditable());
            b.vlItem.setLayoutY(LayoutYAjustes);
            b.vlTotalItem.setPrefHeight(this.vlTotalItem.getHeight());
            b.vlTotalItem.setPrefWidth(this.vlTotalItem.getWidth());
            b.vlTotalItem.setLayoutX(this.vlTotalItem.getLayoutX());
            b.vlTotalItem.setEditable(this.vlTotalItem.isEditable());
            b.vlTotalItem.setLayoutY(LayoutYAjustes);
            b.inCancelado.setPrefHeight(this.inCancelado.getHeight());
            b.inCancelado.setPrefWidth(this.inCancelado.getWidth());
            b.inCancelado.setLayoutX(this.inCancelado.getLayoutX());
            b.inCancelado.setLayoutY(LayoutYAjustes + 5);
            b.btnPesqProd.setPrefHeight(btnPesqProd.getHeight());
            b.btnPesqProd.setPrefWidth(btnPesqProd.getWidth());
            b.btnPesqProd.setLayoutX(btnPesqProd.getLayoutX());
            b.btnPesqProd.setLayoutY(LayoutYAjustes);
            IconButtonHit.setIcon(b.btnPesqProd, IconButtonHit.ICON_PESQUISA);
            b.btnAdd.setPrefHeight(btnAdd.getHeight());
            b.btnAdd.setPrefWidth(btnAdd.getWidth());
            b.btnAdd.setLayoutX(btnAdd.getLayoutX());
            b.btnAdd.setLayoutY(LayoutYAjustes);
            IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
            b.btnRem.setPrefHeight(btnRem.getHeight());
            b.btnRem.setPrefWidth(btnRem.getWidth());
            b.btnRem.setLayoutX(btnRem.getLayoutX());
            b.btnRem.setLayoutY(LayoutYAjustes);
            IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
            painelAjustes.getChildren().add(b.cdProduto);
            painelAjustes.getChildren().add(b.btnPesqProd);
            painelAjustes.getChildren().add(b.dsProduto);
            painelAjustes.getChildren().add(b.qtEstoque);
            painelAjustes.getChildren().add(b.qtAjuste);
            painelAjustes.getChildren().add(b.vlItem);
            painelAjustes.getChildren().add(b.vlTotalItem);
            painelAjustes.getChildren().add(b.inCancelado);
            painelAjustes.getChildren().add(b.btnAdd);
            painelAjustes.getChildren().add(b.btnRem);
            LayoutYAjustes += (this.cdProduto.getHeight() + 5);
            addValidacaoTelefone(b, i, total);
        }

        painelAjustes.setPrefHeight(LayoutYAjustes
                + 10);
    }

    public void addValidacaoTelefone(AjusteMovtoHit ajusteMovto, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(ajusteMovto.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(ajusteMovto.qtAjuste);

        ajusteMovto.btnAdd.setOnAction((ActionEvent event) -> {
            if (listMovto.get(posicao).cdAjuste != null) {
                Alerta.AlertaError("Não autorizado!", "Não é possivel adicionar mais produtos, apenas cancela-los.");
                return;
            }
            listMovto.add(posicao + 1, getAjusteNovo());
            atualizaListaAjustes();
        });

        ajusteMovto.btnRem.setOnAction((ActionEvent event) -> {
            if (listMovto.get(posicao).cdAjuste != null) {
                Alerta.AlertaError("Não autorizado!", "Não é possivel remover um produto já salvo, apenas cancela-lo.");
                return;
            }
            listMovto.remove(ajusteMovto);
            if (total == 1) {
                listMovto.add(getAjusteNovo());
            }
            atualizaListaAjustes();
        });

    }

    private AjusteMovtoHit getAjusteNovo() {
        TextField cdProduto = new TextField();
        TextField dsProduto = new TextField();
        TextField qtEstoque = new TextField();
        TextField qtAjuste = new TextField();
        TextField vlItem = new TextField();
        TextField vlTotalItem = new TextField();
        cdProduto.setText("");
        dsProduto.setText("");
        qtEstoque.setText("");
        qtAjuste.setText("");
        vlItem.setText("");
        vlTotalItem.setText("");

        AjusteMovtoHit ajusteHit = new AjusteMovtoHit();
        ajusteHit.cdProduto = cdProduto;
        ajusteHit.dsProduto = dsProduto;
        ajusteHit.qtEstoque = qtEstoque;
        ajusteHit.qtAjuste = qtAjuste;
        ajusteHit.vlItem = vlItem;
        ajusteHit.vlTotalItem = vlTotalItem;
        return ajusteHit;
    }

    public class AjusteMovtoHit {

        public Integer cdEstoqMovto;
        public Integer cdAjuste;
        public TextField cdProduto;
        public Button btnPesqProd;
        public TextField dsProduto;
        public TextField qtEstoque;
        public TextField qtAjuste;
        public TextField vlItem;
        public TextField vlTotalItem;
        public CheckBox inCancelado;
        public Button btnAdd;
        public Button btnRem;

        public AjusteMovtoHit() {
            this.btnPesqProd = new Button();
            this.btnAdd = new Button();
            this.btnRem = new Button();
            this.inCancelado = new CheckBox();
            this.inCancelado.setText("Cancelado");
            this.inCancelado.setSelected(false);
            this.inCancelado.setDisable(true);
        }
    }

}
