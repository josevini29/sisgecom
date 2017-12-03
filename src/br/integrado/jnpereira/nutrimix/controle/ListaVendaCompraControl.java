package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
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

public class ListaVendaCompraControl implements Initializable {

    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;

    @FXML
    TableView<ClasseGenerica> gridMovto;

    ObservableList<ClasseGenerica> data;
    Dao dao = new Dao();

    public Stage stage;
    public Object param;

    public void iniciaTela() {
        if (((String) param).equals("E")) {
            stage.setTitle("Consulta Compra");
        } else {
            stage.setTitle("Consulta Venda");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);

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
    public void gerarRelatorio() {
        atualizaGrid();
        Relatorio relatorio = new Relatorio();
        //relatorio.gerarRelatorioMovtoEstoque(gridMovto);
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
        try {
            Criteria criterio = new Criteria(new VendaCompra());
            criterio.AddAndBetweenDate("dtCadastro", dtInicio.getText(), dtFim.getText());

            criterio.AddAnd("tpMovto", (String) param, false);
            criterio.AddOrderByAsc("cdMovto");
            String sql = criterio.getWhereSql();

            ArrayList<Object> m = dao.getAllWhere(new VendaCompra(), sql);
            for (Object obj : m) {
                VendaCompra s = (VendaCompra) obj;
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.vendaCompra = s;
                classeGenerica.setCdMovto(s.getCdMovto());
                classeGenerica.setDsMovto(EstoqueControl.getDsEntraSaida(s.getTpMovto()));
                Integer codMovto = null;
                if (s.getTpMovto().equals("E")) {
                    codMovto = 1;
                } else if (s.getTpMovto().equals("S")) {
                    codMovto = 2;
                }
                classeGenerica.setTpMovto(EstoqueControl.getTipoMovtoEstoque(codMovto).getDsTpMovto());
                classeGenerica.setDtMovto(new CustomDate(s.getDtCadastro().getTime()));
                classeGenerica.setVlMovto(Numero.doubleToReal(s.getVlTotal(), 2));
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
        VendaCompra movto = new VendaCompra();
        movto.setCdMovto(classe.vendaCompra.getCdMovto());
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(stage, Tela.CON_VENDA_COMPRA, movto);
    }

    public class ClasseGenerica {

        VendaCompra vendaCompra;
        @Coluna(nome = "Cód. Prod.")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdMovto;
        @Coluna(nome = "Data Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDate dtMovto;
        @Coluna(nome = "Tipo de Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsMovto;
        @Coluna(nome = "Entrada/Saída")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpMovto;
        @Coluna(nome = "Vl. Total")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlMovto;

        public Integer getCdMovto() {
            return cdMovto;
        }

        public void setCdMovto(Integer cdMovto) {
            this.cdMovto = cdMovto;
        }

        public CustomDate getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDate dtMovto) {
            this.dtMovto = dtMovto;
        }

        public String getDsMovto() {
            return dsMovto;
        }

        public void setDsMovto(String dsMovto) {
            this.dsMovto = dsMovto;
        }

        public String getTpMovto() {
            return tpMovto;
        }

        public void setTpMovto(String tpMovto) {
            this.tpMovto = tpMovto;
        }

        public String getVlMovto() {
            return vlMovto;
        }

        public void setVlMovto(String vlMovto) {
            this.vlMovto = vlMovto;
        }

    }

}
