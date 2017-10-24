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
import br.integrado.jnpereira.nutrimix.tools.CustomDateNoTime;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Jose Vinicius
 */
public class FrmConContasFXML implements Initializable {

    @FXML
    TextField cdCliente;
    @FXML
    TextField dsCliente;
    @FXML
    TextField dtInicio;
    @FXML
    TextField dtFim;
    @FXML
    ChoiceBox tpConta;
    @FXML
    ChoiceBox tpSituacao;
    @FXML
    TableView<GridConta> gridConta;

    ObservableList<GridConta> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridConta = ContruirTableView.Criar(gridConta, GridConta.class);
        gridConta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void iniciaTela() {

    }

    @FXML
    public void atualizaGrid() {
        ArrayList<GridConta> contaArray = new ArrayList<>();
        Dao dao = new Dao();
        try {
            ArrayList<Object> contas = dao.getAllWhere(new ContasPagarReceber(), null);
            for (Object obj : contas) {
                ContasPagarReceber conta = (ContasPagarReceber) obj;
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
        @Coluna(nome = "Situação")
        @Style(css = "-fx-alignment: CENTER;")
        private String tpSituacao;
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



    }
}
