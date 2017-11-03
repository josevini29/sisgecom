/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.CustomDateNoTime;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
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

public class ListaPedidoCompraControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;

    @FXML
    TextField cdPedido;
    @FXML
    TextField cdForne;
    @FXML
    TextField dsForne;
    @FXML
    ChoiceBox stPedido;
    @FXML
    TableView<ClasseGenerica> gridGenerica;

    private Stage stage;
    private String dsRetorno = null;
    Fornecedor fornecedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gridGenerica = ContruirTableView.Criar(gridGenerica, ClasseGenerica.class);
        gridGenerica.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TrataCombo.setValueComboStAtendimento(stPedido, null);

        cdForne.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoForne();
            }
        });
        gridGenerica.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                ok();
            }
        });

    }

    public void atualizaGrid() {
        ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();

        try {
            Criteria criteria = new Criteria(new Pedido());
            criteria.AddAnd("cdPedido", cdPedido.getText(), false);
            criteria.AddAnd("cdFornecedor", cdForne.getText(), false);
            criteria.AddAnd("stPedido", TrataCombo.getValueComboStAtendimento(stPedido), false);
            ArrayList<Object> peds = dao.getAllWhere(new Pedido(), criteria.getWhereSql());
            for (Object obj : peds) {
                Pedido ped = (Pedido) obj;
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.setCdPedido(ped.getCdPedido());
                classeGenerica.setCdForne(ped.getCdFornecedor());

                Fornecedor forne = new Fornecedor();
                forne.setCdFornecedor(ped.getCdFornecedor());
                dao.get(forne);
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(forne.getCdPessoa());
                dao.get(pessoa);
                classeGenerica.setDsForne(pessoa.getDsPessoa());
                
                classeGenerica.setStPedido(TrataCombo.getTpSituacao(Integer.parseInt(ped.getStPedido())));
                classeGenerica.setDtCadastro(new CustomDateNoTime(ped.getDtCadastro().getTime()));
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
        setDsRetorno(generica.getCdPedido().toString());
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
        FuncaoCampo.mascaraNumeroInteiro(cdPedido);
        FuncaoCampo.mascaraNumeroInteiro(cdForne);
        atualizaGrid();
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
                    Pessoa pessoa = new Pessoa();
                    pessoa.setCdPessoa(fornecedor.getCdPessoa());
                    dao.get(pessoa);
                    dsForne.setText(pessoa.getDsPessoa());
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    dsForne.setText("");
                    cdForne.requestFocus();
                }
            }
        } else {
            dsForne.setText("");
        }
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

        @Coluna(nome = "Pedido")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Integer cdPedido;
        @Coluna(nome = "Cód.Forne.")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Integer cdForne;
        @Coluna(nome = "Descrição Fornecedor")
        @Style(css = "-fx-alignment: CENTER-LEFT;")
        private String dsForne;
        @Coluna(nome = "Situação")
        @Style(css = "-fx-alignment: CENTER;")
        private String stPedido;
        @Coluna(nome = "Data Pedido")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDateNoTime dtCadastro;

        public Integer getCdPedido() {
            return cdPedido;
        }

        public void setCdPedido(Integer cdPedido) {
            this.cdPedido = cdPedido;
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

        public String getStPedido() {
            return stPedido;
        }

        public void setStPedido(String stPedido) {
            this.stPedido = stPedido;
        }

        public CustomDateNoTime getDtCadastro() {
            return dtCadastro;
        }

        public void setDtCadastro(CustomDateNoTime dtCadastro) {
            this.dtCadastro = dtCadastro;
        }

    }
}
