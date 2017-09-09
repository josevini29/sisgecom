/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.GrupoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.modelo.UnidadePadrao;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FrmCadProdutoFXML implements Initializable {

    Dao dao = new Dao();
    Produto produto;

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdProduto;
    @FXML
    TextField dsProduto;
    @FXML
    CheckBox inAtivo;
    @FXML
    TextField cdGrupoProd;
    @FXML
    TextField dsGrupoProd;
    @FXML
    TextField cdUnidPad;
    @FXML
    TextField dsUnidPad;
    @FXML
    TextField cdUnidPadComp;
    @FXML
    TextField dsUnidPadComp;
    @FXML
    TextField qtConversao;
    @FXML
    TextField qtEstoqMin;
    @FXML
    TextField qtEstoqAtual;
    @FXML
    TextField vlCustoMedio;
    @FXML
    CheckBox inEstoque;
    @FXML
    CheckBox inConsumo;
    @FXML
    CheckBox inVenda;
    @FXML
    Label lblCadastro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdProduto);
        FuncaoCampo.mascaraNumeroInteiro(cdGrupoProd);
        FuncaoCampo.mascaraNumeroDecimal(qtEstoqMin);
        FuncaoCampo.mascaraNumeroDecimal(qtConversao);
        FuncaoCampo.mascaraTexto(dsProduto, 250);
        FuncaoCampo.mascaraTexto(cdUnidPad, 10);
        FuncaoCampo.mascaraTexto(cdUnidPadComp, 10);
        cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto();
            }
        });

        cdGrupoProd.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoGrupoProd();
            }
        });

        cdUnidPad.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoUnidPad();
            }
        });

        cdUnidPadComp.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoUnidPadComp();
            }
        });
    }

    public void iniciaTela() {

    }
    
    @FXML
    public void salvarProduto() {
        if (dsProduto.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo descrição do produto é obrigatório.");
            return;
        }

        if (cdGrupoProd.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Grupo do produto é obrigatório.");
            return;
        }

        if (cdUnidPad.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Unidade Padrão do produto é obrigatório.");
            return;
        }

        if (cdUnidPadComp.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Unidade Padrão de Compra do produto é obrigatório.");
            return;
        }

        if (qtConversao.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Qt. Conversão do produto é obrigatório.");
            return;
        }

        if (qtEstoqMin.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Qt. Estoque Mínimo do produto é obrigatório.");
            return;
        }

        if (produto == null) {
            produto = new Produto();
            produto.setDsProduto(dsProduto.getText());
            produto.setCdGrupo(Integer.parseInt(cdGrupoProd.getText()));
            produto.setCdUndPadrao(cdUnidPad.getText().toUpperCase());
            produto.setCdUndPadraoCompra(cdUnidPadComp.getText().toUpperCase());
            produto.setQtConversao(Numero.RealToDouble(qtConversao.getText()));
            produto.setQtEstoqMin(Numero.RealToDouble(qtEstoqMin.getText()));
            produto.setInEstoque(inEstoque.isSelected());
            produto.setInConsumo(inConsumo.isSelected());
            produto.setInVenda(inVenda.isSelected());
            produto.setInAtivo(inAtivo.isSelected());
            produto.setQtEstqAtual(0.00);
            produto.setVlCustoMedio(0.00);
            produto.setCdUsuario(FrmMenuFXML.usuarioAtivo);
            produto.setDtCadastro(Data.getAgora());
            try {
                dao.save(produto);
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao salvar produto.\n" + ex.toString());
                produto = null;
                return;
            }
            cdProduto.setText(String.valueOf(produto.getCdProduto()));
            produto = null;
            validaCodigoProduto();
        } else {
            produto.setDsProduto(dsProduto.getText());
            produto.setCdGrupo(Integer.parseInt(cdGrupoProd.getText()));
            produto.setCdUndPadrao(cdUnidPad.getText().toUpperCase());
            produto.setCdUndPadraoCompra(cdUnidPadComp.getText().toUpperCase());
            produto.setQtConversao(Numero.RealToDouble(qtConversao.getText()));
            produto.setQtEstoqMin(Numero.RealToDouble(qtEstoqMin.getText()));
            produto.setInEstoque(inEstoque.isSelected());
            produto.setInConsumo(inConsumo.isSelected());
            produto.setInVenda(inVenda.isSelected());
            produto.setInAtivo(inAtivo.isSelected());
            try {
                dao.update(produto);
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao alterar produto.\n" + ex.toString());
                return;
            }
            cdProduto.setText(String.valueOf(produto.getCdProduto()));
            produto = null;
            validaCodigoProduto();
        }
        Alerta.AlertaInfo("Concluído", "Produto salvo com sucesso!");
    }

    @FXML
    public void limparProduto() {
        limparTela();
    }

    @FXML
    public void pesquisarProduto() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", null, "Lista de Produtos");
        if (valor != null) {
            limparTela();
            cdProduto.setText(valor);
            validaCodigoProduto();
        }
    }

    private void validaCodigoProduto() {
        if (!cdProduto.getText().equals("") & produto == null) {
            try {
                produto = new Produto();
                produto.setCdProduto(Integer.parseInt(cdProduto.getText()));
                dao.get(produto);

                dsProduto.setText(produto.getDsProduto());
                inAtivo.setSelected(produto.getInAtivo());

                GrupoProduto grupoProd = new GrupoProduto();
                grupoProd.setCdGrupo(produto.getCdGrupo());
                dao.get(grupoProd);
                cdGrupoProd.setText(String.valueOf(grupoProd.getCdGrupo()));
                dsGrupoProd.setText(grupoProd.getDsGrupo());

                UnidadePadrao unidPad = new UnidadePadrao();
                unidPad.setCdUnidadePadrao(produto.getCdUndPadrao());
                dao.get(unidPad);
                cdUnidPad.setText(unidPad.getCdUnidadePadrao());
                dsUnidPad.setText(unidPad.getDsUnidadePadrao());

                UnidadePadrao unidPadComp = new UnidadePadrao();
                unidPadComp.setCdUnidadePadrao(produto.getCdUndPadraoCompra());
                dao.get(unidPadComp);
                cdUnidPadComp.setText(unidPadComp.getCdUnidadePadrao());
                dsUnidPadComp.setText(unidPadComp.getDsUnidadePadrao());

                qtConversao.setText(Numero.doubleToReal(produto.getQtConversao(), 2));
                qtEstoqMin.setText(Numero.doubleToReal(produto.getQtEstoqMin(), 2));
                qtEstoqAtual.setText(Numero.doubleToReal(produto.getQtEstqAtual(), 5));
                vlCustoMedio.setText(Numero.doubleToReal(produto.getVlCustoMedio(), 3));

                inEstoque.setSelected(produto.getInEstoque());
                inConsumo.setSelected(produto.getInConsumo());
                inVenda.setSelected(produto.getInVenda());

                lblCadastro.setText(Numero.getCadastro(produto.getCdUsuario(), produto.getDtCadastro()));

                cdProduto.setEditable(false);
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                produto = null;
                cdProduto.requestFocus();
            }
        }
    }

    private void validaCodigoGrupoProd() {
        if (!cdGrupoProd.getText().equals("")) {
            try {
                GrupoProduto grupoProd = new GrupoProduto();
                grupoProd.setCdGrupo(Integer.parseInt(cdGrupoProd.getText()));
                dao.get(grupoProd);
                cdGrupoProd.setText(grupoProd.getCdGrupo().toString());
                dsGrupoProd.setText(grupoProd.getDsGrupo());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                cdGrupoProd.requestFocus();
            }
        }
    }

    private void validaCodigoUnidPad() {
        if (!cdUnidPad.getText().equals("")) {
            try {
                UnidadePadrao unid = new UnidadePadrao();
                unid.setCdUnidadePadrao(cdUnidPad.getText().toUpperCase());
                dao.get(unid);
                cdUnidPad.setText(unid.getCdUnidadePadrao());
                dsUnidPad.setText(unid.getDsUnidadePadrao());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                cdUnidPad.requestFocus();
            }
        }
    }

    private void validaCodigoUnidPadComp() {
        if (!cdUnidPadComp.getText().equals("")) {
            try {
                UnidadePadrao unid = new UnidadePadrao();
                unid.setCdUnidadePadrao(cdUnidPadComp.getText().toUpperCase());
                dao.get(unid);
                cdUnidPadComp.setText(unid.getCdUnidadePadrao());
                dsUnidPadComp.setText(unid.getDsUnidadePadrao());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                cdUnidPadComp.requestFocus();
            }
        }
    }

    private void limparTela() {
        produto = null;
        FuncaoCampo.limparCampos(painel);
        inAtivo.setSelected(true);
        cdProduto.setEditable(true);
        lblCadastro.setText("");
    }

    @FXML
    public void pesquisarGrupo() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new GrupoProduto(), "cdGrupo", "dsGrupo", null, "Lista de Grupo de Produtos");
        if (valor != null) {
            cdGrupoProd.setText(valor);
            validaCodigoGrupoProd();
        }
    }

    @FXML
    public void pesquisarUndPad() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new UnidadePadrao(), "cdUnidadePadrao", "dsUnidadePadrao", null, "Lista de Unidade Padrão de Produtos");
        if (valor != null) {
            cdUnidPad.setText(valor);
            validaCodigoUnidPad();
        }
    }

    @FXML
    public void pesquisarUndPadCompra() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new UnidadePadrao(), "cdUnidadePadrao", "dsUnidadePadrao", null, "Lista de Unidade Padrão de Produtos");
        if (valor != null) {
            cdUnidPadComp.setText(valor);
            validaCodigoUnidPadComp();
        }
    }

}
