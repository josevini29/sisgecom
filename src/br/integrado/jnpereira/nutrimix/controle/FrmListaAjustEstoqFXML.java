/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.CustomDate;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.modelo.AjusteEstoque;
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

public class FrmListaAjustEstoqFXML implements Initializable {

    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;

    @FXML
    TextField cdAjuste;
    @FXML
    ChoiceBox tpAjuste;
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
                                dtFim.setDisable(true);
                                dtInicio.requestFocus();
                                dtFim.setDisable(false);
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
                                dtInicio.setDisable(true);
                                dtFim.requestFocus();
                                dtInicio.setDisable(false);
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
        getStage().setTitle("Lista de Ajustes de Estoque");
        String sql = "";
        if (!cdAjuste.getText().equals("")) {
            sql = " $AjusteEstoque.cdAjuste$ = " + cdAjuste.getText();
        }

        if (TrataCombo.getValueComboTpAjustEstoq(tpAjuste) != null) {
            if (!sql.equals("")) {
                sql += " AND";
            }
            sql += " $AjusteEstoque.tpAjuste$ = " + TrataCombo.getValueComboTpAjustEstoq(tpAjuste);
        }

        if (!dtInicio.getText().equals("")) {
            if (!sql.equals("")) {
                sql += " AND";
            }

            sql += " $AjusteEstoque.dtCadastro$ BETWEEN "
                    + "'" + dtInicio.getText() + " 00:00:00' AND '" + dtFim.getText() + " 23:59:59'";
        }

        if (!sql.equals("")) {
            sql = "WHERE " + sql;
        }

        sql = sql + " ORDER BY $AjusteEstoque.cdAjuste$ ASC";

        try {
            ArrayList<Object> ajustes = dao.getAllWhere(new AjusteEstoque(), sql);
            for (Object obj : ajustes) {
                AjusteEstoque ajus = (AjusteEstoque) obj;
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.setCdAjuste(ajus.getCdAjuste());
                classeGenerica.setTpAjuste(EstoqueController.getTipoAjusteEstoque(ajus.getTpAjuste()).getDsTpAjuste());
                classeGenerica.setDtMovto(new CustomDate(ajus.getDtCadastro().getTime()));
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
        TrataCombo.setValueComboTpAjustEstoq(tpAjuste, null);
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
        @Coluna(nome = "Tipo Ajuste")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpAjuste;
        @Coluna(nome = "Data Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDate dtMovto;

        public String getTpAjuste() {
            return tpAjuste;
        }

        public void setTpAjuste(String tpAjuste) {
            this.tpAjuste = tpAjuste;
        }

        public CustomDate getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDate dtMovto) {
            this.dtMovto = dtMovto;
        }

        public Integer getCdAjuste() {
            return cdAjuste;
        }

        public void setCdAjuste(Integer cdAjuste) {
            this.cdAjuste = cdAjuste;
        }

    }
}
