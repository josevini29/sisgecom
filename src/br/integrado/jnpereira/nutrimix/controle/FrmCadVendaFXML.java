/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Atendimento;
import br.integrado.jnpereira.nutrimix.modelo.AtendimentoProduto;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompraProduto;
import br.integrado.jnpereira.nutrimix.modelo.Cliente;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class FrmCadVendaFXML implements Initializable {
    
    @FXML
    AnchorPane painel;
    @FXML
    TextField cdCliente;
    @FXML
    TextField dsCliente;
    @FXML
    TextField nrCpfCnpj;
    @FXML
    Label lblAtendimento;
    
    @FXML
    AnchorPane painelProd;
    @FXML
    TextField cdProduto;
    @FXML
    Button btnPesqProd;
    @FXML
    TextField dsProduto;
    @FXML
    TextField qtUnitario;
    @FXML
    TextField vlUnitario;
    @FXML
    TextField vlTotalProd;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    
    public Stage stage;
    public Object param;
    
    private final Dao dao = new Dao();
    private Pessoa pessoa;
    private Cliente cliente;
    private Atendimento atendimento;
    
    ArrayList<VendaProdHit> listVendaProd = new ArrayList<>();
    double LayoutY;
    boolean inAntiLoop = true;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdCliente);
        cdCliente.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCliente();
            }
        });
    }
    
    public void iniciaTela() {
        if (param != null) {
            atendimento = (Atendimento) param;
            lblAtendimento.setText("Atendimento: " + atendimento.getCdAtend() + " - Data: " + Data.AmericaToBrasil(atendimento.getDtAtend()));
        }
        atualizaVendaProd();
    }
    
    @FXML
    public void pesquisarCliente() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Cliente(), true);
        if (valor != null) {
            cdCliente.setText(valor);
            validaCodigoCliente();
        }
    }
    
    public void abrirListaProduto(VendaProdHit movto) {
        if (movto.atendProd != null) {
            Alerta.AlertaError("Não permitido!", "Altere o item no atendimento.");
            return;
        }
        
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            movto.cdProduto.setText(valor);
            validaCodigoProduto(movto);
        }
    }
    
    private void validaCodigoCliente() {
        if (!cdCliente.getText().equals("")) {
            boolean vInBusca = true;
            if (cliente != null) {
                if (cliente.getCdCliente() == Integer.parseInt(cdCliente.getText())) {
                    vInBusca = false;
                }
            }
            if (vInBusca) {
                try {
                    cliente = new Cliente();
                    cliente.setCdCliente(Integer.parseInt(cdCliente.getText()));
                    dao.get(cliente);
                    if (!cliente.getInAtivo()) {
                        Alerta.AlertaError("Cliente inválido!", "Cliente está inativo.");
                        cliente = null;
                        dsCliente.setText("");
                        nrCpfCnpj.setText("");
                        cdCliente.requestFocus();
                        return;
                    }
                    pessoa = new Pessoa();
                    pessoa.setCdPessoa(cliente.getCdPessoa());
                    dao.get(pessoa);
                    dsCliente.setText(pessoa.getDsPessoa());
                    if (pessoa.getTpPessoa().equals("F")) {
                        nrCpfCnpj.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    } else {
                        nrCpfCnpj.setText(Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()));
                    }
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    cliente = null;
                    pessoa = null;
                    dsCliente.setText("");
                    nrCpfCnpj.setText("");
                    cdCliente.requestFocus();
                }
            }
        } else {
            cliente = null;
            pessoa = null;
            dsCliente.setText("");
            nrCpfCnpj.setText("");
        }
    }
    
    private void validaCodigoProduto(VendaProdHit vendaHit) {
        if (!vendaHit.cdProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(vendaHit.cdProduto.getText()));
                dao.get(prod);
                
                if (inAntiLoop) {
                    inAntiLoop = false;
                    if (!prod.getInAtivo()) {
                        Alerta.AlertaError("Inválido", "Produto está inativo.");
                        vendaHit.cdProduto.requestFocus();
                        inAntiLoop = true;
                        return;
                    }
                    
                    for (VendaProdHit movtoHit : listVendaProd) {
                        if (movtoHit.cdProduto.getText().equals(vendaHit.cdProduto.getText())
                                && !movtoHit.equals(vendaHit)) {
                            Alerta.AlertaError("Inválido", "Produto já está na lista");
                            vendaHit.cdProduto.requestFocus();
                            inAntiLoop = true;
                            return;
                        }
                    }
                    inAntiLoop = true;
                }
                
                vendaHit.dsProduto.setText(prod.getDsProduto());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Produto não encontrado!");
                vendaHit.cdProduto.requestFocus();
            }
        } else {
            vendaHit.dsProduto.setText("");
        }
    }
    
    public void atualizaVendaProd() {
        try {
            ArrayList<Object> atendsProd = new ArrayList<>();
            if (atendimento != null) {
                String where = "WHERE $cdAtend$ = " + atendimento.getCdAtend()
                        + " AND $dtAtend$ = '" + Data.BrasilToAmericaSemHora(atendimento.getDtAtend()) + "' ORDER BY $cdProduto$ ASC";
                atendsProd = dao.getAllWhere(new AtendimentoProduto(), where);
                listVendaProd.clear();
            }
            if (atendsProd.isEmpty()) {
                VendaProdHit vendaHit = new VendaProdHit();
                listVendaProd.add(vendaHit);
            }
            for (Object obj : atendsProd) {
                AtendimentoProduto vendaProd = (AtendimentoProduto) obj;
                if (vendaProd.getQtProduto() != vendaProd.getQtPaga()) {
                    VendaProdHit vendaHit = new VendaProdHit();
                    vendaHit.cdProduto.setText(vendaProd.getCdProduto().toString());
                    Produto produto = new Produto();
                    produto.setCdProduto(vendaProd.getCdProduto());
                    dao.get(produto);
                    vendaHit.dsProduto.setText(produto.getDsProduto());
                    vendaHit.qtUnitario.setText(String.valueOf(vendaProd.getQtProduto() - vendaProd.getQtPaga()));
                    vendaHit.atendProd = vendaProd;
                    listVendaProd.add(vendaHit);
                }
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }
    
    public void atualizaLista() {
        LayoutY = cdProduto.getLayoutY();
        painelProd.getChildren().clear();
        Iterator it = listVendaProd.iterator();
        for (int i = 0; it.hasNext(); i++) {
            VendaProdHit b = (VendaProdHit) it.next();
            b.cdProduto.setEditable(cdProduto.isEditable());
            if (b.atendProd != null) {
                b.cdProduto.setEditable(false);
            }
            b.cdProduto.setPrefHeight(cdProduto.getHeight());
            b.cdProduto.setPrefWidth(cdProduto.getWidth());
            b.cdProduto.setLayoutX(cdProduto.getLayoutX());
            b.cdProduto.setLayoutY(LayoutY);
            b.cdProduto.getStyleClass().addAll(this.cdProduto.getStyleClass());
            b.dsProduto.setEditable(dsProduto.isEditable());
            b.dsProduto.setPrefHeight(dsProduto.getHeight());
            b.dsProduto.setPrefWidth(dsProduto.getWidth());
            b.dsProduto.setLayoutX(dsProduto.getLayoutX());
            b.dsProduto.setLayoutY(LayoutY);
            b.dsProduto.getStyleClass().addAll(this.dsProduto.getStyleClass());
            b.qtUnitario.setEditable(qtUnitario.isEditable());
            b.qtUnitario.setPrefHeight(qtUnitario.getHeight());
            b.qtUnitario.setPrefWidth(qtUnitario.getWidth());
            b.qtUnitario.setLayoutX(qtUnitario.getLayoutX());
            b.qtUnitario.setLayoutY(LayoutY);
            b.qtUnitario.getStyleClass().addAll(this.qtUnitario.getStyleClass());
            if (b.atendProd != null) {
                b.qtUnitario.setEditable(false);
                b.qtUnitario.getStyleClass().addAll("numero_estatico");
            }
            b.vlUnitario.setEditable(vlUnitario.isEditable());
            b.vlUnitario.setPrefHeight(vlUnitario.getHeight());
            b.vlUnitario.setPrefWidth(vlUnitario.getWidth());
            b.vlUnitario.setLayoutX(vlUnitario.getLayoutX());
            b.vlUnitario.setLayoutY(LayoutY);
            b.vlUnitario.getStyleClass().addAll(this.vlUnitario.getStyleClass());
            b.vlTotalProd.setEditable(vlTotalProd.isEditable());
            b.vlTotalProd.setPrefHeight(vlTotalProd.getHeight());
            b.vlTotalProd.setPrefWidth(vlTotalProd.getWidth());
            b.vlTotalProd.setLayoutX(vlTotalProd.getLayoutX());
            b.vlTotalProd.setLayoutY(LayoutY);
            b.vlTotalProd.getStyleClass().addAll(this.vlTotalProd.getStyleClass());
            b.btnPesqProd.setPrefHeight(btnPesqProd.getHeight());
            b.btnPesqProd.setPrefWidth(btnPesqProd.getWidth());
            b.btnPesqProd.setLayoutX(btnPesqProd.getLayoutX());
            b.btnPesqProd.setLayoutY(LayoutY);
            IconButtonHit.setIcon(b.btnPesqProd, IconButtonHit.ICON_PESQUISA);
            b.btnAdd.setPrefHeight(btnAdd.getHeight());
            b.btnAdd.setPrefWidth(btnAdd.getWidth());
            b.btnAdd.setLayoutX(btnAdd.getLayoutX());
            b.btnAdd.setLayoutY(LayoutY);
            IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
            b.btnRem.setPrefHeight(btnRem.getHeight());
            b.btnRem.setPrefWidth(btnRem.getWidth());
            b.btnRem.setLayoutX(btnRem.getLayoutX());
            b.btnRem.setLayoutY(LayoutY);
            IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
            painelProd.getChildren().add(b.cdProduto);
            painelProd.getChildren().add(b.btnPesqProd);
            painelProd.getChildren().add(b.dsProduto);
            painelProd.getChildren().add(b.qtUnitario);
            painelProd.getChildren().add(b.vlUnitario);
            painelProd.getChildren().add(b.vlTotalProd);
            painelProd.getChildren().add(b.btnAdd);
            painelProd.getChildren().add(b.btnRem);
            LayoutY += (cdProduto.getHeight() + 5);
            addValidacao(b, i, listVendaProd.size());
        }
        painelProd.setPrefHeight(LayoutY + 10);
    }
    
    public void addValidacao(VendaProdHit vendaProdHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(vendaProdHit.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(vendaProdHit.qtUnitario);
        
        vendaProdHit.cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto(vendaProdHit);
            }
        });
        
        vendaProdHit.btnPesqProd.setOnAction((ActionEvent event) -> {
            abrirListaProduto(vendaProdHit);
        });
        
        vendaProdHit.btnAdd.setOnAction((ActionEvent event) -> {
            VendaProdHit b = new VendaProdHit();
            listVendaProd.add(posicao + 1, b);
            atualizaLista();
        });
        
        vendaProdHit.btnRem.setOnAction((ActionEvent event) -> {
            
            if (total == 1) {
                VendaProdHit b = new VendaProdHit();
                listVendaProd.add(b);
            }
            
            listVendaProd.remove(vendaProdHit);
            atualizaLista();
        });
    }
    
    public class VendaProdHit {
        
        AtendimentoProduto atendProd;
        TextField cdProduto = new TextField();
        Button btnPesqProd = new Button();
        TextField dsProduto = new TextField();
        TextField qtUnitario = new TextField();
        TextField vlUnitario = new TextField();
        TextField vlTotalProd = new TextField();
        Button btnAdd = new Button();
        Button btnRem = new Button();
    }
    
}
