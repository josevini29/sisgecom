package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.MovtoEstoque;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.CustomDate;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
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
import javafx.stage.Stage;

public class FrmConAjustEstoqFXML implements Initializable {

    @FXML
    TextField cdProduto;
    @FXML
    TextField dsProduto;
    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;
    @FXML
    ChoiceBox tpMovto;

    @FXML
    TableView<ClasseGenerica> gridMovto;

    ObservableList<ClasseGenerica> data;
    Dao dao = new Dao();

    public void iniciaTela() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdProduto);
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);
        TrataCombo.setValueComboTpMovtoEstoque(tpMovto, null);

        gridMovto = ContruirTableView.Criar(gridMovto, ClasseGenerica.class);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /*gridMovto.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                ok();
            }
        });*/
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
        /*if (!cdAjuste.getText().equals("")) {
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
        }*/

        try {
            Criteria criterio = new Criteria(new MovtoEstoque());
            criterio.AddAnd("cdProduto", cdProduto.getText(), false);
            criterio.AddAndBetweenDate("dtMovto", dtInicio.getText(), dtFim.getText());
            criterio.AddOrderByAsc("dtMovto");
            String sql = criterio.getWhereSql();

            ArrayList<Object> ajustes = dao.getAllWhere(new MovtoEstoque(), sql);
            for (Object obj : ajustes) {
                MovtoEstoque movto = (MovtoEstoque) obj;
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.setCdProduto(movto.getCdProduto());
                Produto prod = new Produto();
                prod.setCdProduto(movto.getCdProduto());
                dao.get(prod);
                classeGenerica.setDsProduto(prod.getDsProduto());
                classeGenerica.setDsMovto(EstoqueController.getDsEntraSaida(movto.getTpMovto()));
                Integer codMovto = null;
                if (movto.getCdMovCompVend() != null && movto.getTpMovto().equals("E")) {
                    codMovto = 1;
                } else if (movto.getCdMovCompVend() != null && movto.getTpMovto().equals("S")) {
                    codMovto = 2;
                } else if (movto.getCdAjuste() != null) {
                    codMovto = 3;
                }
                classeGenerica.setTpMovto(EstoqueController.getTipoMovtoEstoque(codMovto).getDsTpMovto());
                classeGenerica.setDtMovto(new CustomDate(movto.getDtMovto().getTime()));
                classeGenerica.setQtMovto(movto.getQtMovto());
                classeGenerica.setQtEstoq(movto.getQtEstoque());
                valoresArray.add(classeGenerica);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
        }

        data = FXCollections.observableArrayList(valoresArray);
        gridMovto.setItems(data);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public class ClasseGenerica extends MovtoEstoque {

        @Coluna(nome = "Cód. Prod.")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer cdProduto;
        @Coluna(nome = "Descrição do Produto")
        @Style(css = "-fx-alignment: CENTER-LEFT;")
        private String dsProduto;
        @Coluna(nome = "Data Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDate dtMovto;
        @Coluna(nome = "Entrada/Saída")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsMovto;
        @Coluna(nome = "Tipo de Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpMovto;
        @Coluna(nome = "Qt. Movto")
        @Style(css = "-fx-alignment: CENTER;")
        private Double qtMovto;
        @Coluna(nome = "Qt. Estoq.")
        @Style(css = "-fx-alignment: CENTER;")
        private Double qtEstoq;

        @Override
        public Integer getCdProduto() {
            return cdProduto;
        }

        @Override
        public void setCdProduto(Integer cdProduto) {
            this.cdProduto = cdProduto;
        }

        public String getDsProduto() {
            return dsProduto;
        }

        public void setDsProduto(String dsProduto) {
            this.dsProduto = dsProduto;
        }

        @Override
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

        @Override
        public String getTpMovto() {
            return tpMovto;
        }

        @Override
        public void setTpMovto(String tpMovto) {
            this.tpMovto = tpMovto;
        }

        @Override
        public Double getQtMovto() {
            return qtMovto;
        }

        @Override
        public void setQtMovto(Double qtMovto) {
            this.qtMovto = qtMovto;
        }

        public Double getQtEstoq() {
            return qtEstoq;
        }

        public void setQtEstoq(Double qtEstoq) {
            this.qtEstoq = qtEstoq;
        }

    }

}
