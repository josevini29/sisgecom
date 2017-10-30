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
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListaGenericaControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;

    @FXML
    TextField codGenerico;
    @FXML
    TextField dsGenerico;
    @FXML
    TableView<ClasseGenerica> gridGenerica;

    private Stage stage;
    private Object classe;
    private String dsCampCod;
    private String dsCampDes;
    private String dsRetorno = null;
    private String sqlAdd = null;
    private boolean inChaveString = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gridGenerica = ContruirTableView.Criar(gridGenerica, ClasseGenerica.class);
        gridGenerica.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        gridGenerica.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                ok();
            }
        });

    }

    public void atualizaGrid() {
        ArrayList<ClasseGenerica> valoresArray = new ArrayList<>();

        if (getSqlAdd() == null){
            setSqlAdd("");
        }
        
        try {
            String sqlWhere;
            if (inChaveString) {
                sqlWhere = "WHERE UPPER($" + getDsCampCod() + "$) LIKE '%" + codGenerico.getText().toUpperCase() + "%' AND "
                        + "UPPER($" + getDsCampDes() + "$) LIKE '%" + dsGenerico.getText().toUpperCase() + "%' "
                        + getSqlAdd() + " ORDER BY $" + getDsCampCod() + "$ ASC";
            } else {
                if (codGenerico.getText().equals("")) {
                    sqlWhere = "WHERE UPPER($" + getDsCampDes() + "$) LIKE '%" + dsGenerico.getText().toUpperCase() + "%' "
                            + getSqlAdd() + " ORDER BY $" + getDsCampCod() + "$ ASC";
                } else {
                    sqlWhere = "WHERE $" + getDsCampCod() + "$ = " + codGenerico.getText() + " AND "
                            + "UPPER($" + getDsCampDes() + "$) LIKE '%" + dsGenerico.getText().toUpperCase() + "%' "
                            + getSqlAdd() + " ORDER BY $" + getDsCampCod() + "$ ASC";
                }
            }
            ArrayList<Object> arrayGenerico = dao.getAllWhere(classe, sqlWhere);
            for (Object obj : arrayGenerico) {
                ClasseGenerica classeGenerica = new ClasseGenerica();
                Field field = obj.getClass().getDeclaredField(dsCampCod);
                field.setAccessible(true);
                String valor = String.valueOf(field.get(obj));
                classeGenerica.setCodigo(valor);

                Field field2 = obj.getClass().getDeclaredField(dsCampDes);
                field2.setAccessible(true);
                String valor2 = String.valueOf(field2.get(obj));
                classeGenerica.setDescricao(valor2);

                valoresArray.add(classeGenerica);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
            stage.close();
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
        setDsRetorno(generica.getCodigo());
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
        try {
            ClasseGenerica classeGenerica = new ClasseGenerica();
            Field field = classe.getClass().getDeclaredField(dsCampCod);
            field.setAccessible(true);
            if (field.getType().getSimpleName().toLowerCase().equals("string")) {
                setInChaveString(true);
            } else {
                FuncaoCampo.mascaraNumeroInteiro(codGenerico);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consultar tabela para lista generica.\n" + ex.toString());
            return;
        }
        atualizaGrid();
    }

    public Object getClasse() {
        return classe;
    }

    public void setClasse(Object classe) {
        this.classe = classe;
    }

    public String getDsCampCod() {
        return dsCampCod;
    }

    public void setDsCampCod(String dsCampCod) {
        this.dsCampCod = dsCampCod;
    }

    public String getDsCampDes() {
        return dsCampDes;
    }

    public void setDsCampDes(String dsCampDes) {
        this.dsCampDes = dsCampDes;
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

    public boolean isInChaveString() {
        return inChaveString;
    }

    public void setInChaveString(boolean inChaveString) {
        this.inChaveString = inChaveString;
    }

    public String getSqlAdd() {
        return sqlAdd;
    }

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    public class ClasseGenerica {

        @Coluna(nome = "Código")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private String codigo;
        @Coluna(nome = "Descrição")
        @Style(css = "-fx-alignment: CENTER-LEFT;")
        private String descricao;

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }
    }
}
