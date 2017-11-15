/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.AjusteCaixa;
import br.integrado.jnpereira.nutrimix.modelo.FechamentoCaixa;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jose Vinicius
 */
public class AjusteCaixaControl implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdAjuste;
    @FXML
    TextField dtAjuste;
    @FXML
    ChoiceBox tpAjuste;
    @FXML
    ChoiceBox tpForPagto;
    @FXML
    TextField vlAjuste;
    @FXML
    TextArea dsObs;
    @FXML
    Label lblCadastro;

    Dao dao = new Dao();
    AjusteCaixa ajuste;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdAjuste);
        FuncaoCampo.mascaraNumeroDecimal(vlAjuste);
        FuncaoCampo.mascaraTexto(dsObs, 150);
        TrataCombo.setValueComboTpAjustCaixa(tpAjuste, null);
        TrataCombo.setValueComboTpFormaPagto(tpForPagto, null);
        cdAjuste.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAjuste();
            }
        });
        vlAjuste.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!vlAjuste.getText().equals("")) {
                    Double valor = Double.parseDouble(vlAjuste.getText());
                    vlAjuste.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });
        dtAjuste.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        dao.autoCommit(false);
    }

    public void iniciaTela() {

    }

    public void validaCodigoAjuste() {
        if (!cdAjuste.getText().equals("") & ajuste == null) {
            try {
                ajuste = new AjusteCaixa();
                ajuste.setCdAjuste(Integer.parseInt(cdAjuste.getText()));
                dao.get(ajuste);

                dtAjuste.setText(Data.AmericaToBrasilSemHora(ajuste.getDtCadastro()));
                TrataCombo.setValueComboTpAjustCaixa(tpAjuste, Integer.parseInt(ajuste.getTpAjuste()));
                TrataCombo.setValueComboTpFormaPagto(tpForPagto, ajuste.getCdForPagto());
                vlAjuste.setText(Numero.doubleToReal(ajuste.getVlAjuste(), 2));
                dsObs.setText(ajuste.getDsObs());
                lblCadastro.setText(Numero.getCadastro(ajuste.getCdUserCad(), ajuste.getDtCadastro()));

                cdAjuste.setEditable(false);
                vlAjuste.setEditable(false);
                dsObs.setEditable(false);
                tpAjuste.setDisable(true);
                tpForPagto.setDisable(true);
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                ajuste = null;
                cdAjuste.requestFocus();
            }
        }
    }

    @FXML
    public void abrirListaAjustCaixa() {

    }

    @FXML
    public void salvar() {
        if (ajuste != null) {
            Alerta.AlertaError("Negado!", "Alterações não são permitidas");
            return;
        }

        if (TrataCombo.getValueComboTpAjustCaixa(tpAjuste) == null) {
            Alerta.AlertaError("Campo inválido", "Tipo de Ajuste é obrigatório.");
            return;
        }
        
         if (TrataCombo.getValueComboTpFormaPagto(tpForPagto) == null) {
            Alerta.AlertaError("Campo inválido", "Forma de Pagamento é obrigatório.");
            return;
        }

        if (vlAjuste.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Valor do ajuste é obrigatório.");
            return;
        }

        CaixaControl caixa = new CaixaControl();
        FechamentoCaixa fechamento;
        try {
            fechamento = caixa.getCaixaAberto(Data.getAgora()); //pega caixa aberto
        } catch (Exception ex) {
            Alerta.AlertaWarning("Negado!", ex.getMessage());
            return;
        }

        ajuste = new AjusteCaixa();
        ajuste.setTpAjuste(TrataCombo.getValueComboTpAjustCaixa(tpAjuste).toString());
        ajuste.setCdForPagto(TrataCombo.getValueComboTpFormaPagto(tpForPagto));
        ajuste.setVlAjuste(Double.parseDouble(vlAjuste.getText()));
        ajuste.setDsObs(dsObs.getText());
        ajuste.setCdUserCad(MenuControl.usuarioAtivo);
        ajuste.setDtCadastro(Data.getAgora());
        try {
            dao.save(ajuste);
            caixa.geraMovtoAjuste(ajuste, fechamento);
            dao.commit();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao salvar registro.\n" + ex.toString());
            ajuste = null;
            return;
        }
        cdAjuste.setText(ajuste.getCdAjuste().toString());
        ajuste = null;
        validaCodigoAjuste();

        Alerta.AlertaInfo("Concluído", "Registro salvo com sucesso!");
    }

    @FXML
    public void limpar() {
        ajuste = null;
        FuncaoCampo.limparCampos(anchor);
        cdAjuste.setEditable(true);
        vlAjuste.setEditable(true);
        dsObs.setEditable(true);
        tpAjuste.setDisable(false);
        tpForPagto.setDisable(false);
        lblCadastro.setText("");
        cdAjuste.requestFocus();
    }

}
