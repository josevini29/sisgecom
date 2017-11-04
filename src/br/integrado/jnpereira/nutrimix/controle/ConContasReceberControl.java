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
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.CustomDateNoTime;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConContasReceberControl implements Initializable {

    @FXML
    TextField cdCliente;
    @FXML
    TextField dsCliente;
    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;
    @FXML
    ChoiceBox tpSituacao;
    @FXML
    TableView<GridConta> gridConta;

    ObservableList<GridConta> data;

    Dao dao = new Dao();
    Cliente cliente;
    Pessoa pessoa;
    public Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdCliente);
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);
        TrataCombo.setValueComboSitConta(tpSituacao, null);
        gridConta.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Tela tela = new Tela();
                ContasPagarReceber conta = new ContasPagarReceber();
                conta.setCdConta(gridConta.getSelectionModel().getSelectedItem().getCdConta());
                tela.abrirTelaModalComParam(stage, Tela.PAG_PARCELA, conta);
            }
        });
        cdCliente.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCliente();
            }
        });
        dtInicio.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtInicio.getText().equals("")) {
                    try {
                        Data.autoComplete(dtInicio);
                        Date dataInicio = Data.StringToDate(dtInicio.getText());
                        if (dataInicio.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data de início não pode ser maior que a data atual.");
                            dtInicio.requestFocus();
                            return;
                        }

                        if (!dtFim.getText().equals("")) {
                            Date dataFim = Data.StringToDate(dtFim.getText());
                            if (dataInicio.after(dataFim)) {
                                Alerta.AlertaError("Campo inválido", "Data de início não pode ser maior que a data fim.");
                                if (!dtFim.isFocused()) {
                                    dtInicio.requestFocus();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtInicio.requestFocus();
                    }
                }
            }
        });

        dtFim.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtFim.getText().equals("")) {
                    try {
                        Data.autoComplete(dtFim);
                        Date dataFim = Data.StringToDate(dtFim.getText());
                        if (dataFim.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data de fim não pode ser maior que a data atual.");
                            dtFim.requestFocus();
                            return;
                        }

                        if (!dtInicio.getText().equals("")) {
                            Date dataInicio = Data.StringToDate(dtInicio.getText());
                            if (dataInicio.after(dataFim)) {
                                Alerta.AlertaError("Campo inválido", "Data de fim não pode ser menor que a data início.");
                                if (!dtInicio.isFocused()) {
                                    dtFim.requestFocus();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtFim.requestFocus();
                    }
                }
            }
        });
        gridConta = ContruirTableView.Criar(gridConta, GridConta.class);
        gridConta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void iniciaTela() {

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
                        pessoa = null;
                        dsCliente.setText("");
                        cdCliente.requestFocus();
                        return;
                    }
                    pessoa = new Pessoa();
                    pessoa.setCdPessoa(cliente.getCdPessoa());
                    dao.get(pessoa);
                    dsCliente.setText(pessoa.getDsPessoa());
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    cliente = null;
                    pessoa = null;
                    dsCliente.setText("");
                    cdCliente.requestFocus();
                }
            }
        } else {
            cliente = null;
            pessoa = null;
            dsCliente.setText("");
        }
    }

    @FXML
    public void atualizaGrid() {
        if (!dtInicio.getText().equals("") && dtFim.getText().equals("")) {
            dtFim.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        }
        if (dtInicio.getText().equals("") && !dtFim.getText().equals("")) {
            Alerta.AlertaError("Não permitido", "Data de início é obrigatória.");
            dtInicio.requestFocus();
            return;
        }
        ArrayList<GridConta> contaArray = new ArrayList<>();
        try {
            Criteria criteria = new Criteria(new ContasPagarReceber());
            criteria.AddAnd("tpMovto", "E", false);
            criteria.AddAnd("stConta", TrataCombo.getValueComboSitConta(tpSituacao), false);
            criteria.AddAndBetweenDate("dtMovto", dtInicio.getText(), dtFim.getText());
            criteria.AddOrderByAsc("dtMovto");
            ArrayList<Object> contas = dao.getAllWhere(new ContasPagarReceber(), criteria.getWhereSql());
            for (Object obj : contas) {
                ContasPagarReceber conta = (ContasPagarReceber) obj;
                VendaCompra movto = null;
                if (pessoa != null) {
                    if (conta.getCdMovto() == null) {
                        continue;
                    }
                    movto = new VendaCompra();
                    movto.setCdMovto(conta.getCdMovto());
                    dao.get(movto);
                    if (movto.getCdPessoa() == null) {
                        continue;
                    }
                    if (!movto.getCdPessoa().equals(pessoa.getCdPessoa())) {
                        continue;
                    }
                }
                GridConta grid = new GridConta();
                grid.setCdConta(conta.getCdConta());
                grid.setTpSituacao(conta.getStConta());
                grid.setDtMovto(new CustomDateNoTime(conta.getDtMovto().getTime()));
                if (conta.getCdDespesa() != null) {
                    grid.setTpConta(TrataCombo.getTpConta(3));
                } else if (conta.getCdMovto() != null && conta.getTpMovto().equals("E")) {
                    grid.setTpConta(TrataCombo.getTpConta(2));
                } else if (conta.getCdMovto() != null && conta.getTpMovto().equals("S")) {
                    grid.setTpConta(TrataCombo.getTpConta(1));
                }
                if (conta.getCdMovto() != null) {
                    if (movto == null) {
                        movto = new VendaCompra();
                        movto.setCdMovto(conta.getCdMovto());
                        dao.get(movto);
                    }
                    if (movto.getCdPessoa() != null) {
                        Pessoa pessoa = new Pessoa();
                        pessoa.setCdPessoa(movto.getCdPessoa());
                        dao.get(pessoa);
                        ArrayList<Object> array = dao.getAllWhere(new Cliente(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa());
                        Cliente cliente = (Cliente) array.get(0);
                        grid.setCdCliente(cliente.getCdCliente());
                        grid.setDsCliente(pessoa.getDsPessoa());
                    }
                }
                CondicaoPagto condicao = new CondicaoPagto();
                condicao.setCdCondicao(conta.getCdCondicao());
                dao.get(condicao);
                grid.setCdCondicao(condicao.getCdCondicao());
                grid.setDsCondicao(condicao.getDsCondicao());
                grid.setTpMovto(TrataCombo.getTpEntradaSaida(conta.getTpMovto()));
                grid.setTpSituacao(TrataCombo.getTpSitConta(Integer.parseInt(conta.getStConta())));
                grid.setVlConta(Numero.doubleToReal(conta.getVlConta(), 2));
                contaArray.add(grid);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.getMessage());
        }

        data = FXCollections.observableArrayList(contaArray);

        gridConta.setItems(data);

        gridConta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void abrirListaCliente() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Cliente(), true);
        if (valor != null) {
            cdCliente.setText(valor);
            validaCodigoCliente();
        }
    }

    public class GridConta {

        @Coluna(nome = "Nª Conta")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdConta;
        @Coluna(nome = "Dt. Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDateNoTime dtMovto;
        @Coluna(nome = "Cód. Cliente")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdCliente;
        @Coluna(nome = "Descrição Cliente")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsCliente;
        @Coluna(nome = "Tipo Conta")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpConta;
        @Coluna(nome = "Saída/Entrada")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpMovto;
        @Coluna(nome = "Situação")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpSituacao;
        @Coluna(nome = "Cód. Condição")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdCondicao;
        @Coluna(nome = "Desc. Condição")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsCondicao;
        @Coluna(nome = "Vl. Total")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlConta;

        public Integer getCdConta() {
            return cdConta;
        }

        public void setCdConta(Integer cdConta) {
            this.cdConta = cdConta;
        }

        public CustomDateNoTime getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDateNoTime dtMovto) {
            this.dtMovto = dtMovto;
        }

        public Integer getCdCliente() {
            return cdCliente;
        }

        public void setCdCliente(Integer cdCliente) {
            this.cdCliente = cdCliente;
        }

        public String getDsCliente() {
            return dsCliente;
        }

        public void setDsCliente(String dsCliente) {
            this.dsCliente = dsCliente;
        }

        public String getTpConta() {
            return tpConta;
        }

        public void setTpConta(String tpConta) {
            this.tpConta = tpConta;
        }

        public String getTpSituacao() {
            return tpSituacao;
        }

        public void setTpSituacao(String tpSituacao) {
            this.tpSituacao = tpSituacao;
        }

        public String getVlConta() {
            return vlConta;
        }

        public void setVlConta(String vlConta) {
            this.vlConta = vlConta;
        }

        public String getTpMovto() {
            return tpMovto;
        }

        public void setTpMovto(String tpMovto) {
            this.tpMovto = tpMovto;
        }

        public Integer getCdCondicao() {
            return cdCondicao;
        }

        public void setCdCondicao(Integer cdCondicao) {
            this.cdCondicao = cdCondicao;
        }

        public String getDsCondicao() {
            return dsCondicao;
        }

        public void setDsCondicao(String dsCondicao) {
            this.dsCondicao = dsCondicao;
        }

    }
}
