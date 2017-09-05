package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.GrupoProduto;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FrmCadGrupoProdFXML implements Initializable {

    @FXML
    private TextField codGrupo;
    @FXML
    private TextField dsGrupo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<GrupoHit> listGrupo = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYGrupo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codGrupo.setVisible(false);
        dsGrupo.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listGrupo.iterator();
        for (int i = 0; it.hasNext(); i++) {
            GrupoHit b = (GrupoHit) it.next();
            try {
                if (b.codGrupo.getText().equals("")) {
                    if (!b.dsGrupo.getText().equals("")) {
                        GrupoProduto grupo = new GrupoProduto();
                        grupo.setDsGrupo(b.dsGrupo.getText());
                        dao.save(grupo);
                    }
                } else {
                    if (!b.dsGrupo.getText().equals("")) {
                        if (b.isExcluir) {
                            long contador = dao.getCountWhere(new Produto(), "WHERE $cdGrupo$ = " + Integer.parseInt(b.codGrupo.getText()));
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) produtos(s) com esse grupo vinculado, código: " + b.codGrupo.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }
                            GrupoProduto grupo = new GrupoProduto();
                            grupo.setCdGrupo(Integer.parseInt(b.codGrupo.getText()));
                            dao.delete(grupo);
                        } else if (b.isAlterado) {
                            GrupoProduto grupo = new GrupoProduto();
                            grupo.setCdGrupo(Integer.parseInt(b.codGrupo.getText()));
                            grupo.setDsGrupo(b.dsGrupo.getText());
                            dao.update(grupo);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Grupo.\n" + ex.toString());
                return;
            }
        }
        dao.commit();
        Alerta.AlertaInfo("Concluído", "Registros salvos com sucesso!");
        iniciaTela();
    }

    @FXML
    public void cancelar() {
        iniciaTela();
    }

    public void atualizaLista() {
        int total = 0;

        for (GrupoHit b : listGrupo) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYGrupo = codGrupo.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listGrupo.iterator();
        for (int i = 0; it.hasNext(); i++) {
            GrupoHit b = (GrupoHit) it.next();
            if (!b.isExcluir) {
                b.codGrupo.setEditable(codGrupo.isEditable());
                b.codGrupo.setPrefHeight(codGrupo.getHeight());
                b.codGrupo.setPrefWidth(codGrupo.getWidth());
                b.codGrupo.setLayoutX(codGrupo.getLayoutX());
                b.codGrupo.setLayoutY(LayoutYGrupo);
                b.dsGrupo.setEditable(dsGrupo.isEditable());
                b.dsGrupo.setPrefHeight(dsGrupo.getHeight());
                b.dsGrupo.setPrefWidth(dsGrupo.getWidth());
                b.dsGrupo.setLayoutX(dsGrupo.getLayoutX());
                b.dsGrupo.setLayoutY(LayoutYGrupo);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYGrupo);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYGrupo);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codGrupo);
                painel.getChildren().add(b.dsGrupo);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYGrupo += (codGrupo.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYGrupo
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> grupos = dao.getAllWhere(new GrupoProduto(), "ORDER BY $cdGrupo$ ASC");
            listGrupo.clear();
            if (grupos.isEmpty()) {
                TextField codGrupo = new TextField();
                codGrupo.setText("");
                TextField dsGrupo = new TextField();
                dsGrupo.setText("");
                GrupoHit grupoHit = new GrupoHit(codGrupo, dsGrupo);
                listGrupo.add(grupoHit);
            }
            for (Object obj : grupos) {
                GrupoProduto grupo = (GrupoProduto) obj;
                TextField codGrupo = new TextField();
                codGrupo.setText(String.valueOf(grupo.getCdGrupo()));
                TextField dsGrupo = new TextField();
                dsGrupo.setText(grupo.getDsGrupo());
                GrupoHit grupoHit = new GrupoHit(codGrupo, dsGrupo);
                listGrupo.add(grupoHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(GrupoHit grupo, int posicao, int total) {
        FuncaoCampo.mascaraTexto(grupo.dsGrupo, 45);
        /*grupo.codGrupo.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listGrupo.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    GrupoHit b = (GrupoHit) it.next();
                    if (b.codGrupo.getText().equals(grupo.codGrupo.getText()) && i != posicao && !b.isExcluir && !b.codGrupo.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        grupo.codGrupo.requestFocus();
                    }
                }
            }
        });*/
 /*grupo.dsGrupo.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        grupo.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codGrupo = new TextField();
            codGrupo.setText("");
            TextField dsGrupo = new TextField();
            dsGrupo.setText("");
            GrupoHit b = new GrupoHit(codGrupo, dsGrupo);
            listGrupo.add(posicao + 1, b);
            atualizaLista();
        });

        grupo.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                TextField codGrupo = new TextField();
                codGrupo.setText("");
                TextField dsGrupo = new TextField();
                dsGrupo.setText("");
                GrupoHit b = new GrupoHit(codGrupo, dsGrupo);
                listGrupo.add(b);
            }
            listGrupo.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class GrupoHit {

        public TextField codGrupo;
        public TextField dsGrupo;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public GrupoHit(TextField codGrupo, TextField dsGrupo) {
            this.codGrupo = codGrupo;
            this.dsGrupo = dsGrupo;
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsGrupo.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
        }
    }

}
