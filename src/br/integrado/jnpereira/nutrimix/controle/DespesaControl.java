package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.ContasPagarReceber;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.modelo.Despesa;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.MovtoCaixa;
import br.integrado.jnpereira.nutrimix.modelo.TipoDespesa;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DespesaControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdDespesa;
    @FXML
    TextField dtDespesa;
    @FXML
    TextField vlDespesa;
    @FXML
    TextField cdTipoDesp;
    @FXML
    TextField dsTipoDesp;
    @FXML
    ChoiceBox tpCondPagto;
    @FXML
    ChoiceBox tpForPagto;
    @FXML
    TextArea dsObs;
    @FXML
    Label lblCadastro;
    @FXML
    Button btnExcluir;

    Dao dao = new Dao();
    Despesa despesa;
    ContasPagarReceber conta;
    public Stage stage;
    public Object param;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdDespesa);
        FuncaoCampo.mascaraData(dtDespesa);
        FuncaoCampo.mascaraNumeroInteiro(cdTipoDesp);
        FuncaoCampo.mascaraNumeroDecimal(vlDespesa);
        FuncaoCampo.mascaraTexto(dsObs, 150);
        TrataCombo.setValueComboTpCondicaoPagto(tpCondPagto, null);
        TrataCombo.setValueComboTpFormaPagto(tpForPagto, null);
        cdDespesa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoDespesa();
            }
        });
        dtDespesa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtDespesa.getText().equals("")) {
                    try {
                        Data.autoComplete(dtDespesa);
                        Date dataInicio = Data.StringToDate(dtDespesa.getText());
                        if (dataInicio.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Data da Despesa não pode ser maior que a data atual.");
                            dtDespesa.requestFocus();
                            return;
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtDespesa.requestFocus();
                    }
                }
            }
        });
        cdTipoDesp.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoTipoDespesa();
            }
        });
        vlDespesa.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlDespesa.getText().equals("")) {
                    Double valor = Double.parseDouble(vlDespesa.getText());
                    vlDespesa.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });
        dtDespesa.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        btnExcluir.setVisible(false);
        dao.autoCommit(false);
    }

    public void iniciaTela() {
        if (param != null) {
            Despesa aj = (Despesa) param;
            cdDespesa.setText(aj.getCdDespesa().toString());
            validaCodigoDespesa();
        }
    }

    public void validaCodigoDespesa() {
        if (!cdDespesa.getText().equals("") & despesa == null) {
            try {
                despesa = new Despesa();
                despesa.setCdDespesa(Integer.parseInt(cdDespesa.getText()));
                dao.get(despesa);
                dtDespesa.setText(Data.AmericaToBrasilSemHora(despesa.getDtDespesa()));
                vlDespesa.setText(Numero.doubleToReal(despesa.getVlDespesa(), 2));
                cdTipoDesp.setText(despesa.getCdTipoDespesa().toString());
                TipoDespesa tipo = new TipoDespesa();
                tipo.setCdTipoDespesa(despesa.getCdTipoDespesa());
                dao.get(tipo);
                dsTipoDesp.setText(tipo.getDsTipoDespesa());
                conta = (ContasPagarReceber) dao.getAllWhere(new ContasPagarReceber(), "WHERE $cdDespesa$ = " + despesa.getCdDespesa()).get(0);
                TrataCombo.setValueComboTpAjustCaixa(tpCondPagto, conta.getCdCondicao());
                TrataCombo.setValueComboTpFormaPagto(tpForPagto, conta.getCdForma());
                dsObs.setText(despesa.getDsObs());
                lblCadastro.setText(Numero.getCadastro(despesa.getCdUserCad(), despesa.getDtCadastro()));

                cdDespesa.setEditable(false);
                dtDespesa.setEditable(false);
                vlDespesa.setEditable(false);
                cdTipoDesp.setEditable(false);
                dsObs.setEditable(false);
                tpCondPagto.setDisable(true);
                tpForPagto.setDisable(true);
                btnExcluir.setVisible(true);
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                despesa = null;
                cdDespesa.requestFocus();
            }
        }
    }

    @FXML
    public void abrirListaDespesa() {
        Tela tela = new Tela();
        String valor = tela.abrirListaDespesa();
        if (valor != null) {
            cdDespesa.setText(valor);
            validaCodigoDespesa();
        }
    }

    private void validaCodigoTipoDespesa() {
        if (!cdTipoDesp.getText().equals("")) {
            try {
                TipoDespesa tipo = new TipoDespesa();
                tipo.setCdTipoDespesa(Integer.parseInt(cdTipoDesp.getText()));
                dao.get(tipo);
                if (!tipo.getInAtivo()) {
                    Alerta.AlertaError("Notificação", "Tipo de Despesa está inativa!");
                    cdTipoDesp.requestFocus();
                    return;
                }
                cdTipoDesp.setText(tipo.getCdTipoDespesa().toString());
                dsTipoDesp.setText(tipo.getDsTipoDespesa());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                cdTipoDesp.requestFocus();
            }
        }
    }

    @FXML
    public void abrirListaTipoDespesa() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new TipoDespesa(), "cdTipoDespesa", "dsTipoDespesa", " AND $inAtivo$ = 'T'", "Lista de Tipo de Despesa");
        if (valor != null) {
            cdTipoDesp.setText(valor);
            validaCodigoTipoDespesa();
        }
    }

    @FXML
    public void visualizarParcelas() {
        if (!vlDespesa.getText().equals("") && TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) != null) {
            if (despesa == null) {
                Double vlTotal = Double.parseDouble(vlDespesa.getText());
                if (vlTotal > 0 && TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) != null) {
                    ContasPagarReceber conta = new ContasPagarReceber();
                    conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
                    conta.setVlConta(vlTotal);
                    Tela tela = new Tela();
                    tela.abrirTelaModalComParam(stage, Tela.CON_PARCELA, conta);
                }
            } else {
                Tela tela = new Tela();
                tela.abrirTelaModalComParam(stage, Tela.PAG_PARCELA, conta);
            }
        }
    }

    @FXML
    public void salvar() {
        try {
            if (despesa != null) {
                Alerta.AlertaError("Negado!", "Não é possivel alterar uma despesa, exclua caso necessário.");
                return;
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao validar registro.\n" + ex.toString());
            despesa = null;
            return;
        }

        if (TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto) == null) {
            Alerta.AlertaError("Campo inválido", "Condição de Pagamento é obrigatório.");
            return;
        }

        if (TrataCombo.getValueComboTpFormaPagto(tpForPagto) == null) {
            Alerta.AlertaError("Campo inválido", "Forma de Pagamento é obrigatório.");
            return;
        }

        if (cdTipoDesp.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Tipo da despesa é obrigatório.");
            return;
        }

        if (dtDespesa.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Data da despesa é obrigatório.");
            return;
        }

        if (vlDespesa.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Valor da despesa é obrigatório.");
            return;
        } else {
            if (Double.parseDouble(vlDespesa.getText()) <= 0.00) {
                Alerta.AlertaError("Campo inválido", "Valor do despesa deve ser maior que zero.");
                return;
            }
        }

        CaixaControl caixa = new CaixaControl();
        FechamentoCaixa fechamento;
        try {
            fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
        } catch (Exception ex) {
            Alerta.AlertaWarning("Negado!", ex.getMessage());
            return;
        }
        try {

            despesa = new Despesa();
            despesa.setCdTipoDespesa(Integer.parseInt(cdTipoDesp.getText()));
            despesa.setDtDespesa(Data.StringToDate(dtDespesa.getText()));
            despesa.setVlDespesa(Double.parseDouble(vlDespesa.getText()));
            despesa.setDsObs(dsObs.getText());
            despesa.setCdUserCad(MenuControl.usuarioAtivo);
            despesa.setDtCadastro(Data.getAgora());
            dao.save(despesa);

            ContasPagarReceber conta = new ContasPagarReceber();
            conta.setTpMovto("S");
            conta.setDtMovto(Data.getAgora());
            conta.setCdCondicao(TrataCombo.getValueComboTpCondicaoPagto(tpCondPagto));
            conta.setCdForma(TrataCombo.getValueComboTpFormaPagto(tpForPagto));
            conta.setCdDespesa(despesa.getCdDespesa());
            conta.setVlConta(despesa.getVlDespesa());
            conta.setStConta("1");
            dao.save(conta);

            ParcelaControl parcela = new ParcelaControl();
            parcela.gerarParcelas(conta, fechamento);

            ParcelaControl parcelaControl = new ParcelaControl();
            parcelaControl.encerrarConta(conta);

            dao.commit();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao salvar registro.\n" + ex.toString());
            despesa = null;
            return;
        }
        cdDespesa.setText(despesa.getCdDespesa().toString());
        despesa = null;
        validaCodigoDespesa();

        Alerta.AlertaInfo("Concluído", "Registro salvo com sucesso!");
    }

    @FXML
    public void limpar() {
        despesa = null;
        FuncaoCampo.limparCampos(anchor);
        cdDespesa.setEditable(true);
        dtDespesa.setEditable(true);
        vlDespesa.setEditable(true);
        cdTipoDesp.setEditable(true);
        dsObs.setEditable(true);
        tpCondPagto.setDisable(false);
        tpForPagto.setDisable(false);
        btnExcluir.setVisible(false);
        lblCadastro.setText("");
        dtDespesa.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        cdDespesa.requestFocus();
    }

    @FXML
    public void excluir() {
        try {
            if (Alerta.AlertaConfirmation("Confirmação", "Deseja realmente excluir está despesa?")) {
                CaixaControl caixa = new CaixaControl();
                caixa.excluirDespesa(despesa);
                dao.commit();
                limpar();
                Alerta.AlertaError("Aviso!", "Despesa removida com sucesso!");
            }
        } catch (Exception ex) {
            dao.rollback();
            Alerta.AlertaError("Aviso!", ex.getMessage());
        }
    }
}
