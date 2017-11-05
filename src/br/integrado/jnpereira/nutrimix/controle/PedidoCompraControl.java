package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.modelo.PedidoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PedidoCompraControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdPedido;
    @FXML
    ChoiceBox stPedido;
    @FXML
    TextField cdForne;
    @FXML
    TextField dsForne;
    @FXML
    TextField nrCpfCnpj;
    @FXML
    TextField vlTotalPedido;
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
    TextField vlProduto;
    @FXML
    TextField dtPrevEntrega;
    @FXML
    TextField qtEntregue;
    @FXML
    TextField vlTotalProd;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;

    public Stage stage;
    public Object param;
    double LayoutY;
    ArrayList<PedidoProdHit> listPedidoProd = new ArrayList<>();
    Dao dao = new Dao();
    Pedido pedido;
    Fornecedor fornecedor;
    boolean inAntiLoop = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdPedido);
        FuncaoCampo.mascaraNumeroInteiro(cdForne);
        TrataCombo.setValueComboStAtendimento(stPedido, "1");
        stPedido.setDisable(true);
        cdPedido.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoPedido();
            }
        });
        cdForne.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoForne();
            }
        });

    }

    public void iniciaTela() {
        if (param != null) {
            Pedido ped = (Pedido) param;
            cdPedido.setText(ped.getCdPedido().toString());
            validaCodigoPedido();
        } else {
            atualizaPedidoProd();
        }
    }

    private void validaCodigoPedido() {
        if (!cdPedido.getText().equals("") & pedido == null) {
            try {
                pedido = new Pedido();
                pedido.setCdPedido(Integer.parseInt(cdPedido.getText()));
                dao.get(pedido);
                cdForne.setText(pedido.getCdFornecedor().toString());
                validaCodigoForne();
                TrataCombo.setValueComboStAtendimento(stPedido, pedido.getStPedido());
                stPedido.setDisable(false);
                lblCadastro.setText(Numero.getCadastro(pedido.getCdUserCad(), pedido.getDtCadastro()));
                cdPedido.setEditable(false);
                atualizaPedidoProd();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                pedido = null;
                cdPedido.requestFocus();
            }
        }
    }

    public void abrirListaProduto(PedidoProdHit movto) {
        if (movto.pedidoProd != null) {
            Alerta.AlertaError("Não permitido!", "Não é possível alterar um produto já efetivado.");
            return;
        }

        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            movto.cdProduto.setText(valor);
            validaCodigoProduto(movto);
        }
    }
    
    @FXML
    public void pesquisarPedidoCompra() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPedidoCompra();
        if (valor != null) {
            cdPedido.setText(valor);
            validaCodigoPedido();
        }
    }

    @FXML
    public void pesquisarFornecedor() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Fornecedor(), true);
        if (valor != null) {
            cdForne.setText(valor);
            validaCodigoForne();
        }
    }

    private void validaCodigoForne() {
        if (!cdForne.getText().equals("")) {
            boolean vInBusca = true;
            if (fornecedor != null) {
                if (fornecedor.getCdFornecedor() == Integer.parseInt(cdForne.getText())) {
                    vInBusca = false;
                }
            }
            if (vInBusca) {
                try {
                    fornecedor = new Fornecedor();
                    fornecedor.setCdFornecedor(Integer.parseInt(cdForne.getText()));
                    dao.get(fornecedor);
                    if (!fornecedor.getInAtivo()) {
                        Alerta.AlertaError("Fornecedor inválido!", "Fornecedor está inativo.");
                        fornecedor = null;
                        dsForne.setText("");
                        nrCpfCnpj.setText("");
                        cdForne.requestFocus();
                        return;
                    }
                    Pessoa pessoa = new Pessoa();
                    pessoa.setCdPessoa(fornecedor.getCdPessoa());
                    dao.get(pessoa);
                    dsForne.setText(pessoa.getDsPessoa());
                    if (pessoa.getTpPessoa().equals("F")) {
                        nrCpfCnpj.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    } else {
                        nrCpfCnpj.setText(Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()));
                    }
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    fornecedor = null;
                    dsForne.setText("");
                    nrCpfCnpj.setText("");
                    cdForne.requestFocus();
                }
            }
        } else {
            fornecedor = null;
            dsForne.setText("");
            nrCpfCnpj.setText("");
        }
    }

    private void validaCodigoProduto(PedidoProdHit pedidoHit) {
        if (!pedidoHit.cdProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(pedidoHit.cdProduto.getText()));
                dao.get(prod);

                if (pedidoHit.pedidoProd == null & inAntiLoop) {
                    inAntiLoop = false;
                    if (!prod.getInAtivo()) {
                        Alerta.AlertaError("Inválido", "Produto está inativo.");
                        pedidoHit.cdProduto.requestFocus();
                        inAntiLoop = true;
                        return;
                    }

                    for (PedidoProdHit movtoHit : listPedidoProd) {
                        if (movtoHit.cdProduto.getText().equals(pedidoHit.cdProduto.getText())
                                && !movtoHit.equals(pedidoHit)) {
                            Alerta.AlertaError("Inválido", "Produto já está na lista");
                            pedidoHit.cdProduto.requestFocus();
                            inAntiLoop = true;
                            return;
                        }
                    }
                    inAntiLoop = true;
                }

                pedidoHit.dsProduto.setText(prod.getDsProduto());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Produto não encontrado!");
                pedidoHit.cdProduto.requestFocus();
            }
        } else {
            pedidoHit.dsProduto.setText("");
        }
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        pedido = null;
        fornecedor = null;
        listPedidoProd = new ArrayList<>();
        FuncaoCampo.limparCampos(anchor);
        lblCadastro.setText("");
        cdPedido.setEditable(true);
        TrataCombo.setValueComboStAtendimento(stPedido, "1");
        stPedido.setDisable(true);
        iniciaTela();
    }

    @FXML
    public void salvar() {
        if (pedido != null) {
            if (pedido.getStPedido().equals("2")) {
                Alerta.AlertaError("Não autorizado", "Pedido já encerrado, não permitido alterações.");
                return;
            }

            if (pedido.getStPedido().equals("3")) {
                Alerta.AlertaError("Não autorizado", "Pedido já cancelado, não permitido alterações.");
                return;
            }

            if (TrataCombo.getValueComboStAtendimento(stPedido).equals("3")) { //Cancelamento
                for (PedidoProdHit pedidoHit : listPedidoProd) {
                    if (pedidoHit.pedidoProd != null) {
                        if (pedidoHit.pedidoProd.getQtEntregue() > 0) {
                            Alerta.AlertaError("Não autorizado", "Pedido já contém produtos entregues, não permitido cancelamento.");
                            return;
                        }
                    }
                }
                try {
                    dao.autoCommit(false);
                    dao.get(pedido);
                    pedido.setStPedido("3");
                    dao.update(pedido);
                    dao.commit();
                    Alerta.AlertaInfo("Concluído", "Pedido Cancelado!");
                    return;
                } catch (Exception ex) {
                    Alerta.AlertaError("Erro!", ex.getMessage());
                    return;
                }
            }
        }

        if (TrataCombo.getValueComboStAtendimento(stPedido).equals("2")) {
            Alerta.AlertaError("Não autorizado", "Não permitido encerrar um pedido nesta tela.");
            return;
        }

        if (cdForne.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Fornecedor é obrigatório");
            cdForne.requestFocus();
            return;
        }

        for (PedidoProdHit pedidoHit : listPedidoProd) {
            if (pedidoHit.cdProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Código do produto é obrigatório");
                pedidoHit.cdProduto.requestFocus();
                return;
            }

            if (pedidoHit.qtProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Quantidade do pedido é obrigatório.");
                pedidoHit.qtProduto.requestFocus();
                return;
            } else {
                double qtProduto = Double.parseDouble(pedidoHit.qtProduto.getText());
                if (qtProduto <= 0) {
                    Alerta.AlertaError("Campo inválido", "Quantidade do pedido deve ser maior que 0.");
                    pedidoHit.qtProduto.requestFocus();
                    return;
                }

                double qtEntregue = (pedidoHit.qtEntregue.getText().equals("") ? 0.0 : Double.parseDouble(pedidoHit.qtEntregue.getText()));
                if (qtProduto < qtEntregue) {
                    Alerta.AlertaError("Campo inválido", "Quantidade do pedido menor que quantidade entregue.");
                    pedidoHit.qtProduto.requestFocus();
                    return;
                }
            }

            if (pedidoHit.vlProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Valor do produto é obrigatório");
                pedidoHit.vlProduto.requestFocus();
                return;
            }

            if (pedidoHit.dtPrevEntrega.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Data de previsão de entrega do produto é obrigatório");
                pedidoHit.dtPrevEntrega.requestFocus();
                return;
            }

        }
        try {
            dao.autoCommit(false);
            if (pedido == null) {
                pedido = new Pedido();
                pedido.setCdFornecedor(Integer.parseInt(cdForne.getText()));
                pedido.setStPedido(TrataCombo.getValueComboStAtendimento(stPedido));
                pedido.setCdUserCad(MenuControl.usuarioAtivo);
                pedido.setDtCadastro(Data.getAgora());
                dao.save(pedido);
            } else {
                pedido.setCdFornecedor(Integer.parseInt(cdForne.getText()));
                pedido.setStPedido(TrataCombo.getValueComboStAtendimento(stPedido));
                dao.update(pedido);
            }

            for (PedidoProdHit pedidoHit : listPedidoProd) {
                if (pedidoHit.pedidoProd == null) {
                    PedidoProduto atendPrd = new PedidoProduto();
                    atendPrd.setCdPedido(pedido.getCdPedido());
                    atendPrd.setCdProduto(Integer.parseInt(pedidoHit.cdProduto.getText()));
                    atendPrd.setQtProduto(Double.parseDouble(pedidoHit.qtProduto.getText()));
                    atendPrd.setVlUnitario(Double.parseDouble(pedidoHit.vlProduto.getText()));
                    atendPrd.setDtPrevEntrega(Data.StringToDate(pedidoHit.dtPrevEntrega.getText()));
                    atendPrd.setQtEntregue(0.0);
                    dao.save(atendPrd);
                } else {
                    if (pedidoHit.isExcluir) {
                        dao.delete(pedidoHit.pedidoProd);
                    } else {
                        boolean vInUpdate = false;
                        Double qtProduto = Double.parseDouble(pedidoHit.qtProduto.getText());
                        if (!qtProduto.equals(pedidoHit.pedidoProd.getQtProduto())) {
                            pedidoHit.pedidoProd.setQtProduto(qtProduto);
                            vInUpdate = true;
                        }

                        Double vlProduto = Double.parseDouble(pedidoHit.qtProduto.getText());
                        if (!vlProduto.equals(pedidoHit.pedidoProd.getQtProduto())) {
                            pedidoHit.pedidoProd.setVlUnitario(vlProduto);
                            vInUpdate = true;
                        }

                        Date dtEntrega = Data.StringToDate(pedidoHit.dtPrevEntrega.getText());
                        if (!dtEntrega.equals(pedidoHit.pedidoProd.getDtPrevEntrega())) {
                            pedidoHit.pedidoProd.setDtPrevEntrega(dtEntrega);
                            vInUpdate = true;
                        }

                        if (vInUpdate) {
                            dao.update(pedidoHit.pedidoProd);
                        }
                    }
                }
            }

            dao.commit();
            Integer cod = pedido.getCdPedido();
            limpar();
            cdPedido.setText(cod.toString());
            validaCodigoPedido();
        } catch (Exception ex) {
            dao.rollback();
            Alerta.AlertaError("Erro!", ex.getMessage());
            return;
        }
        Alerta.AlertaInfo("Concluído", "Pedido salvo!");
        if (param != null) {
            getStage().close();
        }
    }

    private void calculaTotal() {
        double vlTotal = 0.00;
        for (PedidoProdHit pedidoHit : listPedidoProd) {
            if (!pedidoHit.vlTotalProd.getText().equals("")) {
                double valor = Double.parseDouble(pedidoHit.vlTotalProd.getText());
                vlTotal += valor;
            }
        }
        vlTotalPedido.setText(Numero.doubleToReal(vlTotal, 2));
    }

    private void calculaTotalProd(PedidoProdHit hit) {
        if (!hit.qtProduto.getText().equals("") && !hit.vlProduto.getText().equals("")) {
            double vQtProduto = Double.parseDouble(hit.qtProduto.getText());
            double vVlProduto = Double.parseDouble(hit.vlProduto.getText());
            hit.vlTotalProd.setText(Numero.doubleToReal(vQtProduto * vVlProduto, 2));
        } else {
            hit.vlTotalProd.setText("");
        }
    }

    public void atualizaPedidoProd() {
        try {
            ArrayList<Object> prods = new ArrayList<>();
            if (pedido != null) {
                String where = "WHERE $cdPedido$ = " + pedido.getCdPedido() + " ORDER BY $cdProduto$ ASC";
                prods = dao.getAllWhere(new PedidoProduto(), where);
                listPedidoProd.clear();
            }
            if (prods.isEmpty()) {
                PedidoProdHit pedidoHit = new PedidoProdHit();
                listPedidoProd.add(pedidoHit);
            }
            for (Object obj : prods) {
                PedidoProduto pedidoProd = (PedidoProduto) obj;
                PedidoProdHit pedidoHit = new PedidoProdHit();
                pedidoHit.cdProduto.setText(pedidoProd.getCdProduto().toString());
                Produto produto = new Produto();
                produto.setCdProduto(pedidoProd.getCdProduto());
                dao.get(produto);
                pedidoHit.dsProduto.setText(produto.getDsProduto());
                pedidoHit.qtProduto.setText(Numero.doubleToReal(pedidoProd.getQtProduto(), 2));
                pedidoHit.vlProduto.setText(Numero.doubleToReal(pedidoProd.getVlUnitario(), 2));
                pedidoHit.dtPrevEntrega.setText(Data.AmericaToBrasilSemHora(pedidoProd.getDtPrevEntrega()));
                pedidoHit.qtEntregue.setText(Numero.doubleToReal(pedidoProd.getQtEntregue(), 2));
                pedidoHit.vlTotalProd.setText(Numero.doubleToReal(pedidoProd.getQtProduto() * pedidoProd.getVlUnitario(), 2));
                pedidoHit.pedidoProd = pedidoProd;
                listPedidoProd.add(pedidoHit);
            }
            calculaTotal();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void atualizaLista() {
        int total = 0;
        for (PedidoProdHit b : listPedidoProd) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutY = cdProduto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listPedidoProd.iterator();
        for (int i = 0; it.hasNext(); i++) {
            PedidoProdHit b = (PedidoProdHit) it.next();
            if (!b.isExcluir) {
                b.cdProduto.setEditable(cdProduto.isEditable());
                if (b.pedidoProd != null) {
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
                b.qtProduto.setEditable(qtProduto.isEditable());
                b.qtProduto.setPrefHeight(qtProduto.getHeight());
                b.qtProduto.setPrefWidth(qtProduto.getWidth());
                b.qtProduto.setLayoutX(qtProduto.getLayoutX());
                b.qtProduto.setLayoutY(LayoutY);
                b.qtProduto.getStyleClass().addAll(this.qtProduto.getStyleClass());
                b.vlProduto.setEditable(vlProduto.isEditable());
                b.vlProduto.setPrefHeight(vlProduto.getHeight());
                b.vlProduto.setPrefWidth(vlProduto.getWidth());
                b.vlProduto.setLayoutX(vlProduto.getLayoutX());
                b.vlProduto.setLayoutY(LayoutY);
                b.vlProduto.getStyleClass().addAll(this.vlProduto.getStyleClass());
                b.dtPrevEntrega.setEditable(dtPrevEntrega.isEditable());
                b.dtPrevEntrega.setPrefHeight(dtPrevEntrega.getHeight());
                b.dtPrevEntrega.setPrefWidth(dtPrevEntrega.getWidth());
                b.dtPrevEntrega.setLayoutX(dtPrevEntrega.getLayoutX());
                b.dtPrevEntrega.setLayoutY(LayoutY);
                b.dtPrevEntrega.getStyleClass().addAll(this.dtPrevEntrega.getStyleClass());
                b.qtEntregue.setEditable(qtEntregue.isEditable());
                b.qtEntregue.setPrefHeight(qtEntregue.getHeight());
                b.qtEntregue.setPrefWidth(qtEntregue.getWidth());
                b.qtEntregue.setLayoutX(qtEntregue.getLayoutX());
                b.qtEntregue.setLayoutY(LayoutY);
                b.qtEntregue.getStyleClass().addAll(this.qtEntregue.getStyleClass());
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
                painel.getChildren().add(b.cdProduto);
                painel.getChildren().add(b.btnPesqProd);
                painel.getChildren().add(b.dsProduto);
                painel.getChildren().add(b.qtProduto);
                painel.getChildren().add(b.vlProduto);
                painel.getChildren().add(b.dtPrevEntrega);
                painel.getChildren().add(b.qtEntregue);
                painel.getChildren().add(b.vlTotalProd);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutY += (cdProduto.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }
        painel.setPrefHeight(LayoutY + 10);
    }

    public void addValidacao(PedidoProdHit pedidoProdHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(pedidoProdHit.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(pedidoProdHit.qtProduto);
        FuncaoCampo.mascaraNumeroDecimal(pedidoProdHit.vlProduto);

        pedidoProdHit.cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto(pedidoProdHit);
            }
        });

        pedidoProdHit.btnPesqProd.setOnAction((ActionEvent event) -> {
            abrirListaProduto(pedidoProdHit);
        });
        pedidoProdHit.qtProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!pedidoProdHit.qtProduto.getText().equals("")) {
                    Double valor = Double.parseDouble(pedidoProdHit.qtProduto.getText());
                    if (valor <= 0.00) {
                        Alerta.AlertaError("Campo inválido!", "Quantidade não pode ser igual ou menor que zero.");
                        pedidoProdHit.qtProduto.requestFocus();
                        return;
                    }
                    pedidoProdHit.qtProduto.setText(Numero.doubleToReal(valor, 2));
                }
            }
            calculaTotalProd(pedidoProdHit);
            calculaTotal();
        });

        pedidoProdHit.vlProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!pedidoProdHit.vlProduto.getText().equals("")) {
                    Double valor = Double.parseDouble(pedidoProdHit.vlProduto.getText());
                    pedidoProdHit.vlProduto.setText(Numero.doubleToReal(valor, 2));
                }
            }
            calculaTotalProd(pedidoProdHit);
            calculaTotal();
        });

        pedidoProdHit.dtPrevEntrega.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!pedidoProdHit.dtPrevEntrega.getText().equals("")) {
                    try {
                        Data.autoComplete(pedidoProdHit.dtPrevEntrega);
                        Date dataInicio = Data.StringToDate(pedidoProdHit.dtPrevEntrega.getText());
                        if (dataInicio.before(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data de Previsão de Entrega não pode ser menor que a data atual.");
                            pedidoProdHit.dtPrevEntrega.requestFocus();
                            return;
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        pedidoProdHit.dtPrevEntrega.requestFocus();
                    }
                }
            }
        });

        pedidoProdHit.btnAdd.setOnAction((ActionEvent event) -> {
            PedidoProdHit b = new PedidoProdHit();
            listPedidoProd.add(posicao + 1, b);
            atualizaLista();
        });

        pedidoProdHit.btnRem.setOnAction((ActionEvent event) -> {
            if (pedidoProdHit.pedidoProd != null) {
                if (pedidoProdHit.pedidoProd.getQtEntregue() > 0) {
                    Alerta.AlertaError("Negado!", "Não é possivel deletar um item com quantidade entregue.");
                    return;
                }
            }

            if (total == 1) {
                PedidoProdHit b = new PedidoProdHit();
                listPedidoProd.add(b);
            }
            if (pedidoProdHit.pedidoProd == null) {
                listPedidoProd.remove(pedidoProdHit);
            } else {
                listPedidoProd.get(posicao).isExcluir = true;
            }
            atualizaLista();
        });
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class PedidoProdHit {

        PedidoProduto pedidoProd;
        TextField cdProduto = new TextField();
        Button btnPesqProd = new Button();
        TextField dsProduto = new TextField();
        TextField qtProduto = new TextField();
        TextField vlProduto = new TextField();
        TextField dtPrevEntrega = new TextField();
        TextField qtEntregue = new TextField();
        TextField vlTotalProd = new TextField();
        Button btnAdd = new Button();
        Button btnRem = new Button();
        public boolean isExcluir = false;
    }

}
