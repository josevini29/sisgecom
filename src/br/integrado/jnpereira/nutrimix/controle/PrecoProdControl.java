package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.PrecoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PrecoProdControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<PrecoProdTable> data;

    @FXML
    AnchorPane painel;
    @FXML
    TextField codProduto;
    @FXML
    TextField dsProduto;
    @FXML
    TextField vlPreco;
    @FXML
    TableView<PrecoProdTable> gridPreco;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(codProduto);
        FuncaoCampo.mascaraNumeroDecimal(vlPreco);
        codProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigo();
            }
        });
        vlPreco.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlPreco.getText().equals("")) {
                    Double valor = Double.parseDouble(vlPreco.getText());
                    vlPreco.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });
    }

    private void validaCodigo() {
        if (!codProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(codProduto.getText()));
                dao.get(prod);

                if (!prod.getInAtivo()) {
                    Alerta.AlertaError("Inválido", "Produto está inativo.");
                    codProduto.requestFocus();
                    return;
                }

                dsProduto.setText(prod.getDsProduto());
                atualizaTable();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Produto não encontrado!");
                codProduto.requestFocus();
            }
        } else {
            dsProduto.setText("");
        }
    }

    public void iniciaTela() {
        gridPreco = ContruirTableView.Criar(gridPreco, PrecoProdTable.class);
        gridPreco.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void atualizaTable() {
        if (!codProduto.getText().equals("")) {
            ArrayList<PrecoProdTable> precosArray = new ArrayList<>();
            try {
                ArrayList<Object> precos = dao.getAllWhere(new PrecoProduto(), " WHERE $cdProduto$ = " + codProduto.getText() + " ORDER BY $dtPreco$ DESC");
                for (Object obj : precos) {
                    PrecoProduto preco = (PrecoProduto) obj;
                    PrecoProdTable precosTable = new PrecoProdTable();
                    precosTable.setCdProduto(preco.getCdProduto());

                    Produto prod = new Produto();
                    prod.setCdProduto(preco.getCdProduto());
                    dao.get(prod);

                    precosTable.setDsProduto(prod.getDsProduto());
                    precosTable.setDtPreco(Data.AmericaToBrasil(preco.getDtPreco()));
                    precosTable.setVlPreco(Numero.doubleToR$(preco.getVlPreco()));
                    precosArray.add(precosTable);

                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao gerar consulta de Preço do Produto. \n" + ex.toString());
            }

            data = FXCollections.observableArrayList(precosArray);
            gridPreco.setItems(data);
        }
    }

    @FXML
    public void atualizarPreco() {
        if (!codProduto.getText().equals("") && !vlPreco.getText().equals("")) {
            try {
                PrecoProduto preco = new PrecoProduto();
                preco.setCdProduto(Integer.parseInt(codProduto.getText()));
                preco.setVlPreco(Numero.RealToDouble(vlPreco.getText()));
                preco.setDtPreco(Data.getAgora());
                dao.save(preco);
                Alerta.AlertaInfo("Mensagem", "Preço salvo com sucesso!");
                vlPreco.setText("");
                atualizaTable();
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar preço produto.\n" + ex.toString());
            }
        } else {
            Alerta.AlertaError("Aviso!", "Preencha os campos Código do Produto e Preço Atual.");
        }
    }

    @FXML
    public void abrirListaProduto() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            codProduto.setText(valor);
            validaCodigo();
        }
    }

    public class PrecoProdTable {

        @Coluna(nome = "Código Prod.")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private int cdProduto;

        @Coluna(nome = "Descrição do Produto")
        @Style(css = "-fx-alignment: LEFT;")
        private String dsProduto;

        @Coluna(nome = "Data Preço")
        @Style(css = "-fx-alignment: CENTER;")
        private String dtPreco;

        @Coluna(nome = "Preço")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private String vlPreco;

        public int getCdProduto() {
            return cdProduto;
        }

        public void setCdProduto(int cdProduto) {
            this.cdProduto = cdProduto;
        }

        public String getDsProduto() {
            return dsProduto;
        }

        public void setDsProduto(String dsProduto) {
            this.dsProduto = dsProduto;
        }

        public String getDtPreco() {
            return dtPreco;
        }

        public void setDtPreco(String dtPreco) {
            this.dtPreco = dtPreco;
        }

        public String getVlPreco() {
            return vlPreco;
        }

        public void setVlPreco(String vlPreco) {
            this.vlPreco = vlPreco;
        }

    }

}
