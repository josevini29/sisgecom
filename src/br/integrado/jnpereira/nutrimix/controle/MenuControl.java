/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Funcionario;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import br.integrado.jnpereira.nutrimix.relatorio.TelaRelatorio;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Charts;
import br.integrado.jnpereira.nutrimix.tools.Relogio;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.ButtonAtend;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
public class MenuControl implements Initializable {

    private Stage stage;
    private final Dao dao = new Dao();
    public static int usuarioAtivo;
    public static String version = "1.10";

    @FXML
    private Tab tabAtendimento;
    @FXML
    private AnchorPane paneChart;
    @FXML
    private AnchorPane paneAtend;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblRelogio;
    @FXML
    private TabPane tabInicio;
    @FXML
    private MenuButton btnUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void iniciaTela() {
        stage.setOnCloseRequest((WindowEvent t) -> {
            t.consume();
            stage.close();
            System.exit(0);
        });

        try {
            Usuario usuario = new Usuario();
            usuario.setCdUsuario(usuarioAtivo);
            dao.get(usuario);
            btnUser.setText(usuario.getDsLogin());
            if (usuario.getCdFuncionario() == null) {
                lblUsuario.setText("Seja bem vindo, " + usuario.getDsLogin().toUpperCase());
            } else {
                Funcionario func = new Funcionario();
                func.setCdFuncionario(usuario.getCdFuncionario());
                dao.get(func);
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(func.getCdPessoa());
                dao.get(pessoa);
                lblUsuario.setText("Seja bem vindo, " + pessoa.getDsPessoa());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao buscar usuário ao iniciar o sistema.");
            System.exit(0);
        }

        SingleSelectionModel<Tab> selectionModel = tabInicio.getSelectionModel();
        selectionModel.select(tabAtendimento);
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
        buttonAntend.setStage(stage);
        buttonAntend.setPaneAtend(paneAtend);
        buttonAntend.start();
    }

    @FXML
    public void atualizarButtonAtend() {
        loadButtonAtend();
    }

    @FXML
    public void abrirTelaAtendimento() {
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(getStage(), Tela.CAD_ATEND, null);
        loadButtonAtend();
    }

    @FXML
    public void abrirTelaVenda() {
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(getStage(), Tela.CAD_VENDA, null);
        loadButtonAtend();
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
    public void abrirCadCondicaoPagto(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_CONDICAO_PAGTO);
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

    @FXML
    public void abrirCadVenda(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(getStage(), Tela.CAD_VENDA, null);
    }

    @FXML
    public void abrirConVenda(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_VENDA);
    }

    @FXML
    public void abrirCadCompra(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComStage(stage, Tela.CAD_COMPRA);
    }
    
    @FXML
    public void abrirCadPedidoCompra(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(getStage(), Tela.CAD_PEDIDO_COMPRA, null);
    }
    
    @FXML
    public void abrirConCompra(ActionEvent event) {
        Tela tela = new Tela();
        //tela.abrirTelaModal(stage, Tela.CON_COMPRA);
    }

    @FXML
    public void abrirCadAltSenha(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComStage(stage, Tela.CAD_ALTSENHA);
    }

    @FXML
    public void abrirCadUsuario(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_USUARIO);
    }

    @FXML
    public void abrirCadPerfil(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModal(stage, Tela.CAD_PERFIL);
    }

    @FXML
    public void abrirConContasReceber(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComStage(stage, Tela.CON_CONTA_RECEBER);
    }

    @FXML
    public void abrirConContasPagar(ActionEvent event) {
        Tela tela = new Tela();
        tela.abrirTelaModalComStage(stage, Tela.CON_CONTA_PAGAR);
    }

    @FXML
    public void abrirRelEstoqProd(ActionEvent event) {
        TelaRelatorio tela = new TelaRelatorio();
        tela.abrirRelatorio(stage, tela.telaEstoqueProduto());
    }

    @FXML
    public void logoff(ActionEvent event) throws IOException {
        Tela tela = new Tela();
        Stage stageLogin = new Stage();
        tela.abrirLogin(stageLogin);
        stageLogin.setOnCloseRequest((WindowEvent t) -> {
            t.consume();
            stageLogin.close();
            System.exit(0);
        });
        stage.close();
    }

    @FXML
    public void sair(ActionEvent event) {
        System.exit(0);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
