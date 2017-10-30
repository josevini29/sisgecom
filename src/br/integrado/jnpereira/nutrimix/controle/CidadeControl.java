package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Cidade;
import br.integrado.jnpereira.nutrimix.modelo.Estado;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
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

public class CidadeControl implements Initializable {

    Dao dao = new Dao();
    ObservableList<CidadeTable> data;

    @FXML
    AnchorPane painel;
    @FXML
    TextField dsCidade;
    @FXML
    TextField cdSigla;
    @FXML
    TextField dsEstado;
    @FXML
    TableView<CidadeTable> gridCidade;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraTexto(dsCidade, 80);
        FuncaoCampo.mascaraTexto(cdSigla, 2);
        cdSigla.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigo();
            }
        });
    }

    private void validaCodigo() {
        if (!cdSigla.getText().equals("")) {
            try {
                Estado estado = new Estado();
                estado.setCdEstado(cdSigla.getText().toUpperCase());
                dao.get(estado);
                cdSigla.setText(estado.getCdEstado());
                dsEstado.setText(estado.getDsEstado());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Estado não encontrado!");
                cdSigla.requestFocus();
            }
        } else {
            dsEstado.setText("");
        }
    }

    public void iniciaTela() {
        gridCidade = ContruirTableView.Criar(gridCidade, CidadeTable.class);
        gridCidade.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        atualizaTable();
    }

    private void atualizaTable() {

        ArrayList<CidadeTable> cidadesArray = new ArrayList<>();
        try {
            ArrayList<Object> cidades = dao.getAllWhere(new Cidade(), "ORDER BY $cdEstado$, $dsCidade$  ASC");
            for (Object obj : cidades) {
                Cidade cidade = (Cidade) obj;
                CidadeTable cidadesTable = new CidadeTable();
                cidadesTable.setCdSigla(cidade.getCdEstado());
                cidadesTable.setCdCidade(cidade.getCdCidade());
                cidadesTable.setDsCidade(cidade.getDsCidade());
                cidadesArray.add(cidadesTable);

            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao gerar consulta de Cidade. \n" + ex.toString());
            return;
        }

        data = FXCollections.observableArrayList(cidadesArray);
        gridCidade.setItems(data);
    }

    @FXML
    public void salvarCidade() {
        if (!dsCidade.getText().equals("") && !cdSigla.getText().equals("")) {
            try {
                Cidade cidade = new Cidade();
                cidade.setDsCidade(dsCidade.getText());
                cidade.setCdEstado(cdSigla.getText());
                dao.save(cidade);
                Alerta.AlertaInfo("Mensagem", "Cidade salva com sucesso!");
                dsCidade.setText("");
                cdSigla.setText("");
                dsEstado.setText("");
                atualizaTable();
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar Cidade.\n" + ex.toString());
            }
        } else {
            Alerta.AlertaError("Aviso!", "Preencha os campos Cidade e UF.");
        }
    }

    @FXML
    public void excluirCidade() {
        CidadeTable cidadeTab = gridCidade.getSelectionModel().getSelectedItem();
        if (cidadeTab == null) {
            Alerta.AlertaError("Aviso!", "Selecione um item.");
            return;
        }

        boolean dialog = Alerta.AlertaConfirmation("Confirmação!", "Deseja realmente excluir a Cidade: " + cidadeTab.getDsCidade() + "?");
        if (dialog) {
            Cidade cidade = new Cidade();
            cidade.setCdCidade(cidadeTab.getCdCidade());
            try {
                dao.delete(cidade);
                Alerta.AlertaInfo("Concluído", "Cidade excluida com sucesso!");
                atualizaTable();
            } catch (Exception ex) {
               Alerta.AlertaError("Erro!", "Erro ao tentar deletar cidade.\n"+ex.toString());
            }
        }

    }

    @FXML
    public void abrirListaEstado() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Estado(), "cdEstado", "dsEstado", null, "Lista de Estados");
        if (valor != null) {
            cdSigla.setText(valor);
            validaCodigo();

        }
    }

    public class CidadeTable {

        @Coluna(nome = "UF")
        @Style(css = "-fx-alignment: CENTER;")
        private String cdSigla;

        @Coluna(nome = "Cód. Cidade")
        @Style(css = "-fx-alignment: CENTER-RIGHT;")
        private int cdCidade;

        @Coluna(nome = "Descrição da Cidade")
        @Style(css = "-fx-alignment: LEFT;")
        private String dsCidade;

        public String getCdSigla() {
            return cdSigla;
        }

        public void setCdSigla(String cdSigla) {
            this.cdSigla = cdSigla;
        }

        public int getCdCidade() {
            return cdCidade;
        }

        public void setCdCidade(int cdCidade) {
            this.cdCidade = cdCidade;
        }

        public String getDsCidade() {
            return dsCidade;
        }

        public void setDsCidade(String dsCidade) {
            this.dsCidade = dsCidade;
        }

    }

}
