/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.AjusteCaixa;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.CustomDate;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListaAjusteCaixaControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;

    @FXML
    TextField cdAjuste;
    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;
    @FXML
    TableView<ClasseGenerica> gridGenerica;

    private Stage stage;
    private String dsRetorno = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gridGenerica = ContruirTableView.Criar(gridGenerica, ClasseGenerica.class);
        gridGenerica.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        gridGenerica.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                ok();
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

    public void atualizaGrid() {
        ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();
        getStage().setTitle("Lista de Ajuste Caixa");

        if (!dtInicio.getText().equals("") && dtFim.getText().equals("")) {
            dtFim.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        }
        if (dtInicio.getText().equals("") && !dtFim.getText().equals("")) {
            Alerta.AlertaError("Não permitido", "Data de início é obrigatória.");
            dtInicio.requestFocus();
            return;
        }

        try {
            Criteria criterio = new Criteria(new AjusteCaixa());
            criterio.AddAnd("cdAjuste", cdAjuste.getText(), false);
            criterio.AddAndBetweenDate("dtCadastro", dtInicio.getText(), dtFim.getText());

            ArrayList<Object> fech = dao.getAllWhere(new AjusteCaixa(), criterio.getWhereSql());
            for (Object obj : fech) {
                AjusteCaixa ajuste = (AjusteCaixa) obj;
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.setCdAjuste(ajuste.getCdAjuste());
                classeGenerica.setDtMovto(new CustomDate(ajuste.getDtCadastro().getTime()));
                classeGenerica.setTpAjuste(CaixaControl.getAllTipoAjuste().get(Integer.parseInt(ajuste.getTpAjuste()) - 1).getDsTpAjuste());
                classeGenerica.setVlAjuste(Numero.arredondaDecimal(ajuste.getVlAjuste(), 2));
                valoresArray.add(classeGenerica);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
        }

        data = FXCollections.observableArrayList(valoresArray);
        gridGenerica.setItems(data);
        gridGenerica.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void ok() {
        ClasseGenerica generica = gridGenerica.getSelectionModel().getSelectedItem();
        if (generica == null) {
            Alerta.AlertaInfo("Aviso!", "Selecione um item.");
            return;
        }
        setDsRetorno(generica.getCdAjuste().toString());
        stage.close();
    }

    @FXML
    public void cancelar() {
        stage.close();
    }

    @FXML
    public void pesquisar() {
        atualizaGrid();
    }

    public void iniciaTela() {
        FuncaoCampo.mascaraNumeroInteiro(cdAjuste);
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);
        atualizaGrid();
    }

    public String getDsRetorno() {
        return dsRetorno;
    }

    public void setDsRetorno(String dsRetorno) {
        this.dsRetorno = dsRetorno;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class ClasseGenerica {

        @Coluna(nome = "Código")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Integer cdAjuste;
        @Coluna(nome = "Dt. Abertura")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDate dtMovto;
        @Coluna(nome = "Tipo Ajuste")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpAjuste;
        @Coluna(nome = "Vl. Ajuste")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double vlAjuste;

        public Integer getCdAjuste() {
            return cdAjuste;
        }

        public void setCdAjuste(Integer cdAjuste) {
            this.cdAjuste = cdAjuste;
        }

        public CustomDate getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDate dtMovto) {
            this.dtMovto = dtMovto;
        }

        public String getTpAjuste() {
            return tpAjuste;
        }

        public void setTpAjuste(String tpAjuste) {
            this.tpAjuste = tpAjuste;
        }

        public Double getVlAjuste() {
            return vlAjuste;
        }

        public void setVlAjuste(Double vlAjuste) {
            this.vlAjuste = vlAjuste;
        }

    }
}
