package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.table.ContruirTableView;
import br.integrado.jnpereira.nutrimix.table.IgnoreTable;
import br.integrado.jnpereira.nutrimix.table.Style;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FechamentoCaixaControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdFechamento;
    @FXML
    TextField cdUserAber;
    @FXML
    TextField dsUserAber;
    @FXML
    TextField dtAbertura;
    @FXML
    TextField cdUserFech;
    @FXML
    TextField dsUserFech;
    @FXML
    TextField dtFechamento;
    @FXML
    TextField vlAbertura;
    @FXML
    TextField vlFechamento;
    @FXML
    TextField vlEntradas;
    @FXML
    TextField vlSaidas;
    @FXML
    TextField vlRendimentos;
    @FXML
    Button btnAbrir;
    @FXML
    Button btnFechar;
    @FXML
    TableView<EntradasSaidas> gridMovto;

    public Stage stage;
    ObservableList<EntradasSaidas> data;
    Dao dao = new Dao();
    FechamentoCaixa fechamento;
    Double vVlFechamento = 0.00;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao.autoCommit(false);
        btnAbrir.setDisable(true);
        btnFechar.setDisable(true);
        FuncaoCampo.mascaraNumeroInteiro(cdFechamento);
        gridMovto = ContruirTableView.Criar(gridMovto, EntradasSaidas.class);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cdFechamento.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoFechamento();
            }
        });
        gridMovto.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                abrirTelaConsulta();
            }
        });
    }

    public void iniciaTela() {
        caixaAberto(false);
    }

    public void abrirTelaConsulta() {
        EntradasSaidas movto = gridMovto.getSelectionModel().getSelectedItem();
        if (movto == null) {
            Alerta.AlertaWarning("Aviso!", "Selecione um item.");
            return;
        }

        ConMovtoCaixaControl.Param param = new ConMovtoCaixaControl.Param();
        param.setCdFechamento(fechamento.getCdFechamento());
        param.setCdForPagto(movto.getCdForPagto());
        Tela tela = new Tela();
        tela.abrirTelaModalComParam(stage, Tela.CON_MOVTO_CAIXA, param);
    }

    public void caixaAberto(boolean mensagem) {
        try {
            ArrayList<Object> array = dao.getAllWhere(new FechamentoCaixa(), "WHERE $dtFechamento$ IS NULL");
            if (array.isEmpty()) {
                if (mensagem) {
                    limpar();
                    Alerta.AlertaInfo("Aviso!", "Não há caixa aberto.");
                }
                btnAbrir.setDisable(false);
                btnFechar.setDisable(true);
                return;
            }
            cdFechamento.setText(((FechamentoCaixa) array.get(0)).getCdFechamento().toString());
            validaCodigoFechamento();
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação", ex.getMessage());
        }
    }

    @FXML
    public void buscarCaixaAberto() {
        limpar();
        caixaAberto(true);
    }

    private void validaCodigoFechamento() {
        if (!cdFechamento.getText().equals("") & fechamento == null) {
            try {
                fechamento = new FechamentoCaixa();
                fechamento.setCdFechamento(Integer.parseInt(cdFechamento.getText()));
                dao.get(fechamento);
                dtAbertura.setText(Data.AmericaToBrasil(fechamento.getDtAbertura()));
                dtFechamento.setText(Data.AmericaToBrasil(fechamento.getDtFechamento()));
                cdUserAber.setText(fechamento.getCdUserAber().toString());
                dsUserAber.setText(Numero.getDsUsuario(fechamento.getCdUserAber()));
                btnFechar.setDisable(false);
                btnAbrir.setDisable(true);
                vlAbertura.setText(Numero.doubleToR$(fechamento.getVlInicial()));
                if (fechamento.getVlFinal() != null) {
                    cdUserFech.setText(fechamento.getCdUserFech().toString());
                    dsUserFech.setText(Numero.getDsUsuario(fechamento.getCdUserFech()));
                    vlFechamento.setText(Numero.doubleToR$(fechamento.getVlFinal()));
                    btnFechar.setDisable(true);
                }
                cdFechamento.setEditable(false);

                //Caso seja o ultima caixa fechado ele pode reabrir
                //long qtCaixa = dao.getCountWhere(new FechamentoCaixa(), "WHERE $cdFechamento$ > " + fechamento.getCdFechamento());
                //if (qtCaixa == 0){
                //    btnAbrir.setDisable(false);
                //}
                atualizaGrid();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                fechamento = null;
                btnAbrir.setDisable(true);
                btnFechar.setDisable(true);
                cdFechamento.requestFocus();
            }
        }
    }

    public void atualizaGrid() {
        try {
            int cd = fechamento.getCdFechamento();
            ArrayList<EntradasSaidas> valoresArray = new ArrayList<>();
            ResultSet rs = dao.execSQL("SELECT A.CD_FORMA, A.DS_FORMA ,\n"
                    + "(SELECT SUM(B.VL_MOVTO) FROM NUTRIMIX.MOVTO_CAIXA B WHERE B.CD_FECHAMENTO = " + cd + " AND B.CD_FORPAGTO = A.CD_FORMA AND B.TP_MOVTO_CAIXA = 'E') VL_ENTRADA,\n"
                    + "(SELECT SUM(C.VL_MOVTO) FROM NUTRIMIX.MOVTO_CAIXA C WHERE C.CD_FECHAMENTO = " + cd + " AND C.CD_FORPAGTO = A.CD_FORMA AND C.TP_MOVTO_CAIXA = 'S') VL_SAIDA\n"
                    + "FROM NUTRIMIX.FORMA_PAGTO A\n"
                    + "WHERE A.CD_FORMA IN (SELECT D.CD_FORPAGTO FROM NUTRIMIX.MOVTO_CAIXA D WHERE D.CD_FECHAMENTO = " + cd + ")");
            double vVlTotalEntrada = 0.00;
            double vVlTotalSaida = 0.00;
            while (rs.next()) {
                EntradasSaidas movto = new EntradasSaidas();
                movto.setCdForPagto(rs.getInt("CD_FORMA"));
                movto.setDsForPagto(rs.getString("DS_FORMA"));
                movto.setVlEntradas(Numero.doubleToR$(rs.getDouble("VL_ENTRADA")));
                movto.setVlSaidas(Numero.doubleToR$(rs.getDouble("VL_SAIDA")));
                movto.setVlRendimento(Numero.doubleToR$(rs.getDouble("VL_ENTRADA") - rs.getDouble("VL_SAIDA")));
                vVlTotalEntrada += rs.getDouble("VL_ENTRADA");
                vVlTotalSaida += rs.getDouble("VL_SAIDA");
                valoresArray.add(movto);
            }

            vlEntradas.setText(Numero.doubleToR$(vVlTotalEntrada));
            vlSaidas.setText(Numero.doubleToR$(vVlTotalSaida));
            vlRendimentos.setText(Numero.doubleToR$(vVlTotalEntrada - vVlTotalSaida));
            vVlFechamento = ((fechamento.getVlInicial() + vVlTotalEntrada) - vVlTotalSaida);
            vlFechamento.setText(Numero.doubleToR$(vVlFechamento));
            data = FXCollections.observableArrayList(valoresArray);
            gridMovto.setItems(data);
            gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao consulta movimento do caixa!\n" + ex.toString());
        }
    }

    @FXML
    public void pesquisarFechamento() {
        Tela tela = new Tela();
        String valor = tela.abrirListaFechCaixa();
        if (valor != null) {
            cdFechamento.setText(valor);
            validaCodigoFechamento();
        }
    }

    @FXML
    public void abrirCaixa() {
        try {
            if (fechamento == null) {
                if (!Alerta.AlertaConfirmation("Confirmação!", "Deseja realmente abrir o caixa?")) {
                    return;
                }
                double vVlAbertura = 0.00;
                ArrayList<Object> array = dao.getAllWhere(new FechamentoCaixa(), " ORDER BY $cdFechamento$ DESC");
                if (!array.isEmpty()) {
                    FechamentoCaixa fechamentoAnterior = (FechamentoCaixa) array.get(0);
                    vVlAbertura = fechamentoAnterior.getVlFinal();
                }
                FechamentoCaixa fec = new FechamentoCaixa();
                fec.setDtAbertura(Data.getAgora());
                fec.setCdUserAber(MenuControl.usuarioAtivo);
                fec.setVlInicial(vVlAbertura);
                dao.save(fec);
                dao.commit();
                cdFechamento.setText(fec.getCdFechamento().toString());
                validaCodigoFechamento();
                Alerta.AlertaInfo("Aviso!", "Caixa aberto com sucesso!");
            } else {
                if (!Alerta.AlertaConfirmation("Confirmação!", "Deseja realmente reabrir o caixa?")) {
                    return;
                }
                fechamento.setDtFechamento(null);
                fechamento.setCdUserFech(null);
                fechamento.setVlFinal(null);
                dao.update(fechamento);
                dao.commit();
                Alerta.AlertaInfo("Aviso!", "Caixa reaberto com sucesso!");
                validaCodigoFechamento();
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação", ex.getMessage());
        }
    }

    @FXML

    public void fecharCaixa() {
        try {
            if (Alerta.AlertaConfirmation("Confirmação!", "Deseja realmente fechar o caixa?")) {
                fechamento.setDtFechamento(Data.getAgora());
                fechamento.setCdUserFech(MenuControl.usuarioAtivo);
                fechamento.setVlFinal(vVlFechamento);
                dao.update(fechamento);
                dao.commit();
                Alerta.AlertaInfo("Aviso!", "Caixa fechado com sucesso!");
                limpar();
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação", ex.getMessage());
        }
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        fechamento = null;
        FuncaoCampo.limparCampos(anchor);
        cdFechamento.setEditable(true);
        data = null;
        gridMovto.setItems(data);
        gridMovto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnFechar.setDisable(true);
        try {
            ArrayList<Object> array = dao.getAllWhere(new FechamentoCaixa(), "WHERE $dtFechamento$ IS NULL");
            if (array.isEmpty()) {
                btnAbrir.setDisable(false);
            } else {
                btnAbrir.setDisable(true);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação", ex.getMessage());
        }
    }

    public class EntradasSaidas {

        @Coluna(nome = "Forma Pagamento")
        @Style(css = "-fx-alignment: CENTER;")
        private String dsForPagto;
        @Coluna(nome = "Valor Saídas")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlSaidas;
        @Coluna(nome = "Valor Entradas")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlEntradas;
        @Coluna(nome = "Valor Rendimento")
        @Style(css = "-fx-alignment: CENTER;")
        private String vlRendimento;
        @IgnoreTable
        private Integer cdForPagto;

        public String getDsForPagto() {
            return dsForPagto;
        }

        public void setDsForPagto(String dsForPagto) {
            this.dsForPagto = dsForPagto;
        }

        public String getVlSaidas() {
            return vlSaidas;
        }

        public void setVlSaidas(String vlSaidas) {
            this.vlSaidas = vlSaidas;
        }

        public String getVlEntradas() {
            return vlEntradas;
        }

        public void setVlEntradas(String vlEntradas) {
            this.vlEntradas = vlEntradas;
        }

        public String getVlRendimento() {
            return vlRendimento;
        }

        public void setVlRendimento(String vlRendimento) {
            this.vlRendimento = vlRendimento;
        }

        public Integer getCdForPagto() {
            return cdForPagto;
        }

        public void setCdForPagto(Integer cdForPagto) {
            this.cdForPagto = cdForPagto;
        }

    }

}
