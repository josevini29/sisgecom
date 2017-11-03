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
import br.integrado.jnpereira.nutrimix.tools.Numero;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListaPessoaControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<ClasseGenerica> data;

    @FXML
    TextField codGenerico;
    @FXML
    TextField dsGenerico;
    @FXML
    TableView<ClasseGenerica> gridGenerica;
    @FXML
    CheckBox checkAtivo;
    @FXML
    TextField nrCpfCnpj;

    private Stage stage;
    private Object classe;
    private String dsRetorno = null;
    private boolean inAtivo;

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
        String sql = new String();
        switch (classe.getClass().getSimpleName().toLowerCase()) {
            case "cliente":
                getStage().setTitle("Lista de Clientes");
                sql = "SELECT $Cliente.cdCliente$, $Pessoa.dsPessoa$, $Pessoa.nrCpfCnpj$, $Cliente.inAtivo$, $Pessoa.tpPessoa$ FROM &Cliente& "
                        + " INNER JOIN &Pessoa& ON $Pessoa.cdPessoa$ = $Cliente.cdPessoa$ WHERE "
                        + (!codGenerico.getText().equals("") ? "$Cliente.cdCliente$ = " + codGenerico.getText() + " AND " : "")
                        + " UPPER($Pessoa.dsPessoa$) LIKE UPPER('%" + dsGenerico.getText() + "%') AND $Pessoa.nrCpfCnpj$ LIKE '%" + nrCpfCnpj.getText() + "%' "
                        + (checkAtivo.isSelected() ? " AND $Cliente.inAtivo$ = 'T' " : "") + "ORDER BY $Cliente.cdCliente$ ASC;";
                break;
            case "fornecedor":
                getStage().setTitle("Lista de Fornecedores");
                sql = "SELECT $Fornecedor.cdFornecedor$, $Pessoa.dsPessoa$, $Pessoa.nrCpfCnpj$, $Fornecedor.inAtivo$, $Pessoa.tpPessoa$ FROM &Fornecedor& "
                        + " INNER JOIN &Pessoa& ON $Pessoa.cdPessoa$ = $Fornecedor.cdPessoa$ WHERE "
                        + (!codGenerico.getText().equals("") ? "$Fornecedor.cdFornecedor$ = " + codGenerico.getText() + " AND " : "")
                        + " UPPER($Pessoa.dsPessoa$) LIKE UPPER('%" + dsGenerico.getText() + "%') AND $Pessoa.nrCpfCnpj$ LIKE '%" + nrCpfCnpj.getText() + "%' "
                        + (checkAtivo.isSelected() ? " AND $Fornecedor.inAtivo$ = 'T' " : "") + "ORDER BY $Fornecedor.cdFornecedor$ ASC;";
                break;
            case "funcionario":
                getStage().setTitle("Lista de Funcionários");
                sql = "SELECT $Funcionario.cdFuncionario$, $Pessoa.dsPessoa$, $Pessoa.nrCpfCnpj$, CASE WHEN $Funcionario.dtDemissao$ IS NULL THEN 'T' ELSE 'F' END inAtivo, $Pessoa.tpPessoa$ FROM &Funcionario& "
                        + " INNER JOIN &Pessoa& ON $Pessoa.cdPessoa$ = $Funcionario.cdPessoa$ WHERE "
                        + (!codGenerico.getText().equals("") ? "$Funcionario.cdFuncionario$ = " + codGenerico.getText() + " AND " : "")
                        + " UPPER($Pessoa.dsPessoa$) LIKE UPPER('%" + dsGenerico.getText() + "%') AND $Pessoa.nrCpfCnpj$ LIKE '%" + nrCpfCnpj.getText() + "%' "
                        + (checkAtivo.isSelected() ? " AND $Funcionario.dtDemissao$ IS NULL " : "") + "ORDER BY $Funcionario.cdFuncionario$ ASC;";
                break;
        }

        try {
            ResultSet rs = dao.execSQL(sql);
            while (rs.next()) {
                ClasseGenerica classeGenerica = new ClasseGenerica();
                classeGenerica.setCodigo(rs.getString(1));
                classeGenerica.setDescricao(rs.getString(2));
                if (rs.getString(5).equals("F")) {
                    classeGenerica.setCnpj(Numero.NumeroToCPF(rs.getString(3)));
                } else {
                    classeGenerica.setCnpj(Numero.NumeroToCNPJ(rs.getString(3)));
                }
                classeGenerica.setAtivo(rs.getBoolean(4) == true ? "Sim" : "Não");
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
        if (isInAtivo()) {
            checkAtivo.setSelected(true);
            checkAtivo.setVisible(false);
        }
        FuncaoCampo.mascaraNumeroInteiro(codGenerico);
        FuncaoCampo.mascaraNumeroInteiro(nrCpfCnpj);
        atualizaGrid();
    }

    public Object getClasse() {
        return classe;
    }

    public void setClasse(Object classe) {
        this.classe = classe;
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

    public boolean isInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(boolean inAtivo) {
        this.inAtivo = inAtivo;
    }

    public class ClasseGenerica {

        @Coluna(nome = "Código")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private String codigo;
        @Coluna(nome = "Descrição")
        @Style(css = "-fx-alignment: CENTER-LEFT;")
        private String descricao;
        @Coluna(nome = "CPF/CNPJ")
        @Style(css = "-fx-alignment: CENTER;")
        private String cnpj;
        @Coluna(nome = "Ativo?")
        @Style(css = "-fx-alignment: CENTER;")
        private String ativo;

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

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getAtivo() {
            return ativo;
        }

        public void setAtivo(String ativo) {
            this.ativo = ativo;
        }
    }
}
