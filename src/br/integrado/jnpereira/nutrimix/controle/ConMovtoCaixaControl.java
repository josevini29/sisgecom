package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.AjusteCaixa;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Despesa;
import br.integrado.jnpereira.nutrimix.modelo.FormaPagto;
import br.integrado.jnpereira.nutrimix.modelo.MovtoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.relatorio.Relatorio;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.CustomDate;
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

public class ConMovtoCaixaControl implements Initializable {

    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;
    @FXML
    ChoiceBox tpMovto;
    @FXML
    ChoiceBox tpForPagto;
    @FXML
    TextField cdFechamento;

    @FXML
    TableView<ClasseGenerica> gridMovto;

    ObservableList<ClasseGenerica> data;
    Dao dao = new Dao();

    public Stage stage;
    public Object param;

    public void iniciaTela() {
        if (param != null) {
            Param parametro = (Param) param;
            cdFechamento.setText(parametro.cdFechamento.toString());
            TrataCombo.setValueComboTpFormaPagto(tpForPagto, parametro.cdForPagto);
            atualizaGrid();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);
        TrataCombo.setValueComboTpMovtoCaixa(tpMovto, null);
        TrataCombo.setValueComboTpFormaPagto(tpForPagto, null);

        gridMovto = ContruirTableView.Criar(gridMovto, ClasseGenerica.class);

        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        gridMovto.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                abrirTelaConsulta();
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
    }

    @FXML
    public void atualizaGrid() {
        ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();
        if (!dtInicio.getText().equals("") && dtFim.getText().equals("")) {
            dtFim.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        }
        if (dtInicio.getText().equals("") && !dtFim.getText().equals("")) {
            Alerta.AlertaError("Não permitido", "Data de início é obrigatória.");
            dtInicio.requestFocus();
            return;
        }
        Integer vCdTpMovto = TrataCombo.getValueComboTpMovtoCaixa(tpMovto);
        try {
            Criteria criterio = new Criteria(new MovtoCaixa());
            criterio.AddAndBetweenDate("dtMovto", dtInicio.getText(), dtFim.getText());
            criterio.AddAnd("cdFechamento", cdFechamento.getText(), false);
            criterio.AddAnd("cdFormaPagto", TrataCombo.getValueComboTpFormaPagto(tpForPagto), false);
            criterio.AddOrderByAsc("dtMovto");
            String sql = criterio.getWhereSql();

            ArrayList<Object> ajustes = dao.getAllWhere(new MovtoCaixa(), sql);
            for (Object obj : ajustes) {
                MovtoCaixa movto = (MovtoCaixa) obj;
                int vTpMovto;
                if (movto.getCdAjuste() != null) {
                    vTpMovto = 4;
                } else {
                    ContasPagarReceber conta = new ContasPagarReceber();
                    conta.setCdConta(movto.getCdConta());
                    dao.get(conta);
                    if (conta.getCdDespesa() != null) {
                        vTpMovto = 3;
                    } else {
                        VendaCompra vendaCompra = new VendaCompra();
                        vendaCompra.setCdMovto(conta.getCdMovto());
                        dao.get(vendaCompra);
                        if (vendaCompra.getTpMovto().equals("S")) {
                            vTpMovto = 2;
                        } else {
                            vTpMovto = 1;
                        }
                    }
                }
                if (vCdTpMovto != null) {
                    if (vTpMovto != vCdTpMovto) {
                        continue;
                    }
                }
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.movto = movto;
                classeGenerica.setCdMovtoCaixa(movto.getCdMovtoCaixa());
                if (vTpMovto == 4) {
                    AjusteCaixa ajust = new AjusteCaixa();
                    ajust.setCdAjuste(movto.getCdAjuste());
                    dao.get(ajust);
                    int vTpAjuste = Integer.parseInt(ajust.getTpAjuste()) - 1;
                    classeGenerica.setTpMovtoCaixa(CaixaControl.getAllTipoAjuste().get(vTpAjuste).getDsTpAjuste());
                } else {
                    classeGenerica.setTpMovtoCaixa(EstoqueControl.getAllTipoMovtoCaixa().get(vTpMovto).getDsTpMovto());
                }
                classeGenerica.setTpMovto(EstoqueControl.getDsEntraSaida(movto.getTpMovtoCaixa()));
                classeGenerica.setDtMovto(new CustomDate(movto.getDtMovto().getTime()));
                classeGenerica.setCdFechamento(movto.getCdFechamento());
                classeGenerica.setCdForPagto(movto.getCdFormaPagto());
                FormaPagto forma = new FormaPagto();
                forma.setCdFormaPagto(movto.getCdFormaPagto());
                dao.get(forma);
                classeGenerica.setDsForPagto(forma.getDsFormaPagto());
                classeGenerica.setVlMovto(Numero.arredondaDecimal(movto.getVlMovto(), 2));
                valoresArray.add(classeGenerica);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
        }

        data = FXCollections.observableArrayList(valoresArray);
        gridMovto.setItems(data);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void abrirTelaConsulta() {
        ClasseGenerica classe = gridMovto.getSelectionModel().getSelectedItem();
        if (classe == null) {
            Alerta.AlertaWarning("Aviso!", "Selecione um item.");
            return;
        }

        MovtoCaixa movto = classe.movto;
        try {
            if (movto.getCdAjuste() != null) {
                AjusteCaixa aj = new AjusteCaixa();
                aj.setCdAjuste(movto.getCdAjuste());
                Tela tela = new Tela();
                tela.abrirTelaModalComParam(stage, Tela.AJUSTE_CAIXA, aj);
            } else {
                ContasPagarReceber conta = new ContasPagarReceber();
                conta.setCdConta(movto.getCdConta());
                dao.get(conta);

                if (conta.getCdDespesa() != null) {
                    Despesa despesa = new Despesa();
                    despesa.setCdDespesa(conta.getCdDespesa());
                    Tela tela = new Tela();
                    tela.abrirTelaModalComParam(stage, Tela.CAD_DESPESA, despesa);
                } else {
                    VendaCompra vendaCompra = new VendaCompra();
                    vendaCompra.setCdMovto(conta.getCdMovto());
                    Tela tela = new Tela();
                    tela.abrirTelaModalComParam(stage, Tela.CON_VENDA_COMPRA, vendaCompra);
                }
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.toString());
        }
    }
    
    @FXML
    public void gerarRelatorio(){
        atualizaGrid();
        Relatorio relatorio = new Relatorio();
        relatorio.gerarRelatorioMovtoCaixa(gridMovto);
    }

    public class ClasseGenerica {

        @Coluna(nome = "Cód. Movto.")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdMovtoCaixa;
        @Coluna(nome = "Tipo Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpMovtoCaixa;
        @Coluna(nome = "Entrada/Saída")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpMovto;
        @Coluna(nome = "Data Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDate dtMovto;
        @Coluna(nome = "Cód. Fechamento")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdFechamento;
        @Coluna(nome = "Cód.Forma.Pagto")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdForPagto;
        @Coluna(nome = "Forma Pagamento")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsForPagto;
        @Coluna(nome = "Vl. Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private Double vlMovto;
        public MovtoCaixa movto;

        public Integer getCdMovtoCaixa() {
            return cdMovtoCaixa;
        }

        public void setCdMovtoCaixa(Integer cdMovtoCaixa) {
            this.cdMovtoCaixa = cdMovtoCaixa;
        }

        public String getTpMovtoCaixa() {
            return tpMovtoCaixa;
        }

        public void setTpMovtoCaixa(String tpMovtoCaixa) {
            this.tpMovtoCaixa = tpMovtoCaixa;
        }

        public String getTpMovto() {
            return tpMovto;
        }

        public void setTpMovto(String tpMovto) {
            this.tpMovto = tpMovto;
        }

        public CustomDate getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDate dtMovto) {
            this.dtMovto = dtMovto;
        }

        public Integer getCdFechamento() {
            return cdFechamento;
        }

        public void setCdFechamento(Integer cdFechamento) {
            this.cdFechamento = cdFechamento;
        }

        public Integer getCdForPagto() {
            return cdForPagto;
        }

        public void setCdForPagto(Integer cdForPagto) {
            this.cdForPagto = cdForPagto;
        }

        public String getDsForPagto() {
            return dsForPagto;
        }

        public void setDsForPagto(String dsForPagto) {
            this.dsForPagto = dsForPagto;
        }

        public Double getVlMovto() {
            return vlMovto;
        }

        public void setVlMovto(Double vlMovto) {
            this.vlMovto = vlMovto;
        }

    }

    public static class Param {

        private Integer cdFechamento;
        private Integer cdForPagto;

        public Integer getCdFechamento() {
            return cdFechamento;
        }

        public void setCdFechamento(Integer cdFechamento) {
            this.cdFechamento = cdFechamento;
        }

        public Integer getCdForPagto() {
            return cdForPagto;
        }

        public void setCdForPagto(Integer cdForPagto) {
            this.cdForPagto = cdForPagto;
        }

    }
}
