/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Cliente;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class FrmCadVendaFXML implements Initializable {

    @FXML
    TextField cdCliente;
    @FXML
    TextField dsCliente;
    @FXML
    TextField nrCpfCnpj;

    public Stage stage;
    public Object param;

    private final Dao dao = new Dao();
    private Pessoa pessoa;
    private Cliente cliente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdCliente);
        cdCliente.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCliente();
            }
        });
    }

    public void iniciaTela() {

    }

    @FXML
    public void pesquisarCliente() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Cliente(), false);
        if (valor != null) {
            cdCliente.setText(valor);
            validaCodigoCliente();
        }
    }

    private void validaCodigoCliente() {
        if (!cdCliente.getText().equals("")) {
            boolean vInBusca = true;
            if (cliente != null) {
                if (cliente.getCdCliente() == Integer.parseInt(cdCliente.getText())) {
                    vInBusca = false;
                }
            }
            if (vInBusca) {
                try {
                    cliente = new Cliente();
                    cliente.setCdCliente(Integer.parseInt(cdCliente.getText()));
                    dao.get(cliente);
                    if (!cliente.getInAtivo()) {
                        Alerta.AlertaError("Cliente inválido!", "Cliente está inativo.");
                        cliente = null;
                        dsCliente.setText("");
                        nrCpfCnpj.setText("");
                        cdCliente.requestFocus();
                        return;
                    }
                    pessoa = new Pessoa();
                    pessoa.setCdPessoa(cliente.getCdPessoa());
                    dao.get(pessoa);
                    dsCliente.setText(pessoa.getDsPessoa());
                    if (pessoa.getTpPessoa().equals("F")) {
                        nrCpfCnpj.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    } else {
                        nrCpfCnpj.setText(Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()));
                    }
                } catch (Exception ex) {
                    Alerta.AlertaError("Notificação", ex.getMessage());
                    cliente = null;
                    pessoa = null;
                    dsCliente.setText("");
                    nrCpfCnpj.setText("");
                    cdCliente.requestFocus();
                }
            }
        } else {
            cliente = null;
            pessoa = null;
            dsCliente.setText("");
            nrCpfCnpj.setText("");
        }
    }

}
