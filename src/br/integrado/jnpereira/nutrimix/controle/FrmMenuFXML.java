/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.tools.Charts;
import br.integrado.jnpereira.nutrimix.tools.Relogio;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.ButtonAtend;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Jose Vinicius
 */
public class FrmMenuFXML implements Initializable {

    private Stage stage;
    public static int usuarioAtivo;

    @FXML
    private Tab tabDashboard;
    @FXML
    private AnchorPane paneChart;
    @FXML
    private AnchorPane paneAtend;
    @FXML
    private Label lblRelogio;
    @FXML
    private TabPane tabInicio;
    @FXML
    private MenuButton btnUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabInicio.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number oldValue, Number newValue) -> {
           if (newValue.intValue() == 0){
               //loadDashBoard();
           }else{
               //loadButtonAtend();
           }
        });
    }

    public void iniciaTela() {
        stage.setOnCloseRequest((WindowEvent t) -> {
            t.consume();
            stage.close();
            System.exit(0);
        });
        SingleSelectionModel<Tab> selectionModel = tabInicio.getSelectionModel();
        selectionModel.select(tabDashboard);
        Relogio rel = new Relogio(lblRelogio);
        rel.start();
        loadDashBoard();
        loadButtonAtend();
    }

    private void loadDashBoard() {
        Charts chart = new Charts();
        chart.setPaneChart(paneChart);
        chart.start();
    }

    private void loadButtonAtend() {
        ButtonAtend buttonAntend = new ButtonAtend();
        buttonAntend.setPaneAtend(paneAtend);
        buttonAntend.start();
    }

    @FXML
    public void abrirCadBanco(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_BANCO);
    }

    @FXML
    public void abrirCadGrupoProd(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_GRUPO_PRODUTO);
    }

    @FXML
    public void abrirCadTipoDespesa(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_TIPO_DESPESA);
    }

    @FXML
    public void abrirCadUnidPadrao(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_UNID_PADRAO);
    }

    @FXML
    public void abrirCadEstado(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_ESTADO);
    }

    @FXML
    public void abrirCadFormaPagto(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_FORMA_PAGTO);
    }

    @FXML
    public void abrirCadPrecoProd(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_PRECO_PROD);
    }

    @FXML
    public void abrirCadCidade(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_CIDADE);
    }

    @FXML
    public void abrirCadProduto(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_PRODUTO);
    }

    @FXML
    public void abrirCadCliente(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_CLIENTE);
    }

    @FXML
    public void abrirCadForne(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_FORNE);
    }

    @FXML
    public void abrirCadFunc(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_FUNC);
    }

    @FXML
    public void abrirCadAjustEstoq(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_AJUSTPROD);
    }

    @FXML
    public void abrirConMovtoEstoq(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CON_MOVTO_ESTOQ);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
