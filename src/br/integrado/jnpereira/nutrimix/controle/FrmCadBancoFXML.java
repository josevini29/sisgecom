package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.modelo.Banco;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Conta;
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

public class FrmCadBancoFXML implements Initializable {

    @FXML
    private TextField codBanco;
    @FXML
    private TextField dsBanco;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRem;
    @FXML
    AnchorPane painel;

    ArrayList<BancoHit> listBanco = new ArrayList<>();
    Dao dao = new Dao();

    double LayoutYBanco;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codBanco.setVisible(false);
        dsBanco.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
    }

    @FXML
    public void salvar() {
        dao.autoCommit(false);
        Iterator it = listBanco.iterator();
        for (int i = 0; it.hasNext(); i++) {
            BancoHit b = (BancoHit) it.next();
            try {
                if (b.codBanco.getText().equals("")) {
                    if (!b.dsBanco.getText().equals("")) {
                        Banco banco = new Banco();
                        banco.setDsBanco(b.dsBanco.getText());
                        dao.save(banco);
                    }
                } else {
                    if (!b.dsBanco.getText().equals("")) {
                        if (b.isExcluir) {
                            long contador = dao.getCountWhere(new Conta(), "WHERE $cdBanco$ = " + Integer.parseInt(b.codBanco.getText()));
                            if (contador > 0) {
                                Alerta.AlertaError("Não permitido!!", "Existe(m) conta(s) com esse banco vinculado, código: " + b.codBanco.getText() + ".");
                                dao.rollback();
                                iniciaTela();
                                return;
                            }
                            Banco banco = new Banco();
                            banco.setCdBanco(Integer.parseInt(b.codBanco.getText()));
                            dao.delete(banco);
                        } else if (b.isAlterado) {
                            Banco banco = new Banco();
                            banco.setCdBanco(Integer.parseInt(b.codBanco.getText()));
                            banco.setDsBanco(b.dsBanco.getText());
                            dao.update(banco);
                        }
                    }
                }
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Banco.\n" + ex.toString());
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
        for (BancoHit b : listBanco) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYBanco = codBanco.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listBanco.iterator();
        for (int i = 0; it.hasNext(); i++) {
            BancoHit b = (BancoHit) it.next();
            if (!b.isExcluir) {
                b.codBanco.setEditable(codBanco.isEditable());
                b.codBanco.setPrefHeight(codBanco.getHeight());
                b.codBanco.setPrefWidth(codBanco.getWidth());
                b.codBanco.setLayoutX(codBanco.getLayoutX());
                b.codBanco.setLayoutY(LayoutYBanco);
                b.dsBanco.setEditable(dsBanco.isEditable());
                b.dsBanco.setPrefHeight(dsBanco.getHeight());
                b.dsBanco.setPrefWidth(dsBanco.getWidth());
                b.dsBanco.setLayoutX(dsBanco.getLayoutX());
                b.dsBanco.setLayoutY(LayoutYBanco);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYBanco);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYBanco);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.codBanco);
                painel.getChildren().add(b.dsBanco);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                LayoutYBanco += (codBanco.getHeight() + 5);
            }
            addValidacao(b, i, total);
        }

        painel.setPrefHeight(LayoutYBanco
                + 10);
    }

    public void iniciaTela() {
        try {
            ArrayList<Object> bancos = dao.getAllWhere(new Banco(), "ORDER BY $cdBanco$ ASC");
            listBanco.clear();
            if (bancos.isEmpty()) {
                TextField codBanco = new TextField();
                codBanco.setText("");
                TextField dsBanco = new TextField();
                dsBanco.setText("");
                BancoHit bancoHit = new BancoHit(codBanco, dsBanco);
                listBanco.add(bancoHit);
            }
            for (Object obj : bancos) {
                Banco banco = (Banco) obj;
                TextField codBanco = new TextField();
                codBanco.setText(String.valueOf(banco.getCdBanco()));
                TextField dsBanco = new TextField();
                dsBanco.setText(banco.getDsBanco());
                BancoHit bancoHit = new BancoHit(codBanco, dsBanco);
                listBanco.add(bancoHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void addValidacao(BancoHit banco, int posicao, int total) {
        FuncaoCampo.mascaraTexto(banco.dsBanco, 150);
        /*banco.codBanco.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                Iterator it = listBanco.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    BancoHit b = (BancoHit) it.next();
                    if (b.codBanco.getText().equals(banco.codBanco.getText()) && i != posicao && !b.isExcluir && !b.codBanco.getText().equals("")) {
                        Alerta.AlertaError("Valor incorreto!", "Valor já existe.");
                        banco.codBanco.requestFocus();
                    }
                }
            }
        });*/
 /*banco.dsBanco.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

            }
        });*/
        banco.btnAdd.setOnAction((ActionEvent event) -> {
            TextField codBanco = new TextField();
            codBanco.setText("");
            TextField dsBanco = new TextField();
            dsBanco.setText("");
            BancoHit b = new BancoHit(codBanco, dsBanco);
            listBanco.add(posicao + 1, b);
            atualizaLista();
        });

        banco.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                TextField codBanco = new TextField();
                codBanco.setText("");
                TextField dsBanco = new TextField();
                dsBanco.setText("");
                BancoHit b = new BancoHit(codBanco, dsBanco);
                listBanco.add(b);
            }
            listBanco.get(posicao).isExcluir = true;
            atualizaLista();
        });

    }

    public class BancoHit {

        public TextField codBanco;
        public TextField dsBanco;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;
        public boolean isAlterado = false;

        public BancoHit(TextField codBanco, TextField dsBanco) {
            this.codBanco = codBanco;
            this.dsBanco = dsBanco;
            this.btnAdd = new Button();
            this.btnRem = new Button();

            this.dsBanco.textProperty().addListener((obs, velho, novo) -> {
                this.isAlterado = true;
            });
        }
    }

}
