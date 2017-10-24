package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.CustomDateNoTime;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class FrmConParcelaFXML implements Initializable {

    @FXML
    TableView<GridParcela> gridParcela;

    public Object param;
    public Stage stage;
    Dao dao = new Dao();
    ObservableList<GridParcela> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void iniciaTela() {
        gridParcela = ContruirTableView.Criar(gridParcela, GridParcela.class);
        ArrayList<GridParcela> parcelaArray = new ArrayList<>();
        if (param != null) {
            try {
                ContasPagarReceber conta = (ContasPagarReceber) param;
                ParcelaController parController = new ParcelaController();
                CondicaoPagto condicao = new CondicaoPagto();
                condicao.setCdCondicao(conta.getCdCondicao());
                dao.get(condicao);
                ArrayList<Parcela> parcelas = parController.getParcelas(condicao, conta.getVlConta());
                for (Parcela parcela : parcelas) {
                    GridParcela grid = new GridParcela();
                    grid.setNrParcela(parcela.getCdParcela());
                    grid.setDtVencto(new CustomDateNoTime(parcela.getDtVencto().getTime()));
                    grid.setVlParcela(Numero.doubleToReal(parcela.getVlParcela(), 2));
                    parcelaArray.add(grid);
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", ex.getMessage());
            }

            data = FXCollections.observableArrayList(parcelaArray);
            gridParcela.setItems(data);
            gridParcela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }

    @FXML
    public void sair() {
        stage.close();
    }

    public class GridParcela {

        @Coluna(nome = "Nª Parcela")
        @Style(css = "-fx-alignment: CENTER;")
        private Integer nrParcela;
        @Coluna(nome = "Dt. Vencimento")
        @Style(css = "-fx-alignment: CENTER;")
        private CustomDateNoTime dtVencto;
        @Coluna(nome = "Vl. Parcela")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlParcela;

        public Integer getNrParcela() {
            return nrParcela;
        }

        public void setNrParcela(Integer nrParcela) {
            this.nrParcela = nrParcela;
        }

        public CustomDateNoTime getDtVencto() {
            return dtVencto;
        }

        public void setDtVencto(CustomDateNoTime dtVencto) {
            this.dtVencto = dtVencto;
        }

        public String getVlParcela() {
            return vlParcela;
        }

        public void setVlParcela(String vlParcela) {
            this.vlParcela = vlParcela;
        }

    }

}
