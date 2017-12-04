/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Cliente;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.modelo.PedidoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompraProduto;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jose Vinicius
 */
public class ConVendaCompraControl implements Initializable {

    @FXML
    Label lblCdPessoa;
    @FXML
    Label lblDsPessoa;
    @FXML
    Label lblNotaFiscal;
    @FXML
    Label lblSerie;
    @FXML
    Label lblDtEmissao;
    @FXML
    Label lblPedido;
    @FXML
    TextField cdMovto;
    @FXML
    TextField cdPessoa;
    @FXML
    TextField dsPessoa;
    @FXML
    TextField nrCpfCnpj;
    @FXML
    TextField nrNotaFiscal;
    @FXML
    TextField cdSerie;
    @FXML
    TextField dtEmissao;
    @FXML
    TextField cdPedido;
    @FXML
    Label lblCadastro;
    @FXML
    Label lblCancelado;

    @FXML
    TableView<ClasseGenerica> gridProduto;

    @FXML
    TextField cdCondPagto;
    @FXML
    TextField dsCondPagto;
    @FXML
    TextField vlDesconto;
    @FXML
    TextField vlAdicional;
    @FXML
    TextField vlFrete;
    @FXML
    TextField vlTotalProduto;
    @FXML
    TextField vlTotalMovto;

    public Stage stage;
    public Object param;
    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;
    VendaCompra movto;
    ContasPagarReceber conta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridProduto = ContruirTableView.Criar(gridProduto, ClasseGenerica.class);
        gridProduto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void iniciaTela() {
        if (param == null) {
            Alerta.AlertaError("Erro!", "Parametro esperado está Null");
            return;
        }
        try {
            movto = (VendaCompra) param;
            dao.get(movto);
            cdMovto.setText(movto.getCdMovto().toString());
            lblCadastro.setText(Numero.getCadastroComHora(movto.getCdUserCad(), movto.getDtCadastro()));
            if (movto.getTpMovto().equals("S")) { //Venda
                stage.setTitle("Consulta de Venda");
                if (movto.getInCancelado()) {
                    lblCancelado.setText("Venda Cancelada.");
                }
                nrNotaFiscal.setVisible(false);
                cdSerie.setVisible(false);
                dtEmissao.setVisible(false);
                cdPedido.setVisible(false);
                lblNotaFiscal.setVisible(false);
                lblSerie.setVisible(false);
                lblDtEmissao.setVisible(false);
                lblPedido.setVisible(false);
                lblCdPessoa.setText("Cód.Cliente");
                lblDsPessoa.setText("Descrição Cliente");
                if (movto.getCdPessoa() != null) {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setCdPessoa(movto.getCdPessoa());
                    dao.get(pessoa);
                    Cliente cliente = (Cliente) dao.getAllWhere(new Cliente(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa()).get(0);
                    cdPessoa.setText(cliente.getCdCliente().toString());
                    dsPessoa.setText(pessoa.getDsPessoa());
                    if (pessoa.getTpPessoa().equals("F")) {
                        nrCpfCnpj.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    } else {
                        nrCpfCnpj.setText(Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()));
                    }
                }
            } else { //Compra
                stage.setTitle("Consulta de Compra");
                if (movto.getInCancelado()) {
                    lblCancelado.setText("Compra Cancelada.");
                }
                nrNotaFiscal.setText(trataNulo(movto.getNrNota()));
                cdSerie.setText(trataNulo(movto.getCdSerie()));
                dtEmissao.setText(Data.AmericaToBrasilSemHora(movto.getDtEmissao()));
                cdPedido.setText(trataNulo(movto.getCdPedido()));
                lblCdPessoa.setText("Cód.Forne");
                lblDsPessoa.setText("Descrição Fornecedor");
                if (movto.getCdPessoa() != null) {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setCdPessoa(movto.getCdPessoa());
                    dao.get(pessoa);
                    Fornecedor fornecedor = (Fornecedor) dao.getAllWhere(new Fornecedor(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa()).get(0);
                    cdPessoa.setText(fornecedor.getCdFornecedor().toString());
                    dsPessoa.setText(pessoa.getDsPessoa());
                    if (pessoa.getTpPessoa().equals("F")) {
                        nrCpfCnpj.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    } else {
                        nrCpfCnpj.setText(Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()));
                    }
                }
            }

            try {
                conta = (ContasPagarReceber) dao.getAllWhere(new ContasPagarReceber(), "WHERE $cdMovto$ = " + movto.getCdMovto()).get(0);
                cdCondPagto.setText(conta.getCdMovto().toString());
                CondicaoPagto condicao = new CondicaoPagto();
                condicao.setCdCondicao(conta.getCdCondicao());
                dao.get(condicao);
                dsCondPagto.setText(condicao.getDsCondicao());
            } catch (Exception ex) {
            }

            vlDesconto.setText(Numero.doubleToReal(movto.getVlDesconto(), 2));
            vlAdicional.setText(Numero.doubleToReal(movto.getVlAdicional(), 2));
            vlFrete.setText(Numero.doubleToReal(movto.getVlFrete(), 2));
            vlTotalMovto.setText(Numero.doubleToReal(movto.getVlTotal(), 2));

            double vVlTotalProd = 0.00;
            ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();
            String where = "WHERE $cdMovto$ = " + movto.getCdMovto() + " ORDER BY $cdProduto$ ASC";
            ArrayList<Object> prods = dao.getAllWhere(new VendaCompraProduto(), where);
            for (Object obj : prods) {
                VendaCompraProduto vendaProd = (VendaCompraProduto) obj;
                ClasseGenerica classe = new ClasseGenerica();
                classe.setCdProduto(vendaProd.getCdProduto());
                Produto produto = new Produto();
                produto.setCdProduto(vendaProd.getCdProduto());
                dao.get(produto);
                classe.setDsProduto(produto.getDsProduto());
                classe.setQtProduto(Numero.arredondaDecimal(vendaProd.getQtUnitario(), 2));
                classe.setVlProduto(Numero.arredondaDecimal(vendaProd.getVlUnitario(), 2));
                classe.setVlTotal(Numero.arredondaDecimal(vendaProd.getQtUnitario() * vendaProd.getVlUnitario(), 2));
                vVlTotalProd += (vendaProd.getQtUnitario() * vendaProd.getVlUnitario());
                valoresArray.add(classe);
            }
            vlTotalProduto.setText(Numero.doubleToReal(vVlTotalProd, 2));

            data = FXCollections.observableArrayList(valoresArray);
            gridProduto.setItems(data);
            gridProduto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.toString());
        }
    }

    private String trataNulo(String valor) {
        if (valor == null) {
            return "";
        }
        return valor;
    }

    private String trataNulo(Integer valor) {
        if (valor == null) {
            return "";
        }
        return valor.toString();
    }

    @FXML
    public void visualizarPagamento() {
        Tela tela = new Tela();
        if (conta != null) {
            tela.abrirTelaModalComParam(stage, Tela.PAG_PARCELA, conta);
        } else {
            Alerta.AlertaError("Erro!", "Conta já excluida!");
        }
    }

    @FXML
    public void excluirMovto() {
        if (movto.getInCancelado() == true) {
            Alerta.AlertaError("Negado!", "Movimento já cancelado!");
            return;
        }
        if (Alerta.AlertaConfirmation("Confirmação", "Deseja cancelar está operaçao? não podera ser estornada.")) {
            try {
                dao.autoCommit(false);
                movto.setInCancelado(true);
                dao.update(movto);
                CaixaControl caixa = new CaixaControl();
                caixa.excluirConta(conta);

                ArrayList<Object> array = dao.getAllWhere(new MovtoEstoque(), "WHERE $cdMovCompVend$ = " + movto.getCdMovto());
                for (Object obj : array) {
                    MovtoEstoque movtoEstoque = (MovtoEstoque) obj;
                    movtoEstoque.setInCancelado(true);
                    EstoqueControl estq = new EstoqueControl();
                    estq.geraMovtoEstoque(movtoEstoque);
                }

                if (movto.getCdPedido() != null) {
                    Pedido pedido = new Pedido();
                    pedido.setCdPedido(movto.getCdPedido());
                    dao.get(pedido);
                    for (ClasseGenerica classe : data) {
                        ArrayList<Object> prod = dao.getAllWhere(new PedidoProduto(), "WHERE $cdPedido$ = " + pedido.getCdPedido() + " AND $cdProduto$ = " + classe.getCdProduto());
                        if (prod.size() > 0) {
                            PedidoProduto pedItem = (PedidoProduto) prod.get(0);
                            Produto produto = new Produto();
                            produto.setCdProduto(pedItem.getCdProduto());
                            dao.get(produto);
                            double vVl = classe.getQtProduto() / produto.getQtConversao();
                            if (vVl > pedItem.getQtEntregue()) {
                                vVl = pedItem.getQtEntregue();
                            }
                            pedItem.setQtEntregue(pedItem.getQtEntregue() - vVl);
                            dao.update(pedItem);
                        }
                    }
                    if (pedido.getStPedido().equals("2")) {
                        pedido.setStPedido("1");
                        dao.update(pedido);
                    }
                }
                dao.commit();
                iniciaTela();
            } catch (Exception ex) {
                dao.rollback();
                iniciaTela();
                Alerta.AlertaError("Erro!", ex.getMessage());
            }
        }
    }

    public class ClasseGenerica {

        @Coluna(nome = "Cód. Produto")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdProduto;
        @Coluna(nome = "Desc. Produto")
        @Style(css = "-fx-alignment: LEFT-CENTER;")
        private String dsProduto;
        @Coluna(nome = "Qt. Produto")
        @Style(css = "-fx-alignment: CENTER;")
        private Double qtProduto;
        @Coluna(nome = "Vl. Produto")
        @Style(css = "-fx-alignment: CENTER;")
        private Double vlProduto;
        @Coluna(nome = "Total Produto")
        @Style(css = "-fx-alignment: CENTER;")
        private Double vlTotal;

        public Integer getCdProduto() {
            return cdProduto;
        }

        public void setCdProduto(Integer cdProduto) {
            this.cdProduto = cdProduto;
        }

        public String getDsProduto() {
            return dsProduto;
        }

        public void setDsProduto(String dsProduto) {
            this.dsProduto = dsProduto;
        }

        public Double getQtProduto() {
            return qtProduto;
        }

        public void setQtProduto(Double qtProduto) {
            this.qtProduto = qtProduto;
        }

        public Double getVlProduto() {
            return vlProduto;
        }

        public void setVlProduto(Double vlProduto) {
            this.vlProduto = vlProduto;
        }

        public Double getVlTotal() {
            return vlTotal;
        }

        public void setVlTotal(Double vlTotal) {
            this.vlTotal = vlTotal;
        }

    }

}
