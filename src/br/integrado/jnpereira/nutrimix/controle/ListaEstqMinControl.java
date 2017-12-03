package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Pedido;
import br.integrado.jnpereira.nutrimix.modelo.PedidoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Criteria;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ListaEstqMinControl implements Initializable {
    
    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;
    
    @FXML
    public TableView<ClasseGenerica> grid;
    
    private Stage stage;
    public ObservableList<ClasseGenerica> dsRetorno = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grid = ContruirTableView.Criar(grid, ClasseGenerica.class);
        grid.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        grid.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }
    
    private Double divisao(Double v1, Double v2) {
        if (v1 == null | v2 == null) {
            return 0.00;
        }
        if (v1 == 0 | v2 == 0) {
            return 0.00;
        }
        return v1 / v2;
    }
    
    public void atualizaGrid() {
        ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();
        
        try {
            Criteria criteria = new Criteria(new Produto());
            criteria.AddAnd("inAtivo", true, false);
            criteria.AddAnd("inEstoque", true, false);
            criteria.AddOrderByAsc("cdProduto");
            ArrayList<Object> prods = dao.getAllWhere(new Produto(), criteria.getWhereSql());
            for (Object obj : prods) {
                Produto prod = (Produto) obj;
                ArrayList<Object> pedProd = dao.getAllWhere(new PedidoProduto(), "WHERE $cdProduto$ = " + prod.getCdProduto());
                double qtCompra = 0.00;
                for (Object obj2 : pedProd) {
                    PedidoProduto pD = (PedidoProduto) obj2;
                    Pedido ped = new Pedido();
                    ped.setCdPedido(pD.getCdPedido());
                    dao.get(ped);
                    if (!ped.getStPedido().equals("3")) {
                        qtCompra += (pD.getQtProduto() - pD.getQtEntregue());
                    }
                }
                Double qtNecessario = (prod.getQtEstoqMin() - (prod.getQtEstqAtual() + (qtCompra * prod.getQtConversao())));
                if (qtNecessario > 0) {
                    ClasseGenerica classeGenerica = new ClasseGenerica();
                    classeGenerica.setCdProduto(prod.getCdProduto());
                    classeGenerica.setDsProduto(prod.getDsProduto());
                    classeGenerica.setQtEstoq(Numero.arredondaDecimal(prod.getQtEstqAtual(), 2));
                    classeGenerica.setQtEstoqMin(Numero.arredondaDecimal(prod.getQtEstoqMin(), 2));
                    classeGenerica.setQtComprado(Numero.arredondaDecimal(qtCompra, 2));
                    classeGenerica.setQtNecessario(Numero.arredondaDecimal(divisao(qtNecessario, prod.getQtConversao()), 2));
                    classeGenerica.setQtConversao(Numero.arredondaDecimal(prod.getQtConversao(), 2));
                    valoresArray.add(classeGenerica);
                }
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
        }
        
        data = FXCollections.observableArrayList(valoresArray);
        grid.setItems(data);
        grid.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    @FXML
    public void ok() {
        if (grid.getSelectionModel().getSelectedItems().size() <= 0) {
            Alerta.AlertaInfo("Aviso!", "Selecione um item.");
            return;
        }
        dsRetorno = grid.getSelectionModel().getSelectedItems();
        stage.close();
    }
    
    @FXML
    public void cancelar() {
        stage.close();
    }
    
    public void iniciaTela() {
        atualizaGrid();
    }
    
    public Stage getStage() {
        return stage;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public class ClasseGenerica {
        
        @Coluna(nome = "Cód. Prod.")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Integer cdProduto;
        @Coluna(nome = "Desc. Produto")
        @Style(css = "-fx-alignment: CENTER-LEFT;")
        private String dsProduto;
        @Coluna(nome = "Qt. Estoq. Min")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double qtEstoqMin;
        @Coluna(nome = "Qt. Estoq.")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double qtEstoq;
        @Coluna(nome = "Qt. Conversão")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double qtConversao;
        @Coluna(nome = "Qt. Comprado")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double qtComprado;
        @Coluna(nome = "Qt. Necessário")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private Double qtNecessario;
        
        public Integer getCdProduto() {
            return cdProduto;
        }
        
        public void setCdProduto(Integer cdProduto) {
            this.cdProduto = cdProduto;
        }
        
        public String getDsProduto() {
            return dsProduto;
        }
        
        public void setDsProduto(String dsProduto) {
            this.dsProduto = dsProduto;
        }
        
        public Double getQtEstoqMin() {
            return qtEstoqMin;
        }
        
        public void setQtEstoqMin(Double qtEstoqMin) {
            this.qtEstoqMin = qtEstoqMin;
        }
        
        public Double getQtEstoq() {
            return qtEstoq;
        }
        
        public void setQtEstoq(Double qtEstoq) {
            this.qtEstoq = qtEstoq;
        }
        
        public Double getQtComprado() {
            return qtComprado;
        }
        
        public void setQtComprado(Double qtComprado) {
            this.qtComprado = qtComprado;
        }
        
        public Double getQtNecessario() {
            return qtNecessario;
        }
        
        public void setQtNecessario(Double qtNecessario) {
            this.qtNecessario = qtNecessario;
        }
        
        public Double getQtConversao() {
            return qtConversao;
        }
        
        public void setQtConversao(Double qtConversao) {
            this.qtConversao = qtConversao;
        }
        
    }
}
