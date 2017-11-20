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
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.relatorio.Relatorio;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class VendaControl implements Initializable {

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
    TextField vlTotalProds;
    @FXML
    ChoiceBox tpFormaPagto;
    @FXML
    ChoiceBox tpCondPagto;
    @FXML
    TextField vlDesconto;
    @FXML
    TextField vlAdicional;
    @FXML
    TextField vlFrete;
    @FXML
    TextField vlTotalVenda;

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
        TrataCombo.setValueComboTpCondicaoPagto(tpCondPagto, 1);
        TrataCombo.setValueComboTpFormaPagto(tpFormaPagto, 1);
        FuncaoCampo.mascaraNumeroDecimal(vlDesconto);
        FuncaoCampo.mascaraNumeroDecimal(vlAdicional);
        FuncaoCampo.mascaraNumeroDecimal(vlFrete);
        cdCliente.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCliente();
            }
        });
        vlAdicional.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlAdicional.getText().equals("")) {
                    Double valor = Double.parseDouble(vlAdicional.getText());
                    vlAdicional.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
        });
        vlDesconto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlDesconto.getText().equals("")) {
                    Double valorTotal = Double.parseDouble(vlTotalVenda.getText());
                    Double valor = Double.parseDouble(vlDesconto.getText());
                    if (valorTotal < valor) {
                        Alerta.AlertaError("Campo inválido!", "Valor do desconto maior que o valor de venda.");
                        vlDesconto.requestFocus();
                        return;
                    }
                    vlDesconto.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
        });
        vlFrete.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlFrete.getText().equals("")) {
                    Double valor = Double.parseDouble(vlFrete.getText());
                    vlFrete.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
        });
    }

    public void iniciaTela() {
        if (param != null) {
            atendimento = (Atendimento) param;
            lblAtendimento.setText("Atendimento: " + atendimento.getCdAtend() + " - Data: " + Data.AmericaToBrasil(atendimento.getDtCadastro()));
        }
        atualizaVendaProd();
    }

    @FXML
    public void abrirTelaDivisao(){
        Tela tela = new Tela();
        Parametro p = new Parametro();
        p.setListVendaProd(listVendaProd);
        p.setVlTotal(Double.parseDouble(vlTotalVenda.getText()));
        tela.abrirTelaModalComParam(stage, Tela.DIVISAO, p);
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

    @FXML
    public void visualizarParcelas() {
        Double vlTotal = Double.parseDouble(vlTotalVenda.getText());
        if (vlTotal > 0 && TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) != null) {
            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            conta.setVlConta(vlTotal);
            Tela tela = new Tela();
            tela.abrirTelaModalComParam(stage, Tela.CON_PARCELA, conta);
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
                    if (!prod.getInVenda()) {
                        Alerta.AlertaError("Inválido", "Produto não permitido em venda.");
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

                    if (prod.getQtEstqAtual() <= 0) {
                        Alerta.AlertaWarning("Aviso!", "Produto sem estoque ou estoque negativo, favor verificar!");
                    }

                    EstoqueControl prodController = new EstoqueControl();
                    Double vlPreco = prodController.getUltimoPreco(prod.getCdProduto());
                    if (vlPreco == null) {
                        inAntiLoop = true;
                        Alerta.AlertaError("Notificação", "Produto: " + prod.getCdProduto() + " sem preço cadastrado.");
                        vendaHit.cdProduto.requestFocus();
                        return;
                    }
                    vendaHit.vlUnitario.setText(Numero.doubleToReal(vlPreco, 2));

                    inAntiLoop = true;
                }

                vendaHit.dsProduto.setText(prod.getDsProduto());

                if (atendimento != null) {
                    AtendimentoProduto atendProd = new AtendimentoProduto();
                    atendProd.setCdAtend(atendimento.getCdAtend());
                    atendProd.setDtAtend(atendimento.getDtAtend());
                    atendProd.setCdProduto(prod.getCdProduto());
                    try {
                        dao.get(atendProd);
                        if (atendProd.getQtProduto() == atendProd.getQtPaga()) {
                            Alerta.AlertaError("Notificação", "Produto: " + prod.getCdProduto() + " está como pago no atendimento.");
                            vendaHit.cdProduto.requestFocus();
                            return;
                        }
                        vendaHit.atendProd = atendProd;
                        vendaHit.qtUnitario.setText(Numero.doubleToReal(atendProd.getQtProduto() - atendProd.getQtPaga(), 2));
                        vendaHit.cdProduto.setEditable(false);
                        vendaHit.cdProduto.getStyleClass().addAll("texto_estatico_center");
                    } catch (Exception ex) {
                    }
                }

                calculaTotalProd();
            } catch (Exception ex) {
                inAntiLoop = true;
                Alerta.AlertaError("Notificação", ex.getMessage());
                vendaHit.cdProduto.requestFocus();
            }
        } else {
            vendaHit.dsProduto.setText("");
        }
    }

    public void calculaTotalProd() {
        Double totalProd = 0.00;
        Double total = 0.00;
        for (VendaProdHit vendaHit : listVendaProd) {
            double vlUnit = 0.00;
            if (!vendaHit.vlUnitario.getText().equals("")) {
                vlUnit = Double.parseDouble(vendaHit.vlUnitario.getText());
            }
            double qtUnit = 0.00;
            if (!vendaHit.qtUnitario.getText().equals("")) {
                qtUnit = Double.parseDouble(vendaHit.qtUnitario.getText());
            }
            totalProd += qtUnit * vlUnit;
            vendaHit.vlTotalProd.setText(Numero.doubleToReal(qtUnit * vlUnit, 2));
        }
        double vVlDesconto = 0.00;
        if (!vlDesconto.getText().equals("")) {
            vVlDesconto = Double.parseDouble(vlDesconto.getText());
        }
        double vVlAdicional = 0.00;
        if (!vlAdicional.getText().equals("")) {
            vVlAdicional = Double.parseDouble(vlAdicional.getText());
        }
        double vVlFrete = 0.00;
        if (!vlFrete.getText().equals("")) {
            vVlFrete = Double.parseDouble(vlFrete.getText());
        }
        vlTotalProds.setText(Numero.doubleToReal(totalProd, 2));
        total += ((totalProd + vVlAdicional + vVlFrete) - vVlDesconto);
        if (total < 0.00) {
            Alerta.AlertaWarning("Alerta!", "Desconto maior que o total da venda, desconto removido.");
            vlDesconto.setText("");
            calculaTotalProd();
            return;
        }
        vlTotalVenda.setText(Numero.doubleToReal(total, 2));
    }

    @FXML
    public void salvar() {
        for (VendaProdHit vendaHit : listVendaProd) {
            if (vendaHit.cdProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido!", "Código do produto é obrigatório.");
                vendaHit.cdProduto.requestFocus();
                return;
            }
            if (vendaHit.qtUnitario.getText().equals("")) {
                Alerta.AlertaError("Campo inválido!", "Quantidade da venda é obrigatório.");
                vendaHit.qtUnitario.requestFocus();
                return;
            }
        }

        if (TrataCombo.getValueComboTpFormaPagto(tpFormaPagto) == null) {
            Alerta.AlertaError("Campo inválido!", "Forma de pagamento é obrigatório.");
            tpCondPagto.requestFocus();
            return;
        }

        if (TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) == null) {
            Alerta.AlertaError("Campo inválido!", "Condição de pagamento é obrigatório.");
            tpCondPagto.requestFocus();
            return;
        }

        try {
            CondicaoPagto cond = new CondicaoPagto();
            cond.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            dao.get(cond);
            if (!cond.getInEntrada() || cond.getQtParcelas() > 1 || cond.getNrIntervalo() > 0) {
                if (cdCliente.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido!", "Caso não seja venda com pagamento no ato, cliente é obrigatório.");
                    cdCliente.requestFocus();
                    return;
                }
            }

            CaixaControl caixa = new CaixaControl();
            FechamentoCaixa fechamento;
            try {
                fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
            } catch (Exception ex) {
                Alerta.AlertaWarning("Negado!", ex.getMessage());
                return;
            }

            dao.autoCommit(false);
            VendaCompra venda = new VendaCompra();
            venda.setTpMovto("S");
            venda.setCdPessoa(pessoa != null ? pessoa.getCdPessoa() : null);
            venda.setVlDesconto(!vlDesconto.getText().equals("") ? Double.parseDouble(vlDesconto.getText()) : 0.0);
            venda.setVlAdicional(!vlAdicional.getText().equals("") ? Double.parseDouble(vlAdicional.getText()) : 0.0);
            venda.setVlFrete(!vlFrete.getText().equals("") ? Double.parseDouble(vlFrete.getText()) : 0.0);
            venda.setCdUserCad(MenuControl.usuarioAtivo);
            venda.setDtCadastro(Data.getAgora());
            if (atendimento != null) {
                venda.setCdAtend(atendimento.getCdAtend());
                venda.setDtAtend(atendimento.getDtAtend());
            }
            venda.setVlTotal(Double.parseDouble(vlTotalVenda.getText()));
            venda.setInCancelado(false);

            dao.save(venda);

            for (VendaProdHit vendaHit : listVendaProd) { //baixa do produto de um atendimento
                VendaCompraProduto vendaProd = new VendaCompraProduto();
                Double qtVenda = Double.parseDouble(vendaHit.qtUnitario.getText());
                vendaProd.setCdMovto(venda.getCdMovto());
                vendaProd.setCdProduto(Integer.parseInt(vendaHit.cdProduto.getText()));
                vendaProd.setQtUnitario(qtVenda);
                vendaProd.setVlUnitario(Double.parseDouble(vendaHit.vlUnitario.getText()));
                dao.save(vendaProd);
                if (vendaHit.atendProd != null) {
                    Double qtPaga = vendaHit.atendProd.getQtPaga() + qtVenda;
                    vendaHit.atendProd.setQtPaga(qtPaga);
                    dao.update(vendaHit.atendProd);
                }
                //Gera movimento estoque
                MovtoEstoque movtoEstoque = new MovtoEstoque();
                movtoEstoque.setCdMovCompVend(venda.getCdMovto());
                movtoEstoque.setTpMovto(EstoqueControl.SAIDA);
                movtoEstoque.setCdProduto(vendaProd.getCdProduto());
                movtoEstoque.setDtMovto(Data.getAgora());
                movtoEstoque.setQtMovto(vendaProd.getQtUnitario());
                movtoEstoque.setInCancelado(false);
                EstoqueControl estq = new EstoqueControl();
                estq.geraMovtoEstoque(movtoEstoque);
            }

            if (atendimento != null) { //Encerra o atendimento
                String where = "WHERE $cdAtend$ = " + atendimento.getCdAtend() + " AND $dtAtend$ = '" + Data.BrasilToAmericaSemHora(atendimento.getDtAtend()) + "'";
                ArrayList<Object> atendProds = dao.getAllWhere(new AtendimentoProduto(), where);
                boolean vInEncerrado = true;
                for (Object obj : atendProds) {
                    AtendimentoProduto atendProd = (AtendimentoProduto) obj;
                    if (atendProd.getQtProduto() > atendProd.getQtPaga()) {
                        vInEncerrado = false;
                    }
                }

                if (vInEncerrado) {
                    atendimento.setStAtend("2");
                    dao.update(atendimento);
                }
            }

            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setTpMovto("E");
            conta.setDtMovto(Data.getAgora());
            conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            conta.setCdForma(TrataCombo.getValueComboTpFormaPagto(tpFormaPagto));
            conta.setCdMovto(venda.getCdMovto());
            conta.setVlConta(Double.parseDouble(vlTotalVenda.getText()));
            conta.setStConta("1");
            dao.save(conta);

            ParcelaControl parcela = new ParcelaControl();
            parcela.gerarParcelas(conta, fechamento);

            ParcelaControl parcelaControl = new ParcelaControl();
            parcelaControl.encerrarConta(conta);
            dao.commit();

            Relatorio recibo = new Relatorio();
            recibo.gerarReciboVenda(venda.getCdMovto());
        } catch (Exception ex) {
            ex.printStackTrace();
            dao.rollback();
            Alerta.AlertaError("Erro!", ex.getMessage());
            stage.close();
            return;
        }

        if (Alerta.AlertaConfirmation("Confirmação", "Venda efetivada com sucesso.\nDeseja realizar uma nova venda?")) {
            param = null;
            limparTela();
        } else {
            stage.close();
        }
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        atendimento = null;
        cliente = null;
        pessoa = null;
        listVendaProd = new ArrayList<>();
        FuncaoCampo.limparCampos(painel);
        lblAtendimento.setText("");
        iniciaTela();
        TrataCombo.setValueComboTpCondicaoPagto(tpCondPagto, 1);
        TrataCombo.setValueComboTpFormaPagto(tpFormaPagto, 1);
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
                boolean vInAdd = true;
                if (vendaProd.getQtProduto() > vendaProd.getQtPaga()) {
                    VendaProdHit vendaHit = new VendaProdHit();
                    vendaHit.cdProduto.setText(vendaProd.getCdProduto().toString());
                    Produto produto = new Produto();
                    produto.setCdProduto(vendaProd.getCdProduto());
                    dao.get(produto);
                    vendaHit.dsProduto.setText(produto.getDsProduto());
                    vendaHit.qtUnitario.setText(Numero.doubleToReal(vendaProd.getQtProduto() - vendaProd.getQtPaga(), 2));
                    vendaHit.atendProd = vendaProd;
                    EstoqueControl prodController = new EstoqueControl();
                    Double vlPreco = prodController.getUltimoPreco(produto.getCdProduto());
                    if (vlPreco == null) {
                        inAntiLoop = true;
                        vInAdd = false;
                        Alerta.AlertaError("Não permitido", "Produto: " + produto.getCdProduto() + " sem preço cadastrado.");
                        stage.close();
                    }
                    vendaHit.vlUnitario.setText(Numero.doubleToReal(vlPreco, 2));
                    if (vInAdd) {
                        listVendaProd.add(vendaHit);
                    }
                }
            }
            calculaTotalProd();
        } catch (Exception ex) {
            ex.printStackTrace();
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
                b.cdProduto.getStyleClass().addAll("texto_estatico_center");
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
            //if (b.atendProd != null) {
            //b.qtUnitario.setEditable(false);
            //b.qtUnitario.getStyleClass().addAll("numero_estatico");
            //}
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

        vendaProdHit.qtUnitario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vendaProdHit.qtUnitario.getText().equals("")) {
                    Double valor = Double.parseDouble(vendaProdHit.qtUnitario.getText());
                    if (valor <= 0) {
                        Alerta.AlertaError("Campo inválido", "Venda deve ser maior que 0.00!");
                        vendaProdHit.qtUnitario.requestFocus();
                        return;
                    }
                    vendaProdHit.qtUnitario.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
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
            calculaTotalProd();
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
    
    public class Parametro{
         private ArrayList<VendaProdHit> listVendaProd;
         private Double vlTotal;

        public ArrayList<VendaProdHit> getListVendaProd() {
            return listVendaProd;
        }

        public void setListVendaProd(ArrayList<VendaProdHit> listVendaProd) {
            this.listVendaProd = listVendaProd;
        }

        public Double getVlTotal() {
            return vlTotal;
        }

        public void setVlTotal(Double vlTotal) {
            this.vlTotal = vlTotal;
        }
    }

}
