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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ConMovtoEstoqueControl implements Initializable {

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
    CheckBox inMovCancel;

    @FXML
    TableView<ClasseGenerica> gridMovto;

    ObservableList<ClasseGenerica> data;
    Dao dao = new Dao();

    public void iniciaTela() {

    }

    @FXML
    public void abrirListaProduto() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            cdProduto.setText(valor);
            validaCodigoProduto();
        }
    }

    private void validaCodigoProduto() {
        if (!cdProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(cdProduto.getText()));
                dao.get(prod);

                if (!prod.getInAtivo()) {
                    Alerta.AlertaError("Inválido", "Produto está inativo.");
                    cdProduto.requestFocus();
                    return;
                }

                dsProduto.setText(prod.getDsProduto());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Produto não encontrado!");
                cdProduto.requestFocus();
            }
        } else {
            dsProduto.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdProduto);
        FuncaoCampo.mascaraData(dtInicio);
        FuncaoCampo.mascaraData(dtFim);
        TrataCombo.setValueComboTpMovtoEstoque(tpMovto, null);

        gridMovto = ContruirTableView.Criar(gridMovto, ClasseGenerica.class);
        
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto();
            }
        });

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
        if (!dtInicio.getText().equals("") && dtFim.getText().equals("")) {
            dtFim.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        }
        if (dtInicio.getText().equals("") && !dtFim.getText().equals("")) {
            Alerta.AlertaError("Não permitido", "Data de início é obrigatória.");
            dtInicio.requestFocus();
            return;
        }
        try {
            Criteria criterio = new Criteria(new MovtoEstoque());
            criterio.AddAnd("cdProduto", cdProduto.getText(), false);
            if (!inMovCancel.isSelected()) {
                criterio.AddAnd("inCancelado", false, false);
            }
            criterio.AddAndBetweenDate("dtMovto", dtInicio.getText(), dtFim.getText());

            if (TrataCombo.getValueComboTpMovtoEstoque(tpMovto) != null) {
                if (TrataCombo.getValueComboTpMovtoEstoque(tpMovto) == 1) {
                    criterio.AddAnd("tpMovto", "E", false);
                    criterio.AddAnd("cdAjuste", null, true);
                } else if (TrataCombo.getValueComboTpMovtoEstoque(tpMovto) == 2) {
                    criterio.AddAnd("tpMovto", "S", false);
                    criterio.AddAnd("cdAjuste", null, true);
                } else if (TrataCombo.getValueComboTpMovtoEstoque(tpMovto) == 3) {
                    criterio.AddAnd("cdMovCompVend", null, true);
                }
            }
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
                classeGenerica.setDsMovto(EstoqueControl.getDsEntraSaida(movto.getTpMovto()));
                Integer codMovto = null;
                if (movto.getCdMovCompVend() != null && movto.getTpMovto().equals("E")) {
                    codMovto = 1;
                } else if (movto.getCdMovCompVend() != null && movto.getTpMovto().equals("S")) {
                    codMovto = 2;
                } else if (movto.getCdAjuste() != null) {
                    codMovto = 3;
                }
                classeGenerica.setTpMovto(EstoqueControl.getTipoMovtoEstoque(codMovto).getDsTpMovto());
                classeGenerica.setDtMovto(new CustomDate(movto.getDtMovto().getTime()));
                classeGenerica.setQtMovto(movto.getQtMovto());
                classeGenerica.setQtEstoq(movto.getQtEstoque());
                classeGenerica.setVlCustoMedio(movto.getVlCustoMedio());
                classeGenerica.setInCancel(movto.getInCancelado() ? "Sim" : "Não");
                valoresArray.add(classeGenerica);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
        }

        data = FXCollections.observableArrayList(valoresArray);
        gridMovto.setItems(data);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public class ClasseGenerica extends MovtoEstoque{

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
        @Coluna(nome = "Qt. Estoque")
        @Style(css = "-fx-alignment: CENTER;")
        private Double qtEstoq;
        @Coluna(nome = "Vl. Custo Médio")
        @Style(css = "-fx-alignment: CENTER;")
        private Double vlCustoMedio;
        @Coluna(nome = "Cancelado")
        @Style(css = "-fx-alignment: CENTER;")
        private String inCancel;

        @Override
        public Integer getCdProduto() {
            return cdProduto;
        }

        @Override
        public void setCdProduto(Integer vCdProduto) {
            this.cdProduto = vCdProduto;
        }

        public String getDsProduto() {
            return dsProduto;
        }

        public void setDsProduto(String vDsProduto) {
            this.dsProduto = vDsProduto;
        }

        @Override
        public CustomDate getDtMovto() {
            return dtMovto;
        }

        public void setDtMovto(CustomDate vDtMovto) {
            this.dtMovto = vDtMovto;
        }

        public String getDsMovto() {
            return dsMovto;
        }

        public void setDsMovto(String vDsMovto) {
            this.dsMovto = vDsMovto;
        }

        @Override
        public String getTpMovto() {
            return tpMovto;
        }

        @Override
        public void setTpMovto(String vTpMovto) {
            this.tpMovto = vTpMovto;
        }

        @Override
        public Double getQtMovto() {
            return qtMovto;
        }

        @Override
        public void setQtMovto(Double vQtMovto) {
            this.qtMovto = vQtMovto;
        }

        public Double getQtEstoq() {
            return qtEstoq;
        }

        public void setQtEstoq(Double vQtEstoq) {
            this.qtEstoq = vQtEstoq;
        }

        @Override
        public Double getVlCustoMedio() {
            return vlCustoMedio;
        }

        @Override
        public void setVlCustoMedio(Double vVlCustoMedio) {
            this.vlCustoMedio = vVlCustoMedio;
        }

        public String getInCancel() {
            return inCancel;
        }

        public void setInCancel(String vInCancel) {
            this.inCancel = vInCancel;
        }             

    }

}
