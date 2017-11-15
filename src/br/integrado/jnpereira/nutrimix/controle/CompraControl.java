/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompraProduto;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.modelo.PedidoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class CompraControl implements Initializable {

    @FXML
    AnchorPane painel;
    @FXML
    TextField nrNotaFiscal;
    @FXML
    TextField cdSerie;
    @FXML
    TextField dtEmissao;
    @FXML
    TextField cdPedido;
    @FXML
    TextField cdForne;
    @FXML
    TextField dsForne;
    @FXML
    TextField nrCpfCnpj;
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
    TextField vlTotalCompra;

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
    private Fornecedor fornecedor;
    private Pedido pedido;

    ArrayList<CompraProdHit> listCompraProd = new ArrayList<>();
    double LayoutY;
    boolean inAntiLoop = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdForne);
        FuncaoCampo.mascaraNumeroInteiro(nrNotaFiscal);
        FuncaoCampo.mascaraNumeroInteiro(cdPedido);
        TrataCombo.setValueComboTpCondicaoPagto(tpCondPagto, 1);
        TrataCombo.setValueComboTpFormaPagto(tpFormaPagto, 1);
        FuncaoCampo.mascaraNumeroDecimal(vlDesconto);
        FuncaoCampo.mascaraNumeroDecimal(vlAdicional);
        FuncaoCampo.mascaraNumeroDecimal(vlFrete);
        FuncaoCampo.mascaraTexto(cdSerie, 10);
        FuncaoCampo.mascaraData(dtEmissao);
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
        cdForne.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoForne();
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
                    Double valorTotal = Double.parseDouble(vlTotalCompra.getText());
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
        dtEmissao.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtEmissao.getText().equals("")) {
                    try {
                        Data.autoComplete(dtEmissao);
                        Date dataInicio = Data.StringToDate(dtEmissao.getText());
                        if (dataInicio.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Dt. Emissão não pode ser maior que a data atual.");
                            dtEmissao.requestFocus();
                            return;
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtEmissao.requestFocus();
                    }
                }
            }
        });
    }

    public void iniciaTela() {
        if (param != null) {
            pedido = (Pedido) param;
        }
        atualizaCompraProd();
    }

    private void validaCodigoPedido() {
        if (!cdPedido.getText().equals("")) {
            try {
                if (pedido != null) {
                    if (Integer.parseInt(cdPedido.getText()) == pedido.getCdPedido()) {
                        return;
                    }
                }

                pedido = new Pedido();
                pedido.setCdPedido(Integer.parseInt(cdPedido.getText()));
                dao.get(pedido);
                if (!pedido.getStPedido().equals("1")) {
                    Alerta.AlertaError("Campo inválido!", "Apenas permitido pedidos pendentes.");
                    cdPedido.setText("");
                    pedido = null;
                    return;
                }
                cdForne.setText(pedido.getCdFornecedor().toString());
                validaCodigoForne();
                atualizaCompraProd();
            } catch (Exception ex) {
                Alerta.AlertaError("Campo inválido!", ex.getMessage());
            }
        } else {
            limparTelaPedido();
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
    public void visualizarParcelas() {
        Double vlTotal = Double.parseDouble(vlTotalCompra.getText());
        if (vlTotal > 0 && TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) != null) {
            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            conta.setVlConta(vlTotal);
            Tela tela = new Tela();
            tela.abrirTelaModalComParam(stage, Tela.CON_PARCELA, conta);
        }
    }

    public void abrirListaProduto(CompraProdHit movto) {
        if (movto.pedidoProd != null) {
            Alerta.AlertaError("Não permitido!", "Altere o item no pedido.");
            return;
        }

        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            movto.cdProduto.setText(valor);
            validaCodigoProduto(movto);
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
                    pessoa = new Pessoa();
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
                    pessoa = null;
                    dsForne.setText("");
                    nrCpfCnpj.setText("");
                    cdForne.requestFocus();
                }
            }
        } else {
            fornecedor = null;
            pessoa = null;
            dsForne.setText("");
            nrCpfCnpj.setText("");
        }
    }

    private void validaCodigoProduto(CompraProdHit compraHit) {
        if (!compraHit.cdProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(compraHit.cdProduto.getText()));
                dao.get(prod);

                if (inAntiLoop) {
                    inAntiLoop = false;
                    if (!prod.getInAtivo()) {
                        Alerta.AlertaError("Inválido", "Produto está inativo.");
                        compraHit.cdProduto.requestFocus();
                        inAntiLoop = true;
                        return;
                    }

                    for (CompraProdHit movtoHit : listCompraProd) {
                        if (movtoHit.cdProduto.getText().equals(compraHit.cdProduto.getText())
                                && !movtoHit.equals(compraHit)) {
                            Alerta.AlertaError("Inválido", "Produto já está na lista");
                            compraHit.cdProduto.requestFocus();
                            inAntiLoop = true;
                            return;
                        }
                    }
                    inAntiLoop = true;
                }

                compraHit.dsProduto.setText(prod.getDsProduto());

                if (pedido != null) {
                    PedidoProduto pedidoProd = new PedidoProduto();
                    pedidoProd.setCdPedido(pedido.getCdPedido());
                    pedidoProd.setCdProduto(prod.getCdProduto());
                    try {
                        dao.get(pedidoProd);
                        if (pedidoProd.getQtProduto() == pedidoProd.getQtEntregue()) {
                            Alerta.AlertaError("Notificação", "Produto: " + prod.getCdProduto() + " está como totalmente entregue no pedido.");
                            compraHit.cdProduto.requestFocus();
                            return;
                        }
                        compraHit.pedidoProd = pedidoProd;
                        compraHit.qtUnitario.setText(Numero.doubleToReal(pedidoProd.getQtProduto() - pedidoProd.getQtEntregue(), 2));
                        compraHit.vlUnitario.setText(Numero.doubleToReal(pedidoProd.getVlUnitario(), 2));
                        compraHit.vlUnitario.setEditable(false);
                        compraHit.vlUnitario.getStyleClass().addAll("numero_estatico");
                        compraHit.cdProduto.setEditable(false);
                        compraHit.cdProduto.getStyleClass().addAll("texto_estatico_center");
                    } catch (Exception ex) {
                    }
                }

                calculaTotalProd();
            } catch (Exception ex) {
                inAntiLoop = true;
                Alerta.AlertaError("Notificação", ex.getMessage());
                compraHit.cdProduto.requestFocus();
            }
        } else {
            compraHit.dsProduto.setText("");
        }
    }

    public void calculaTotalProd() {
        Double totalProd = 0.00;
        Double total = 0.00;
        for (CompraProdHit compraHit : listCompraProd) {
            double vlUnit = 0.00;
            if (!compraHit.vlUnitario.getText().equals("")) {
                vlUnit = Double.parseDouble(compraHit.vlUnitario.getText());
            }
            double qtUnit = 0.00;
            if (!compraHit.qtUnitario.getText().equals("")) {
                qtUnit = Double.parseDouble(compraHit.qtUnitario.getText());
            }
            totalProd += qtUnit * vlUnit;
            compraHit.vlTotalProd.setText(Numero.doubleToReal(qtUnit * vlUnit, 2));
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
        vlTotalCompra.setText(Numero.doubleToReal(total, 2));
    }

    @FXML
    public void salvar() {
        if (cdForne.getText().equals("")) {
            Alerta.AlertaError("Campo inválido!", "Fornecedor é obrigatório.");
            tpCondPagto.requestFocus();
            return;
        }

        if ((nrNotaFiscal.getText().equals("") && !cdSerie.getText().equals(""))
                || (!nrNotaFiscal.getText().equals("") && cdSerie.getText().equals(""))) {
            Alerta.AlertaError("Campo inválido!", "Caso tenha nota fiscal, Nª da Nota e Série são obrigatórios.");
            return;
        }

        for (CompraProdHit compraHit : listCompraProd) {
            if (compraHit.cdProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido!", "Código do produto é obrigatório.");
                compraHit.cdProduto.requestFocus();
                return;
            }
            if (compraHit.qtUnitario.getText().equals("")) {
                Alerta.AlertaError("Campo inválido!", "Quantidade da compra é obrigatório.");
                compraHit.qtUnitario.requestFocus();
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
            dao.autoCommit(false);
            CondicaoPagto cond = new CondicaoPagto();
            cond.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            dao.get(cond);

            CaixaControl caixa = new CaixaControl();
            FechamentoCaixa fechamento;
            try {
                fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
            } catch (Exception ex) {
                Alerta.AlertaWarning("Negado!", ex.getMessage());
                return;
            }

            VendaCompra compra = new VendaCompra();
            compra.setTpMovto(EstoqueControl.ENTRADA);
            compra.setCdPessoa(pessoa.getCdPessoa());
            compra.setNrNota(!nrNotaFiscal.getText().equals("") ? nrNotaFiscal.getText() : null);
            compra.setCdSerie(!cdSerie.getText().equals("") ? cdSerie.getText() : null);
            compra.setCdPedido(!cdPedido.getText().equals("") ? Integer.parseInt(cdPedido.getText()) : null);
            compra.setDtEmissao(Data.StringToDate(dtEmissao.getText()));
            compra.setVlDesconto(!vlDesconto.getText().equals("") ? Double.parseDouble(vlDesconto.getText()) : 0.0);
            compra.setVlAdicional(!vlAdicional.getText().equals("") ? Double.parseDouble(vlAdicional.getText()) : 0.0);
            compra.setVlFrete(!vlFrete.getText().equals("") ? Double.parseDouble(vlFrete.getText()) : 0.0);
            compra.setCdUserCad(MenuControl.usuarioAtivo);
            compra.setDtCadastro(Data.getAgora());
            if (pedido != null) {
                compra.setCdPedido(pedido.getCdPedido());
            }
            compra.setVlTotal(Double.parseDouble(vlTotalCompra.getText()));
            compra.setInCancelado(false);

            dao.save(compra);

            for (CompraProdHit compraHit : listCompraProd) { //baixa do produto de um pedido
                VendaCompraProduto compraProd = new VendaCompraProduto();
                Double qtCompra = Double.parseDouble(compraHit.qtUnitario.getText());
                compraProd.setCdMovto(compra.getCdMovto());
                compraProd.setCdProduto(Integer.parseInt(compraHit.cdProduto.getText()));
                Produto prod = new Produto();
                prod.setCdProduto(compraProd.getCdProduto());
                dao.get(prod);
                compraProd.setQtUnitario(qtCompra * prod.getQtConversao()); //Conversão conforme cadastro
                compraProd.setVlUnitario(divisao(Double.parseDouble(compraHit.vlUnitario.getText()), prod.getQtConversao()));
                dao.save(compraProd);
                if (compraHit.pedidoProd != null) {
                    Double qtEntregue = compraHit.pedidoProd.getQtEntregue() + qtCompra;
                    compraHit.pedidoProd.setQtEntregue(qtEntregue);
                    dao.update(compraHit.pedidoProd);
                }
                //Gera movimento estoque
                MovtoEstoque movtoEstoque = new MovtoEstoque();
                movtoEstoque.setCdMovCompVend(compra.getCdMovto());
                movtoEstoque.setTpMovto(EstoqueControl.ENTRADA);
                movtoEstoque.setCdProduto(compraProd.getCdProduto());
                movtoEstoque.setDtMovto(Data.getAgora());
                movtoEstoque.setQtMovto(compraProd.getQtUnitario());
                movtoEstoque.setVlItem(compraProd.getVlUnitario());
                movtoEstoque.setInCancelado(false);
                EstoqueControl estq = new EstoqueControl();
                estq.geraMovtoEstoque(movtoEstoque);
            }

            if (pedido != null) { //Encerra o pedido
                String where = "WHERE $cdPedido$ = " + pedido.getCdPedido();
                ArrayList<Object> pedidoProds = dao.getAllWhere(new PedidoProduto(), where);
                boolean vInEncerrado = true;
                for (Object obj : pedidoProds) {
                    PedidoProduto pedidoProd = (PedidoProduto) obj;
                    if (pedidoProd.getQtProduto() > pedidoProd.getQtEntregue()) {
                        vInEncerrado = false;
                    }
                }

                if (vInEncerrado) {
                    pedido.setStPedido("2");
                    dao.update(pedido);
                }
            }

            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setTpMovto("S");
            conta.setDtMovto(Data.getAgora());
            conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            conta.setCdForma(TrataCombo.getValueComboTpFormaPagto(tpFormaPagto));
            conta.setCdMovto(compra.getCdMovto());
            conta.setVlConta(Double.parseDouble(vlTotalCompra.getText()));
            conta.setStConta("1");
            dao.save(conta);

            ParcelaControl parcela = new ParcelaControl();
            parcela.gerarParcelas(conta, fechamento);

            ParcelaControl parcelaControl = new ParcelaControl();
            parcelaControl.encerrarConta(conta);

            dao.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            dao.rollback();
            Alerta.AlertaError("Erro!", ex.getMessage());
            return;
        }

        if (Alerta.AlertaConfirmation("Confirmação", "Compra efetivada com sucesso.\nDeseja realizar uma nova compra?")) {
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
        pedido = null;
        fornecedor = null;
        pessoa = null;
        listCompraProd = new ArrayList<>();
        FuncaoCampo.limparCampos(painel);
        iniciaTela();
    }

    private void limparTelaPedido() {
        String nrNotaFiscal = this.nrNotaFiscal.getText();
        String cdSerie = this.cdSerie.getText();
        String dtEmissao = this.dtEmissao.getText();

        pedido = null;
        fornecedor = null;
        pessoa = null;
        listCompraProd = new ArrayList<>();
        FuncaoCampo.limparCampos(painel);
        iniciaTela();

        this.nrNotaFiscal.setText(nrNotaFiscal);
        this.cdSerie.setText(cdSerie);
        this.dtEmissao.setText(dtEmissao);
    }

    private Double divisao(Double v1, Double v2) {
        if (v1 == null | v2 == null) {
            return 0.00;
        }
        if (v1 == 0 | v2 == 0) {
            return 0.00;
        }
        return v1 / v2;
    }

    public void atualizaCompraProd() {
        try {
            ArrayList<Object> pedidosProd = new ArrayList<>();
            if (pedido != null) {
                String where = "WHERE $cdPedido$ = " + pedido.getCdPedido() + " ORDER BY $cdProduto$ ASC";
                pedidosProd = dao.getAllWhere(new PedidoProduto(), where);
                listCompraProd.clear();
            }
            if (pedidosProd.isEmpty()) {
                CompraProdHit compraHit = new CompraProdHit();
                listCompraProd.add(compraHit);
            }
            for (Object obj : pedidosProd) {
                PedidoProduto pedidoProd = (PedidoProduto) obj;
                boolean vInAdd = true;
                if (pedidoProd.getQtProduto() > pedidoProd.getQtEntregue()) {
                    CompraProdHit compraHit = new CompraProdHit();
                    compraHit.cdProduto.setText(pedidoProd.getCdProduto().toString());
                    Produto produto = new Produto();
                    produto.setCdProduto(pedidoProd.getCdProduto());
                    dao.get(produto);
                    compraHit.dsProduto.setText(produto.getDsProduto());
                    compraHit.qtUnitario.setText(Numero.doubleToReal(pedidoProd.getQtProduto() - pedidoProd.getQtEntregue(), 2));
                    compraHit.pedidoProd = pedidoProd;
                    compraHit.vlUnitario.setText(Numero.doubleToReal(pedidoProd.getVlUnitario(), 2));
                    if (vInAdd) {
                        listCompraProd.add(compraHit);
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
        Iterator it = listCompraProd.iterator();
        for (int i = 0; it.hasNext(); i++) {
            CompraProdHit b = (CompraProdHit) it.next();
            b.cdProduto.setEditable(cdProduto.isEditable());
            if (b.pedidoProd != null) {
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
            b.vlUnitario.setEditable(vlUnitario.isEditable());
            b.vlUnitario.setPrefHeight(vlUnitario.getHeight());
            b.vlUnitario.setPrefWidth(vlUnitario.getWidth());
            b.vlUnitario.setLayoutX(vlUnitario.getLayoutX());
            b.vlUnitario.setLayoutY(LayoutY);
            b.vlUnitario.getStyleClass().addAll(this.vlUnitario.getStyleClass());
            if (b.pedidoProd != null) {
                b.vlUnitario.setEditable(false);
                b.vlUnitario.getStyleClass().addAll("numero_estatico");
            }
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
            addValidacao(b, i, listCompraProd.size());
        }
        painelProd.setPrefHeight(LayoutY + 10);
    }

    public void addValidacao(CompraProdHit compraProdHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(compraProdHit.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(compraProdHit.qtUnitario);
        FuncaoCampo.mascaraNumeroDecimal(compraProdHit.vlUnitario);

        compraProdHit.cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto(compraProdHit);
            }
        });

        compraProdHit.btnPesqProd.setOnAction((ActionEvent event) -> {
            abrirListaProduto(compraProdHit);
        });

        compraProdHit.qtUnitario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!compraProdHit.qtUnitario.getText().equals("")) {
                    Double valor = Double.parseDouble(compraProdHit.qtUnitario.getText());
                    if (valor <= 0) {
                        Alerta.AlertaError("Campo inválido", "Compra deve ser maior que 0.00!");
                        compraProdHit.qtUnitario.requestFocus();
                        return;
                    }
                    compraProdHit.qtUnitario.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
        });

        compraProdHit.vlUnitario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!compraProdHit.vlUnitario.getText().equals("")) {
                    Double valor = Double.parseDouble(compraProdHit.vlUnitario.getText());
                    if (valor <= 0) {
                        Alerta.AlertaError("Campo inválido", "Valor do produto deve ser maior que 0.00!");
                        compraProdHit.vlUnitario.requestFocus();
                        return;
                    }
                    compraProdHit.vlUnitario.setText(Numero.doubleToReal(valor, 2));
                }
                calculaTotalProd();
            }
        });

        compraProdHit.btnAdd.setOnAction((ActionEvent event) -> {
            CompraProdHit b = new CompraProdHit();
            listCompraProd.add(posicao + 1, b);
            atualizaLista();
        });

        compraProdHit.btnRem.setOnAction((ActionEvent event) -> {

            if (total == 1) {
                CompraProdHit b = new CompraProdHit();
                listCompraProd.add(b);
            }

            listCompraProd.remove(compraProdHit);
            calculaTotalProd();
            atualizaLista();
        });
    }

    public class CompraProdHit {

        PedidoProduto pedidoProd;
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
