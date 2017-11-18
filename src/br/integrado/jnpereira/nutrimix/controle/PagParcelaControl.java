package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.Parcela;
import br.integrado.jnpereira.nutrimix.relatorio.Relatorio;
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
    TextField stConta;
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
    @FXML
    Label lblParcelaEstornada;

    public Stage stage;
    public Object param;
    double LayoutY;
    ArrayList<ParcelaHit> listParcela = new ArrayList<>();
    Dao dao = new Dao();
    ContasPagarReceber conta;
    boolean vInRecibo = false;

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
                vInRecibo = true;
            } else if (conta.getCdMovto() != null && conta.getTpMovto().equals("S")) {
                tpConta.setText(TrataCombo.getTpConta(1));
            }
            stConta.setText(TrataCombo.getTpSitConta(Integer.parseInt(conta.getStConta())));
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

        double vVlTotal = 0.00;
        double vVlTotalDesconto = 0.00;
        double vVlTotalMulta = 0.00;

        for (ParcelaHit parcelaHit : listParcela) {

            if (!parcelaHit.inCancelada.isSelected()) {
                if (parcelaHit.parcela != null) {
                    if (parcelaHit.parcela.getTpMovto().equals(conta.getTpMovto())) {
                        vVlTotal += Double.parseDouble(parcelaHit.vlParcela.getText());
                    } else {
                        vVlTotal -= Double.parseDouble(parcelaHit.vlParcela.getText()); //parcela de estorno
                    }
                } else {
                    vVlTotal += Double.parseDouble(parcelaHit.vlParcela.getText());
                }
            }

            if (parcelaHit.parcela == null) {
                if (parcelaHit.vlParcela.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido", "Valor da parcela é obrigatória.");
                    parcelaHit.vlParcela.requestFocus();
                    return;
                }

                if (parcelaHit.dtVencto.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido", "Data de vencimento da parcela é obrigatória.");
                    parcelaHit.dtVencto.requestFocus();
                    return;
                }
            } else {

            }

            if (TrataCombo.getValueComboTpFormaPagto(parcelaHit.tpForPagto) != null
                    || !parcelaHit.dtPagamento.getText().equals("")
                    || !parcelaHit.vlDesconto.getText().equals("")
                    || !parcelaHit.vlMulta.getText().equals("")) {

                if (parcelaHit.dtPagamento.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido", "Para pagamento de parcela data de pagamento é obrigatória.");
                    parcelaHit.dtPagamento.requestFocus();
                    return;
                }

                if (TrataCombo.getValueComboTpFormaPagto(parcelaHit.tpForPagto) == null) {
                    Alerta.AlertaError("Campo inválido", "Para pagamento de parcela forma de pagamento é obrigatória.");
                    parcelaHit.tpForPagto.requestFocus();
                    return;
                }
                if (parcelaHit.parcela.getTpMovto().equals(conta.getTpMovto())) { //Não precisao se for parcela de estorno
                    double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());

                    double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                    vVlTotalDesconto += vVlDesconto;
                    double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                    vVlTotalMulta += vVlMulta;

                    vVlParcela = vVlParcela - vVlDesconto;
                    vVlParcela = vVlParcela + vVlMulta;
                    if (vVlParcela < 0) {
                        Alerta.AlertaError("Campo inválido", "Parcela com data de pagamento " + parcelaHit.dtPagamento.getText() + " está com valor de pagamento inferior a R$ 0.00.");
                        parcelaHit.vlDesconto.requestFocus();
                        return;
                    }
                }
            }

        }

        vVlTotal = vVlTotal - vVlTotalDesconto;
        vVlTotal = vVlTotal + vVlTotalMulta;
        String vDsTotal = Numero.doubleToReal(vVlTotal, 2);
        String vDsTotalConta = Numero.doubleToReal((Double.parseDouble(vlTotal.getText()) + vVlTotalMulta) - vVlTotalDesconto, 2);
        if (!vDsTotal.equals(vDsTotalConta)) {
            Alerta.AlertaError("Campo inválido", "Total das parcelas não confere com o total da conta.\n"
                    + "Total Parcela: " + Numero.doubleToReal(vVlTotal, 2));
            return;
        }

        try {
            dao.autoCommit(false);
            CaixaControl caixa = new CaixaControl();
            FechamentoCaixa fechamento;
            try {
                fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
            } catch (Exception ex) {
                Alerta.AlertaWarning("Negado!", ex.getMessage());
                return;
            }

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
                    if (!parcelaHit.dtPagamento.getText().equals("")) {
                        parcelaHit.parcela.setDtPagto(Data.StringToDate(parcelaHit.dtPagamento.getText()));
                        double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());
                        double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                        double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                        vVlParcela = vVlParcela - vVlDesconto;
                        vVlParcela = vVlParcela + vVlMulta;
                        parcelaHit.parcela.setVlPagto(vVlParcela);
                        parcelaHit.parcela.setVlDesconto(vVlDesconto);
                        parcelaHit.parcela.setVlMulta(vVlMulta);
                        parcelaHit.parcela.setCdForPagto(TrataCombo.getValueComboTpFormaPagto(parcelaHit.tpForPagto));
                        vInPag = true;
                    }
                    parcelaHit.parcela.setInCancelada(false);
                    parcelaHit.parcela.setCdUserMovto(MenuControl.usuarioAtivo);
                    parcelaHit.parcela.setDtUltMovto(Data.getAgora());
                    dao.save(parcelaHit.parcela);
                } else {
                    if (!parcelaHit.dtPagamento.getText().equals("")
                            && parcelaHit.parcela.getDtPagto() == null) {
                        parcelaHit.parcela.setDtPagto(Data.StringToDate(parcelaHit.dtPagamento.getText()));
                        double vVlParcela = Double.parseDouble(parcelaHit.vlParcela.getText());
                        double vVlDesconto = converteDouble(parcelaHit.vlDesconto.getText());
                        double vVlMulta = converteDouble(parcelaHit.vlMulta.getText());
                        vVlParcela = vVlParcela - vVlDesconto;
                        vVlParcela = vVlParcela + vVlMulta;
                        parcelaHit.parcela.setVlPagto(vVlParcela);
                        parcelaHit.parcela.setVlDesconto(vVlDesconto);
                        parcelaHit.parcela.setVlMulta(vVlMulta);
                        parcelaHit.parcela.setCdForPagto(TrataCombo.getValueComboTpFormaPagto(parcelaHit.tpForPagto));
                        vInPag = true;
                        dao.update(parcelaHit.parcela);
                    }
                    if (parcelaHit.parcela.getInCancelada() != parcelaHit.inCancelada.isSelected()) {
                        parcelaHit.parcela.setInCancelada(parcelaHit.inCancelada.isSelected());
                        dao.update(parcelaHit.parcela);
                    }
                }
                if (vInPag) {
                    caixa.geraMovtoCaixaParcela(parcelaHit.parcela, fechamento);
                }
            }

            ParcelaControl parcelaControl = new ParcelaControl();
            parcelaControl.encerrarConta(conta);

            dao.commit();
            validaCodigoConta();

            if (vInRecibo) {
                Relatorio relatorio = new Relatorio();
                relatorio.gerarReciboVenda(conta.getCdMovto());
            }
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
            String where = "WHERE $cdConta$ = " + conta.getCdConta() + " ORDER BY $cdParcela$ ASC";
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
                TrataCombo.setValueComboTpFormaPagto(parcelaHit.tpForPagto, parcela.getCdForPagto());
                parcelaHit.lblMovtoParcela.setText(Numero.getCadastro(parcela.getCdUserMovto(), parcela.getDtUltMovto()));
                parcelaHit.parcela = parcela;

                if (parcela.getVlPagto() != null) {
                    if (parcela.getTpMovto().equals(conta.getTpMovto())) {
                        vVlPago += parcela.getVlPagto(); //Parcela normal
                    } else {
                        vVlPago -= parcela.getVlPagto();//Parcela estorno
                    }
                }

                vlPago.setText(Numero.doubleToReal(vVlPago, 2));

                where = "WHERE $cdConta$ = " + conta.getCdConta() + " AND $cdParEstorno$ = " + parcela.getCdParcela();
                long vCdParEstorno = dao.getCountWhere(new Parcela(), where);
                if (vCdParEstorno > 0) {
                    parcelaHit.lblParcelaEstornada.setText("Parcela Estornada");
                }

                if (parcela.getCdParEstorno() != null) {
                    parcelaHit.lblParcelaEstornada.setText("Estorno Parcela: " + parcela.getCdParEstorno());
                }

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
            b.lblParcelaEstornada.setPrefHeight(lblParcelaEstornada.getHeight());
            b.lblParcelaEstornada.setPrefWidth(lblParcelaEstornada.getWidth());
            b.lblParcelaEstornada.setLayoutX(lblParcelaEstornada.getLayoutX());
            b.lblParcelaEstornada.setLayoutY(LayoutY + 30);
            b.lblParcelaEstornada.getStyleClass().addAll(this.lblParcelaEstornada.getStyleClass());
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
            } else {
                if (b.parcela.getInCancelada()) {
                    b.btnAdd.setDisable(true);
                    b.inCancelada.setDisable(true);
                    b.btnEstornar.setDisable(true);
                    b.tpForPagto.setDisable(true);
                    b.dtPagamento.setEditable(false);
                    b.dtPagamento.getStyleClass().remove("texto_center");
                    b.dtPagamento.getStyleClass().addAll("texto_estatico_center");
                    b.vlDesconto.setEditable(false);
                    b.vlDesconto.getStyleClass().remove("numero_editavel");
                    b.vlDesconto.getStyleClass().addAll("numero_estatico");
                    b.vlMulta.setEditable(false);
                    b.vlMulta.getStyleClass().remove("numero_editavel");
                    b.vlMulta.getStyleClass().addAll("numero_estatico");
                } else {
                    if (b.parcela.getDtPagto() != null) {
                        b.btnEstornar.setDisable(false);
                        b.inCancelada.setDisable(true);
                        b.tpForPagto.setDisable(true);
                        b.dtPagamento.setEditable(false);
                        b.dtPagamento.getStyleClass().remove("texto_center");
                        b.dtPagamento.getStyleClass().addAll("texto_estatico_center");
                        b.vlDesconto.setEditable(false);
                        b.vlDesconto.getStyleClass().remove("numero_editavel");
                        b.vlDesconto.getStyleClass().addAll("numero_estatico");
                        b.vlMulta.setEditable(false);
                        b.vlMulta.getStyleClass().remove("numero_editavel");
                        b.vlMulta.getStyleClass().addAll("numero_estatico");
                    } else {
                        try {
                            Date vDtHoje = Data.StringToDate(Data.AmericaToBrasilSemHora(Data.getAgora()) + " 00:00:00");
                            if (Data.StringToDate(b.dtVencto.getText()).before(vDtHoje)) {
                                b.dtVencto.getStyleClass().remove("texto_estatico_center");
                                b.dtVencto.getStyleClass().addAll("texto_estatico_center_vermelho");
                            }
                        } catch (Exception ex) {
                        }
                        b.btnEstornar.setDisable(true);
                    }
                }
            }

            if (!b.lblParcelaEstornada.getText().equals("")) {
                b.inCancelada.setDisable(true);
                b.btnEstornar.setDisable(true);
            }

            painel.getChildren().add(b.cdParcela);
            painel.getChildren().add(b.dtVencto);
            painel.getChildren().add(b.vlParcela);
            painel.getChildren().add(b.tpMovto);
            painel.getChildren().add(b.vlPagamento);
            painel.getChildren().add(b.tpForPagto);
            painel.getChildren().add(b.dtPagamento);
            painel.getChildren().add(b.vlDesconto);
            painel.getChildren().add(b.vlMulta);
            painel.getChildren().add(b.inCancelada);
            painel.getChildren().add(b.btnAdd);
            painel.getChildren().add(b.btnEstornar);
            painel.getChildren().add(b.lblMovtoParcela);
            painel.getChildren().add(b.lblParcelaEstornada);
            LayoutY += (cdParcela.getHeight() + 25);
            addValidacao(b, i, listParcela.size());
        }
        painel.setPrefHeight(LayoutY + 10);
    }

    private void calculaPago(ParcelaHit parcelaHit) {
        if (parcelaHit.vlDesconto.getText().equals("") && parcelaHit.vlMulta.getText().equals("")) {
            parcelaHit.vlPagamento.setText("");
        } else {
            double vVlPago = 0.00;
            vVlPago += converteDouble(parcelaHit.vlParcela.getText());
            vVlPago -= converteDouble(parcelaHit.vlDesconto.getText());
            vVlPago += converteDouble(parcelaHit.vlMulta.getText());
            parcelaHit.vlPagamento.setText(Numero.doubleToReal(vVlPago, 2));
        }
    }

    public void addValidacao(ParcelaHit parcelaHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroDecimal(parcelaHit.vlParcela);
        FuncaoCampo.mascaraNumeroDecimal(parcelaHit.vlMulta);
        FuncaoCampo.mascaraNumeroDecimal(parcelaHit.vlDesconto);
       // TrataCombo.setValueComboTpFormaPagto(parcelaHit.tpForPagto, null);

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
                calculaPago(parcelaHit);
            }
        });

        parcelaHit.vlMulta.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlMulta.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlMulta.getText());
                    parcelaHit.vlMulta.setText(Numero.doubleToReal(valor, 2));
                }
                calculaPago(parcelaHit);
            }
        });

        parcelaHit.vlDesconto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!parcelaHit.vlDesconto.getText().equals("")) {
                    Double valor = Double.parseDouble(parcelaHit.vlDesconto.getText());
                    parcelaHit.vlDesconto.setText(Numero.doubleToReal(valor, 2));
                }
                calculaPago(parcelaHit);
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
                    try { //Estorno de Parcela
                        if (Alerta.AlertaConfirmation("Confirmação", "Deseja estornar está parcela? todas as outras alterações desta tela serão descartadas.")) {
                            CaixaControl caixa = new CaixaControl();
                            FechamentoCaixa fechamento;
                            fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
                            ParcelaControl parcelaControl = new ParcelaControl();
                            parcelaControl.geraParcelaEstorno(parcelaHit.parcela, fechamento);
                            dao.commit();
                            validaCodigoConta();
                        }
                    } catch (Exception ex) {
                        dao.rollback();
                        validaCodigoConta();
                        Alerta.AlertaWarning("Negado!", ex.getMessage());
                        return;
                    }

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
        Label lblParcelaEstornada = new Label();
        
        public ParcelaHit(){
            TrataCombo.setValueComboTpFormaPagto(this.tpForPagto, null);
        }
    }

}
