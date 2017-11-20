package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Fornecedor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DivisaoControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField vlTotal;
    @FXML
    TextField vlPessoa;
    @FXML
    TextField qtPessoa;

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdProduto;
    @FXML
    TextField dsProduto;
    @FXML
    TextField vlTotalProd;
    @FXML
    TextField vlPessoaProd;
    @FXML
    TextField qtPessoaProd;

    public Stage stage;
    public Object param;
    double LayoutY;
    ArrayList<VendaControl.VendaProdHit> listaParam;
    ArrayList<Lista> lista = new ArrayList<>();
    Dao dao = new Dao();
    Pedido pedido;
    Fornecedor fornecedor;
    boolean inAntiLoop = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(qtPessoa);
        qtPessoa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!qtPessoa.getText().equals("")) {
                    Integer valor = Integer.parseInt(qtPessoa.getText());
                    if (valor <= 0.00) {
                        Alerta.AlertaError("Campo inválido!", "Quantidade não pode ser igual ou menor que zero.");
                        qtPessoa.requestFocus();
                        return;
                    }
                    qtPessoa.setText(valor.toString());
                    if (Double.parseDouble(vlTotal.getText()) > 0.00) {
                        vlPessoa.setText(Numero.doubleToReal(Double.parseDouble(vlTotal.getText()) / valor, 2));
                    }
                } else {
                    vlPessoa.setText("");
                }
            }
        });

    }

    public void iniciaTela() {
        if (param != null) {
            VendaControl.Parametro p = (VendaControl.Parametro) param;
            vlTotal.setText(Numero.doubleToReal(p.getVlTotal(), 2));
            vlPessoa.setText(Numero.doubleToReal(p.getVlTotal(), 2));
            listaParam = p.getListVendaProd();
            atualiza();
        }
    }

    public void atualiza() {
        try {

            for (VendaControl.VendaProdHit c : listaParam) {
                Lista l = new Lista();
                l.getCdProduto().setText(c.cdProduto.getText());
                l.getDsProduto().setText(c.dsProduto.getText());
                l.getVlTotalProd().setText(c.vlTotalProd.getText());
                l.getVlPessoaProd().setText(c.vlTotalProd.getText());
                l.getQtPessoaProd().setText("1");
                lista.add(l);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }
    
    @FXML
    public void sair(){
        stage.close();
    }

    public void atualizaLista() {
        LayoutY = cdProduto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = lista.iterator();
        for (int i = 0; it.hasNext(); i++) {
            Lista b = (Lista) it.next();
            b.getCdProduto().setEditable(cdProduto.isEditable());
            b.getCdProduto().setPrefHeight(cdProduto.getHeight());
            b.getCdProduto().setPrefWidth(cdProduto.getWidth());
            b.getCdProduto().setLayoutX(cdProduto.getLayoutX());
            b.getCdProduto().setLayoutY(LayoutY);
            b.getCdProduto().getStyleClass().addAll(this.cdProduto.getStyleClass());
            b.getDsProduto().setEditable(dsProduto.isEditable());
            b.getDsProduto().setPrefHeight(dsProduto.getHeight());
            b.getDsProduto().setPrefWidth(dsProduto.getWidth());
            b.getDsProduto().setLayoutX(dsProduto.getLayoutX());
            b.getDsProduto().setLayoutY(LayoutY);
            b.getDsProduto().getStyleClass().addAll(this.dsProduto.getStyleClass());
            b.getVlTotalProd().setEditable(vlTotalProd.isEditable());
            b.getVlTotalProd().setPrefHeight(vlTotalProd.getHeight());
            b.getVlTotalProd().setPrefWidth(vlTotalProd.getWidth());
            b.getVlTotalProd().setLayoutX(vlTotalProd.getLayoutX());
            b.getVlTotalProd().setLayoutY(LayoutY);
            b.getVlTotalProd().getStyleClass().addAll(this.vlTotalProd.getStyleClass());
            b.getVlPessoaProd().setEditable(vlPessoaProd.isEditable());
            b.getVlPessoaProd().setPrefHeight(vlPessoaProd.getHeight());
            b.getVlPessoaProd().setPrefWidth(vlPessoaProd.getWidth());
            b.getVlPessoaProd().setLayoutX(vlPessoaProd.getLayoutX());
            b.getVlPessoaProd().setLayoutY(LayoutY);
            b.getVlPessoaProd().getStyleClass().addAll(this.vlPessoaProd.getStyleClass());
            b.getQtPessoaProd().setEditable(qtPessoaProd.isEditable());
            b.getQtPessoaProd().setPrefHeight(qtPessoaProd.getHeight());
            b.getQtPessoaProd().setPrefWidth(qtPessoaProd.getWidth());
            b.getQtPessoaProd().setLayoutX(qtPessoaProd.getLayoutX());
            b.getQtPessoaProd().setLayoutY(LayoutY);
            b.getQtPessoaProd().getStyleClass().addAll(this.qtPessoaProd.getStyleClass());      
            painel.getChildren().add(b.getCdProduto());
            painel.getChildren().add(b.getDsProduto());
            painel.getChildren().add(b.getVlTotalProd());
            painel.getChildren().add(b.getVlPessoaProd());
            painel.getChildren().add(b.getQtPessoaProd());
            LayoutY += (cdProduto.getHeight() + 5);
            addValidacao(b);
        }
        painel.setPrefHeight(LayoutY + 10);
    }

    public void addValidacao(Lista lista) {
        FuncaoCampo.mascaraNumeroInteiro(lista.qtPessoaProd);

        lista.qtPessoaProd.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!lista.qtPessoaProd.getText().equals("")) {
                    Integer valor = Integer.parseInt(lista.qtPessoaProd.getText());
                    if (valor <= 0.00) {
                        Alerta.AlertaError("Campo inválido!", "Quantidade não pode ser igual ou menor que zero.");
                        lista.qtPessoaProd.requestFocus();
                        return;
                    }
                    lista.qtPessoaProd.setText(valor.toString());
                    if (Double.parseDouble(lista.vlTotalProd.getText()) > 0.00) {
                        lista.vlPessoaProd.setText(Numero.doubleToReal(Double.parseDouble(lista.vlTotalProd.getText()) / valor, 2));
                    }
                }
            }
        });

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class Lista {

        private TextField cdProduto = new TextField();
        private TextField dsProduto = new TextField();
        private TextField vlTotalProd = new TextField();
        private TextField vlPessoaProd = new TextField();
        private TextField qtPessoaProd = new TextField();

        public TextField getCdProduto() {
            return cdProduto;
        }

        public void setCdProduto(TextField cdProduto) {
            this.cdProduto = cdProduto;
        }

        public TextField getDsProduto() {
            return dsProduto;
        }

        public void setDsProduto(TextField dsProduto) {
            this.dsProduto = dsProduto;
        }

        public TextField getVlTotalProd() {
            return vlTotalProd;
        }

        public void setVlTotalProd(TextField vlTotalProd) {
            this.vlTotalProd = vlTotalProd;
        }

        public TextField getVlPessoaProd() {
            return vlPessoaProd;
        }

        public void setVlPessoaProd(TextField vlPessoaProd) {
            this.vlPessoaProd = vlPessoaProd;
        }

        public TextField getQtPessoaProd() {
            return qtPessoaProd;
        }

        public void setQtPessoaProd(TextField qtPessoaProd) {
            this.qtPessoaProd = qtPessoaProd;
        }

    }

}
