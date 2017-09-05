/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FrmCadAjustEstoqFXML implements Initializable {

    @FXML
    TextField cdAjuste;
    @FXML
    TextField dtAjuste;
    @FXML
    ChoiceBox tpAjuste;
    @FXML
    TextField cdProduto;
    @FXML
    Button btnPesqProd;
    @FXML
    TextField dsProduto;
    @FXML
    TextField qtAjuste;
    @FXML
    TextField vlItem;
    @FXML
    TextField vlTotalItem;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    @FXML
    TextArea dsObs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumero(cdAjuste);       
        dtAjuste.setText(Data.AmericaToBrasilSemHora(Data.getAgora()));
        TrataCombo.setValueComboTpAjustEstoq(tpAjuste, null);
        FuncaoCampo.mascaraTexto(dsObs, 150);
    }

    public void iniciaTela() {
    }

}
