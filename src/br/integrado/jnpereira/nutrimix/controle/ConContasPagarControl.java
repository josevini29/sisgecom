/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.CustomDateNoTime;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
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

public class ConContasPagarControl implements Initializable {

    @FXML
    TextField cdForne;
    @FXML
    TextField dsForne;
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
    Fornecedor fornecedor;
    Pessoa pessoa;
    public Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdForne);
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
        cdForne.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoForne();
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
                        pessoa = null;
                        dsForne.setText("");
                        cdForne.requestFocus();
                        return;
                    }
                    pessoa = new Pessoa();
                    pessoa.setCdPessoa(fornecedor.getCdPessoa());
                    dao.get(pessoa);
                    dsForne.setText(pessoa.getDsPessoa());
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    fornecedor = null;
                    pessoa = null;
                    dsForne.setText("");
                    cdForne.requestFocus();
                }
            }
        } else {
            fornecedor = null;
            pessoa = null;
            dsForne.setText("");
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
            criteria.AddAnd("tpMovto", "S", false);
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
                        ArrayList<Object> array = dao.getAllWhere(new Fornecedor(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa());
                        Fornecedor fornecedor = (Fornecedor) array.get(0);
                        grid.setCdForne(fornecedor.getCdFornecedor());
                        grid.setDsForne(pessoa.getDsPessoa());
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
    public void abrirListaFornecedor() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Fornecedor(), true);
        if (valor != null) {
            cdForne.setText(valor);
            validaCodigoForne();
        }
    }

    public class GridConta {

        @Coluna(nome = "Nª Conta")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdConta;
        @Coluna(nome = "Dt. Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDateNoTime dtMovto;
        @Coluna(nome = "Cód. Forne.")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdForne;
        @Coluna(nome = "Descrição Fornecedor")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsForne;
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

        public Integer getCdForne() {
            return cdForne;
        }

        public void setCdForne(Integer cdForne) {
            this.cdForne = cdForne;
        }

        public String getDsForne() {
            return dsForne;
        }

        public void setDsForne(String dsForne) {
            this.dsForne = dsForne;
        }

        public String getTpConta() {
            return tpConta;
        }

        public void setTpConta(String tpConta) {
            this.tpConta = tpConta;
        }

        public String getTpMovto() {
            return tpMovto;
        }

        public void setTpMovto(String tpMovto) {
            this.tpMovto = tpMovto;
        }

        public String getTpSituacao() {
            return tpSituacao;
        }

        public void setTpSituacao(String tpSituacao) {
            this.tpSituacao = tpSituacao;
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

        public String getVlConta() {
            return vlConta;
        }

        public void setVlConta(String vlConta) {
            this.vlConta = vlConta;
        }

    }
}
