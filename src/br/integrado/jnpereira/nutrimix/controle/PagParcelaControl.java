package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PagParcelaControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdConta;
    @FXML
    TextField tpConta;
    @FXML
    TextField dtConta;
    @FXML
    TextField vlTotal;
    @FXML
    TextField vlPago;

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdParcela;
    @FXML
    TextField dtVencto;
    @FXML
    TextField vlParcela;
    @FXML
    TextField tpMovto;
    @FXML
    TextField dtPagamento;
    @FXML
    ChoiceBox tpForPagto;
    @FXML
    TextField vlPagamento;
    @FXML
    TextField vlDesconto;
    @FXML
    TextField vlMulta;
    @FXML
    CheckBox inCancelada;
    @FXML
    Button btnAdd;
    @FXML
    Button btnEstornar;
    @FXML
    Label lblMovtoParcela;

    public Stage stage;
    public Object param;
    double LayoutY;
    ArrayList<ParcelaHit> listParcela = new ArrayList<>();
    Dao dao = new Dao();
    ContasPagarReceber conta;
    //boolean inAntiLoop = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void iniciaTela() {
        if (param != null) {
            conta = (ContasPagarReceber) param;
            validaCodigoConta();
        }

    }

    private void validaCodigoConta() {
        try {
            dao.get(conta);
            cdConta.setText(conta.getCdConta().toString());
            if (conta.getCdDespesa() != null) {
                tpConta.setText(TrataCombo.getTpConta(3));
            } else if (conta.getCdMovto() != null && conta.getTpMovto().equals("E")) {
                tpConta.setText(TrataCombo.getTpConta(2));
            } else if (conta.getCdMovto() != null && conta.getTpMovto().equals("S")) {
                tpConta.setText(TrataCombo.getTpConta(1));
            }
            dtConta.setText(Data.AmericaToBrasilSemHora(conta.getDtMovto()));
            vlTotal.setText(Numero.doubleToReal(conta.getVlConta(), 2));
            vlPago.setText("0.00");
            atualizaParcela();
        } catch (Exception ex) {
            Alerta.AlertaError("Notificação", ex.getMessage());
        }
    }

    @FXML
    public void cancelar() {
        iniciaTela();
    }

    @FXML
    public void salvar() {

        for (ParcelaHit parcelaHit : listParcela) {

            if (parcelaHit.parcela == null) {
                if (parcelaHit.vlParcela.equals("")) {
                    Alerta.AlertaError("Campo inválido", "Valor da parcela é obrigatória.");
                    parcelaHit.vlParcela.requestFocus();
                    return;
                }

                if (parcelaHit.dtVencto.equals("")) {
                    Alerta.AlertaError("Campo inválido", "Data de vencimento da parcela é obrigatória.");
                    parcelaHit.dtVencto.requestFocus();
                    return;
                }
            }

            if (!parcelaHit.dtPagamento.getText().equals("")
                    || !parcelaHit.vlDesconto.getText().equals("")
                    || !parcelaHit.vlMulta.getText().equals("")) {

                if (parcelaHit.dtPagamento.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido", "Para pagamento de parcela data de pagamento é obrigatória.");
                    parcelaHit.tpForPagto.requestFocus();
                    return;
                }

                if (TrataCombo.getValueComboTpFormaPagto(parcelaHit.tpForPagto) == null) {
                    Alerta.AlertaError("Campo inválido", "Para pagamento de parcela forma de pagamento é obrigatória.");
                    parcelaHit.tpForPagto.requestFocus();
                    return;
                }

                double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());
                double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                vVlParcela = vVlParcela - vVlDesconto;
                vVlParcela = vVlParcela + vVlMulta;
                if (vVlParcela < 0) {
                    Alerta.AlertaError("Campo inválido", "Pagamento não pode ser inferior ao valor 0.");
                    parcelaHit.vlDesconto.requestFocus();
                    return;
                }
            }

        }
        try {
            dao.autoCommit(false);

            for (ParcelaHit parcelaHit : listParcela) {
                boolean vInPag = false;
                if (parcelaHit.parcela == null) {
                    parcelaHit.parcela = new Parcela();
                    parcelaHit.parcela.setCdConta(conta.getCdConta());
                    long vCdParcela = dao.getCountWhere(new Parcela(), " WHERE $cdConta$ = " + conta.getCdConta());
                    vCdParcela++;
                    parcelaHit.parcela.setCdParcela(Integer.parseInt(String.valueOf(vCdParcela)));
                    parcelaHit.parcela.setTpMovto(conta.getTpMovto());
                    parcelaHit.parcela.setDtVencto(Data.StringToDate(parcelaHit.dtVencto.getText()));
                    parcelaHit.parcela.setVlParcela(converteDouble(parcelaHit.vlParcela.getText()));
                    parcelaHit.parcela.setDtPagto(Data.StringToDate(parcelaHit.dtPagamento.getText()));
                    if (!parcelaHit.dtPagamento.getText().equals("")) {
                        double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());
                        double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                        double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                        vVlParcela = vVlParcela - vVlDesconto;
                        vVlParcela = vVlParcela + vVlMulta;
                        parcelaHit.parcela.setVlPagto(vVlParcela);
                        parcelaHit.parcela.setVlDesconto(vVlDesconto);
                        parcelaHit.parcela.setVlMulta(vVlMulta);
                        parcelaHit.parcela.setCdForPagto(TrataCombo.getValueComboTpFormaPagto(tpForPagto));
                        vInPag = true;
                    }
                    parcelaHit.parcela.setInCancelada(false);
                    parcelaHit.parcela.setCdUserMovto(MenuControl.usuarioAtivo);
                    parcelaHit.parcela.setDtUltMovto(Data.getAgora());
                    dao.save(parcelaHit.parcela);
                } else {
                    if (!parcelaHit.dtPagamento.getText().equals("")
                            && parcelaHit.parcela.getDtPagto() == null) {
                        double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());
                        double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                        double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                        vVlParcela = vVlParcela - vVlDesconto;
                        vVlParcela = vVlParcela + vVlMulta;
                        parcelaHit.parcela.setVlPagto(vVlParcela);
                        parcelaHit.parcela.setVlDesconto(vVlDesconto);
                        parcelaHit.parcela.setVlMulta(vVlMulta);
                        parcelaHit.parcela.setCdForPagto(TrataCombo.getValueComboTpFormaPagto(tpForPagto));
                        vInPag = true;
                    }
                }
                if (vInPag) {
                    CaixaControler caixa = new CaixaControler();
                    caixa.geraMovtoCaixa(parcelaHit.parcela);
                }
            }

            dao.commit();
            validaCodigoConta();
        } catch (Exception ex) {
            dao.rollback();
            Alerta.AlertaError("Erro!", ex.getMessage());
            return;
        }
        Alerta.AlertaInfo("Concluído", "Registro salvo!");
    }

    public double converteDouble(String valor) {
        if (valor == null) {
            return 0.00;
        } else {
            if (valor.equals("")) {
                return 0.00;
            }
        }
        return Double.parseDouble(valor);
    }

    public void atualizaParcela() {
        try {
            String where = "WHERE $cdConta$ = " + conta.getCdConta() + " ORDER BY $dtVencto$ ASC";
            ArrayList<Object> atends = dao.getAllWhere(new Parcela(), where);
            listParcela.clear();
            double vVlPago = 0.00;
            for (Object obj : atends) {
                Parcela parcela = (Parcela) obj;
                ParcelaHit parcelaHit = new ParcelaHit();
                parcelaHit.cdParcela.setText(parcela.getCdParcela().toString());
                parcelaHit.dtVencto.setText(Data.AmericaToBrasilSemHora(parcela.getDtVencto()));
                parcelaHit.vlParcela.setText(Numero.doubleToReal(parcela.getVlParcela(), 2));
                parcelaHit.tpMovto.setText(EstoqueControl.getDsEntraSaida(parcela.getTpMovto()));
                parcelaHit.vlPagamento.setText(Numero.doubleToReal(parcela.getVlPagto(), 2));
                parcelaHit.dtPagamento.setText(Data.AmericaToBrasilSemHora(parcela.getDtPagto()));
                parcelaHit.vlDesconto.setText(Numero.doubleToReal(parcela.getVlDesconto(), 2));
                parcelaHit.vlMulta.setText(Numero.doubleToReal(parcela.getVlMulta(), 2));
                parcelaHit.inCancelada.setSelected(parcela.getInCancelada());
                parcelaHit.lblMovtoParcela.setText(Numero.getCadastro(parcela.getCdUserMovto(), parcela.getDtUltMovto()));
                parcelaHit.parcela = parcela;

                if (parcela.getVlPagto() != null) {
                    vVlPago += parcela.getVlPagto();
                }

                vlPago.setText(Numero.doubleToReal(vVlPago, 2));
                listParcela.add(parcelaHit);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void atualizaLista() {
        LayoutY = cdParcela.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listParcela.iterator();
        for (int i = 0; it.hasNext(); i++) {
            ParcelaHit b = (ParcelaHit) it.next();
            b.cdParcela.setEditable(cdParcela.isEditable());
            b.cdParcela.setPrefHeight(cdParcela.getHeight());
            b.cdParcela.setPrefWidth(cdParcela.getWidth());
            b.cdParcela.setLayoutX(cdParcela.getLayoutX());
            b.cdParcela.setLayoutY(LayoutY);
            b.cdParcela.getStyleClass().addAll(this.cdParcela.getStyleClass());
            b.dtVencto.setEditable(dtVencto.isEditable());
            b.dtVencto.setPrefHeight(dtVencto.getHeight());
            b.dtVencto.setPrefWidth(dtVencto.getWidth());
            b.dtVencto.setLayoutX(dtVencto.getLayoutX());
            b.dtVencto.setLayoutY(LayoutY);
            b.dtVencto.getStyleClass().addAll(this.dtVencto.getStyleClass());
            b.vlParcela.setEditable(vlParcela.isEditable());
            b.vlParcela.setPrefHeight(vlParcela.getHeight());
            b.vlParcela.setPrefWidth(vlParcela.getWidth());
            b.vlParcela.setLayoutX(vlParcela.getLayoutX());
            b.vlParcela.setLayoutY(LayoutY);
            b.vlParcela.getStyleClass().addAll(this.vlParcela.getStyleClass());
            b.tpMovto.setEditable(tpMovto.isEditable());
            b.tpMovto.setPrefHeight(tpMovto.getHeight());
            b.tpMovto.setPrefWidth(tpMovto.getWidth());
            b.tpMovto.setLayoutX(tpMovto.getLayoutX());
            b.tpMovto.setLayoutY(LayoutY);
            b.tpMovto.getStyleClass().addAll(this.tpMovto.getStyleClass());
            b.vlPagamento.setEditable(vlPagamento.isEditable());
            b.vlPagamento.setPrefHeight(vlPagamento.getHeight());
            b.vlPagamento.setPrefWidth(vlPagamento.getWidth());
            b.vlPagamento.setLayoutX(vlPagamento.getLayoutX());
            b.vlPagamento.setLayoutY(LayoutY);
            b.vlPagamento.getStyleClass().addAll(this.vlPagamento.getStyleClass());
            b.tpForPagto.setDisable(tpForPagto.isDisable());
            b.tpForPagto.setPrefHeight(dtPagamento.getHeight());
            b.tpForPagto.setPrefWidth(tpForPagto.getWidth());
            b.tpForPagto.setLayoutX(tpForPagto.getLayoutX());
            b.tpForPagto.setLayoutY(LayoutY);
            b.tpForPagto.getStyleClass().addAll(this.tpForPagto.getStyleClass());
            b.dtPagamento.setEditable(dtPagamento.isEditable());
            b.dtPagamento.setPrefHeight(dtPagamento.getHeight());
            b.dtPagamento.setPrefWidth(dtPagamento.getWidth());
            b.dtPagamento.setLayoutX(dtPagamento.getLayoutX());
            b.dtPagamento.setLayoutY(LayoutY);
            b.dtPagamento.getStyleClass().addAll(this.dtPagamento.getStyleClass());
            b.vlDesconto.setEditable(vlDesconto.isEditable());
            b.vlDesconto.setPrefHeight(vlDesconto.getHeight());
            b.vlDesconto.setPrefWidth(vlDesconto.getWidth());
            b.vlDesconto.setLayoutX(vlDesconto.getLayoutX());
            b.vlDesconto.setLayoutY(LayoutY);
            b.vlDesconto.getStyleClass().addAll(this.vlDesconto.getStyleClass());
            b.vlMulta.setEditable(vlMulta.isEditable());
            b.vlMulta.setPrefHeight(vlMulta.getHeight());
            b.vlMulta.setPrefWidth(vlMulta.getWidth());
            b.vlMulta.setLayoutX(vlMulta.getLayoutX());
            b.vlMulta.setLayoutY(LayoutY);
            b.vlMulta.getStyleClass().addAll(this.vlMulta.getStyleClass());
            b.inCancelada.setPrefHeight(inCancelada.getHeight());
            b.inCancelada.setPrefWidth(inCancelada.getWidth());
            b.inCancelada.setLayoutX(inCancelada.getLayoutX());
            b.inCancelada.setText(inCancelada.getText());
            b.inCancelada.setLayoutY(LayoutY + 5);
            b.inCancelada.getStyleClass().addAll(this.inCancelada.getStyleClass());
            b.lblMovtoParcela.setPrefHeight(lblMovtoParcela.getHeight());
            b.lblMovtoParcela.setPrefWidth(lblMovtoParcela.getWidth());
            b.lblMovtoParcela.setLayoutX(lblMovtoParcela.getLayoutX());
            b.lblMovtoParcela.setLayoutY(LayoutY + 30);
            b.lblMovtoParcela.getStyleClass().addAll(this.lblMovtoParcela.getStyleClass());
            b.btnAdd.setPrefHeight(btnAdd.getHeight());
            b.btnAdd.setPrefWidth(btnAdd.getWidth());
            b.btnAdd.setLayoutX(btnAdd.getLayoutX());
            b.btnAdd.setLayoutY(LayoutY);
            IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
            b.btnEstornar.setPrefHeight(btnEstornar.getHeight());
            b.btnEstornar.setPrefWidth(btnEstornar.getWidth());
            b.btnEstornar.setText(btnEstornar.getText());
            b.btnEstornar.setLayoutX(btnEstornar.getLayoutX());
            b.btnEstornar.setLayoutY(LayoutY);
            IconButtonHit.setIconComTexto(b.btnEstornar, IconButtonHit.ICON_EXCLUIR);

            if (!conta.getStConta().equals("1")) {//Conta não pendente
                b.btnAdd.setDisable(true);
                b.inCancelada.setDisable(true);
                b.btnEstornar.setDisable(true);
            }

            if (b.parcela == null) { //caso seja uma nova parcela
                b.dtVencto.setEditable(true);
                b.dtVencto.getStyleClass().remove("texto_estatico_center");
                b.dtVencto.getStyleClass().addAll("texto_center");
                b.vlParcela.setEditable(true);
                b.vlParcela.getStyleClass().remove("numero_estatico");
                b.vlParcela.getStyleClass().addAll("numero_editavel");
                b.inCancelada.setDisable(true);
                b.btnEstornar.setDisable(true);
            } else {
                if (b.parcela.getInCancelada()) {
                    b.btnAdd.setDisable(true);
                    b.inCancelada.setDisable(true);
                    b.btnEstornar.setDisable(true);
                } else {
                    if (b.parcela.getDtPagto() != null) {
                        b.inCancelada.setDisable(true);
                    } else {
                        b.btnEstornar.setDisable(true);
                    }
                }
            }

            painel.getChildren().add(b.cdParcela);
            painel.getChildren().add(b.dtVencto);
            painel.getChildren().add(b.vlParcela);
            painel.getChildren().add(b.tpMovto);
            painel.getChildren().add(b.vlPagamento);
            painel.getChildren().add(b.dtPagamento);
            painel.getChildren().add(b.tpForPagto);
            painel.getChildren().add(b.vlDesconto);
            painel.getChildren().add(b.vlMulta);
            painel.getChildren().add(b.inCancelada);
            painel.getChildren().add(b.btnAdd);
            painel.getChildren().add(b.btnEstornar);
            painel.getChildren().add(b.lblMovtoParcela);
            LayoutY += (cdParcela.getHeight() + 17);
            addValidacao(b, i, listParcela.size());
        }
        painel.setPrefHeight(LayoutY + 10);
    }

    public void addValidacao(ParcelaHit parcelaHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroDecimal(parcelaHit.vlMulta);
        FuncaoCampo.mascaraNumeroDecimal(parcelaHit.vlDesconto);
        TrataCombo.setValueComboTpFormaPagto(parcelaHit.tpForPagto, null);

        parcelaHit.dtVencto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.dtVencto.getText().equals("")) {
                    try {
                        Data.autoComplete(parcelaHit.dtVencto);
                        Date dataInicio = Data.StringToDate(parcelaHit.dtVencto.getText() + " 23:59:59");
                        if (dataInicio.before(new Date()) && !dataInicio.equals(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data de Vencimento não pode ser menor que a data atual.");
                            parcelaHit.dtVencto.requestFocus();
                            return;
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        parcelaHit.dtVencto.requestFocus();
                    }
                }
            }
        });

        parcelaHit.dtPagamento.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.dtPagamento.getText().equals("")) {
                    try {
                        Data.autoComplete(parcelaHit.dtPagamento);
                        Date dataInicio = Data.StringToDate(parcelaHit.dtPagamento.getText());
                        if (dataInicio.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data de Pagamento não pode ser maior que a data atual.");
                            parcelaHit.dtPagamento.requestFocus();
                            return;
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        parcelaHit.dtPagamento.requestFocus();
                    }
                }
            }
        });

        parcelaHit.vlParcela.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlParcela.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlParcela.getText());
                    parcelaHit.vlParcela.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });

        parcelaHit.vlMulta.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlMulta.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlMulta.getText());
                    parcelaHit.vlMulta.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });

        parcelaHit.vlDesconto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlDesconto.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlDesconto.getText());
                    parcelaHit.vlDesconto.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });

        parcelaHit.vlMulta.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlDesconto.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlDesconto.getText());
                    parcelaHit.vlDesconto.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });

        parcelaHit.btnAdd.setOnAction((ActionEvent event) -> {
            ParcelaHit b = new ParcelaHit();
            listParcela.add(posicao + 1, b);
            atualizaLista();
        });

        parcelaHit.btnEstornar.setOnAction((ActionEvent event) -> {
            if (parcelaHit.parcela == null) {
                listParcela.remove(parcelaHit);
            } else {
                if (parcelaHit.parcela.getDtPagto() != null) {
                    //estorno
                }
            }
            atualizaLista();
        });
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class ParcelaHit {

        Parcela parcela;
        TextField cdParcela = new TextField();
        TextField dtVencto = new TextField();
        TextField vlParcela = new TextField();
        TextField tpMovto = new TextField();
        ChoiceBox tpForPagto = new ChoiceBox();
        TextField dtPagamento = new TextField();
        TextField vlPagamento = new TextField();
        TextField vlDesconto = new TextField();
        TextField vlMulta = new TextField();
        CheckBox inCancelada = new CheckBox();
        Button btnAdd = new Button();
        Button btnEstornar = new Button();
        Label lblMovtoParcela = new Label();

    }

}
