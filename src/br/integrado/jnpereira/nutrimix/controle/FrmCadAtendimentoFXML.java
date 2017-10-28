package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import br.integrado.jnpereira.nutrimix.modelo.Atendimento;
import br.integrado.jnpereira.nutrimix.modelo.AtendimentoProduto;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FrmCadAtendimentoFXML implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML
    TextField cdAtend;
    @FXML
    TextField dtAtend;
    @FXML
    TextField nrMesa;
    @FXML
    ChoiceBox tpSituacao;
    @FXML
    Label lblCadastro;

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdProduto;
    @FXML
    Button btnPesqProd;
    @FXML
    TextField dsProduto;
    @FXML
    TextField qtProduto;
    @FXML
    TextField qtPaga;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    @FXML
    Label lblCadProd;

    public Stage stage;
    public Object param;
    double LayoutYAtend;
    ArrayList<AtendProdHit> listAtendProd = new ArrayList<>();
    Dao dao = new Dao();
    Atendimento atendimento;
    boolean inAntiLoop = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FuncaoCampo.mascaraNumeroInteiro(cdAtend);
        FuncaoCampo.mascaraNumeroInteiro(nrMesa);
        FuncaoCampo.mascaraData(dtAtend);
        TrataCombo.setValueComboStAtendimento(tpSituacao, "1");
        tpSituacao.setDisable(true);
        cdAtend.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAtendimento();
            }
        });
        dtAtend.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoAtendimento();
            }
        });
    }

    public void iniciaTela() {
        if (param != null) {
            Atendimento atend = (Atendimento) param;
            cdAtend.setText(atend.getCdAtend().toString());
            dtAtend.setText(Data.AmericaToBrasilSemHora(atend.getDtAtend()));
            validaCodigoAtendimento();
        } else {
            atualizaAtendProd();
        }

    }

    private void validaCodigoAtendimento() {
        if (!cdAtend.getText().equals("") & !dtAtend.getText().equals("") & atendimento == null) {
            try {
                atendimento = new Atendimento();
                atendimento.setCdAtend(Integer.parseInt(cdAtend.getText()));
                atendimento.setDtAtend(Data.StringToDate(dtAtend.getText()));
                dao.get(atendimento);
                nrMesa.setText(atendimento.getNrMesa().toString());
                TrataCombo.setValueComboStAtendimento(tpSituacao, atendimento.getStAtend());
                tpSituacao.setDisable(false);
                lblCadastro.setText(Numero.getCadastro(atendimento.getCdUserCad(), atendimento.getDtCadastro()));
                cdAtend.setEditable(false);
                dtAtend.setEditable(false);
                atualizaAtendProd();
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                atendimento = null;
                dtAtend.requestFocus();
            }
        }
    }

    public void abrirListaProduto(AtendProdHit movto) {
        if (movto.atendProd != null) {
            Alerta.AlertaError("Não permitido!", "Não é possível alterar um produto já efetivado.");
            return;
        }

        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Produto(), "cdProduto", "dsProduto", "AND $inAtivo$ = 'T'", "Lista de Produtos");
        if (valor != null) {
            movto.cdProduto.setText(valor);
            validaCodigoProduto(movto);
        }
    }

    private void validaCodigoProduto(AtendProdHit atendHit) {
        if (!atendHit.cdProduto.getText().equals("")) {
            try {
                Produto prod = new Produto();
                prod.setCdProduto(Integer.parseInt(atendHit.cdProduto.getText()));
                dao.get(prod);

                if (atendHit.atendProd == null & inAntiLoop) {
                    inAntiLoop = false;
                    if (!prod.getInAtivo()) {
                        Alerta.AlertaError("Inválido", "Produto está inativo.");
                        atendHit.cdProduto.requestFocus();
                        inAntiLoop = true;
                        return;
                    }

                    if (!prod.getInVenda()) {
                        Alerta.AlertaError("Inválido", "Produto não permitido em venda.");
                        atendHit.cdProduto.requestFocus();
                        inAntiLoop = true;
                        return;
                    }

                    for (AtendProdHit movtoHit : listAtendProd) {
                        if (movtoHit.cdProduto.getText().equals(atendHit.cdProduto.getText())
                                && !movtoHit.equals(atendHit)) {
                            Alerta.AlertaError("Inválido", "Produto já está na lista");
                            atendHit.cdProduto.requestFocus();
                            inAntiLoop = true;
                            return;
                        }
                    }
                    inAntiLoop = true;
                }

                atendHit.dsProduto.setText(prod.getDsProduto());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", "Produto não encontrado!");
                atendHit.cdProduto.requestFocus();
            }
        } else {
            atendHit.dsProduto.setText("");
        }
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        atendimento = null;
        listAtendProd = new ArrayList<>();
        FuncaoCampo.limparCampos(anchor);
        lblCadastro.setText("");
        cdAtend.setEditable(true);
        dtAtend.setEditable(true);
        TrataCombo.setValueComboStAtendimento(tpSituacao, "1");
        tpSituacao.setDisable(true);
        iniciaTela();
    }

    @FXML
    public void salvar() {
        if (atendimento != null) {
            if (atendimento.getStAtend().equals("2")) {
                Alerta.AlertaError("Não autorizado", "Atendimento já encerrado, não permitido alterações.");
                return;
            }

            if (atendimento.getStAtend().equals("3")) {
                Alerta.AlertaError("Não autorizado", "Atendimento já cancelado, não permitido alterações.");
                return;
            }

            if (TrataCombo.getValueComboStAtendimento(tpSituacao).equals("3")) { //Cancelamento
                for (AtendProdHit atendHit : listAtendProd) {
                    if (atendHit.atendProd != null) {
                        if (atendHit.atendProd.getQtPaga() > 0) {
                            Alerta.AlertaError("Não autorizado", "Atendimento já contém produtos pagos, não permitido cancelamento.");
                            return;
                        }
                    }
                }
                try {
                    dao.autoCommit(false);
                    dao.get(atendimento);
                    atendimento.setStAtend("3");
                    dao.update(atendimento);
                    dao.commit();
                    Alerta.AlertaInfo("Concluído", "Atendimento Cancelado!");
                    return;
                } catch (Exception ex) {
                    Alerta.AlertaError("Erro!", ex.getMessage());
                    return;
                }
            }
        }

        if (TrataCombo.getValueComboStAtendimento(tpSituacao).equals("2")) {
            Alerta.AlertaError("Não autorizado", "Não permitido encerrar um atendimento nesta tela.");
            return;
        }

        if (nrMesa.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Nª da Mesa é obrigatório");
            nrMesa.requestFocus();
            return;
        }

        try {
            boolean vInValidaMesa = false;
            if (atendimento == null) {
                vInValidaMesa = true;
            } else {
                if (!atendimento.getNrMesa().toString().equals(nrMesa.getText())) {
                    vInValidaMesa = true;
                }
            }
            if (vInValidaMesa) {
                String where = " WHERE $nrMesa$ = " + nrMesa.getText() + " AND $stAtend$ = '1'";
                long qtMesas = dao.getCountWhere(new Atendimento(), where);
                if (qtMesas > 0) {
                    Alerta.AlertaError("Campo inválido!", "Já existe atendimento pendente para a Mesa: " + nrMesa.getText());
                    nrMesa.requestFocus();
                    return;
                }
            }

        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.getMessage());
            return;
        }

        for (AtendProdHit atendHit : listAtendProd) {
            if (atendHit.cdProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Código do produto é obrigatório");
                atendHit.cdProduto.requestFocus();
                return;
            }

            if (atendHit.qtProduto.getText().equals("")) {
                Alerta.AlertaError("Campo inválido", "Quantidade do atendimento é obrigatório.");
                atendHit.qtProduto.requestFocus();
                return;
            } else {
                double vlProduto = Double.parseDouble(atendHit.qtProduto.getText());
                if (vlProduto <= 0) {
                    Alerta.AlertaError("Campo inválido", "Quantidade do atendimento deve ser maior que 0.");
                    atendHit.qtProduto.requestFocus();
                    return;
                }

                double vlPaga = (atendHit.qtPaga.getText().equals("") ? 0.0 : Double.parseDouble(atendHit.qtPaga.getText()));
                if (vlProduto < vlPaga) {
                    Alerta.AlertaError("Campo inválido", "Quantidade do atendimento menor que quantidade paga.");
                    atendHit.qtProduto.requestFocus();
                    return;
                }
            }

        }
        try {
            dao.autoCommit(false);
            if (atendimento == null) {
                atendimento = new Atendimento();
                atendimento.setDtAtend(Data.getAgora());
                String where = "WHERE $dtAtend$ = '" + Data.BrasilToAmericaSemHora(atendimento.getDtAtend()) + "'";
                Long cdAtendimento = dao.getCountWhere(new Atendimento(), where) + 1;
                atendimento.setCdAtend(Integer.parseInt(cdAtendimento.toString()));
                atendimento.setNrMesa(Integer.parseInt(nrMesa.getText()));
                atendimento.setStAtend(TrataCombo.getValueComboStAtendimento(tpSituacao));
                atendimento.setCdUserCad(FrmMenuFXML.usuarioAtivo);
                atendimento.setDtCadastro(Data.getAgora());
                dao.save(atendimento);
            } else {
                atendimento.setNrMesa(Integer.parseInt(nrMesa.getText()));
                atendimento.setStAtend(TrataCombo.getValueComboStAtendimento(tpSituacao));
                dao.update(atendimento);
            }

            for (AtendProdHit atendHit : listAtendProd) {
                if (atendHit.atendProd == null) {
                    AtendimentoProduto atendPrd = new AtendimentoProduto();
                    atendPrd.setCdAtend(atendimento.getCdAtend());
                    atendPrd.setDtAtend(atendimento.getDtAtend());
                    atendPrd.setCdProduto(Integer.parseInt(atendHit.cdProduto.getText()));
                    atendPrd.setQtProduto(Double.parseDouble(atendHit.qtProduto.getText()));
                    atendPrd.setQtPaga(0.0);
                    atendPrd.setCdUserCad(FrmMenuFXML.usuarioAtivo);
                    atendPrd.setDtCadastro(Data.getAgora());
                    dao.save(atendPrd);
                } else {
                    if (atendHit.isExcluir) {
                        dao.delete(atendHit.atendProd);
                    } else {
                        Double vlProduto = Double.parseDouble(atendHit.qtProduto.getText());
                        if (!vlProduto.equals(atendHit.atendProd.getQtProduto())) {
                            atendHit.atendProd.setQtProduto(vlProduto);
                            dao.update(atendHit.atendProd);
                        }
                    }
                }
            }

            dao.commit();
            Integer cod = atendimento.getCdAtend();
            Date date = atendimento.getDtAtend();
            limpar();
            cdAtend.setText(cod.toString());
            dtAtend.setText(Data.AmericaToBrasilSemHora(date));
            validaCodigoAtendimento();
        } catch (Exception ex) {
            dao.rollback();
            Alerta.AlertaError("Erro!", ex.getMessage());
            return;
        }
        Alerta.AlertaInfo("Concluído", "Atendimento salvo!");
        if (param != null) {
            getStage().close();
        }
    }

    public void atualizaAtendProd() {
        try {
            ArrayList<Object> atends = new ArrayList<>();
            if (atendimento != null) {
                String where = "WHERE $cdAtend$ = " + atendimento.getCdAtend()
                        + " AND $dtAtend$ = '" + Data.BrasilToAmericaSemHora(atendimento.getDtAtend()) + "' ORDER BY $cdProduto$ ASC";
                atends = dao.getAllWhere(new AtendimentoProduto(), where);
                listAtendProd.clear();
            }
            if (atends.isEmpty()) {
                AtendProdHit atendHit = new AtendProdHit();
                listAtendProd.add(atendHit);
            }
            for (Object obj : atends) {
                AtendimentoProduto atendProd = (AtendimentoProduto) obj;
                AtendProdHit atendHit = new AtendProdHit();
                atendHit.cdProduto.setText(atendProd.getCdProduto().toString());
                Produto produto = new Produto();
                produto.setCdProduto(atendProd.getCdProduto());
                dao.get(produto);
                atendHit.dsProduto.setText(produto.getDsProduto());
                atendHit.qtProduto.setText(Numero.doubleToReal(atendProd.getQtProduto(), 2));
                atendHit.qtPaga.setText(Numero.doubleToReal(atendProd.getQtPaga(), 2));
                atendHit.lblCadProd.setText(Numero.getCadastro(atendProd.getCdUserCad(), atendProd.getDtCadastro()));
                atendHit.atendProd = atendProd;
                listAtendProd.add(atendHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaLista();
    }

    public void atualizaLista() {
        int total = 0;
        for (AtendProdHit b : listAtendProd) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYAtend = cdProduto.getLayoutY();
        painel.getChildren().clear();
        Iterator it = listAtendProd.iterator();
        for (int i = 0; it.hasNext(); i++) {
            AtendProdHit b = (AtendProdHit) it.next();
            if (!b.isExcluir) {
                b.cdProduto.setEditable(cdProduto.isEditable());
                if (b.atendProd != null) {
                    b.cdProduto.setEditable(false);
                }
                b.cdProduto.setPrefHeight(cdProduto.getHeight());
                b.cdProduto.setPrefWidth(cdProduto.getWidth());
                b.cdProduto.setLayoutX(cdProduto.getLayoutX());
                b.cdProduto.setLayoutY(LayoutYAtend);
                b.cdProduto.getStyleClass().addAll(this.cdProduto.getStyleClass());
                b.dsProduto.setEditable(dsProduto.isEditable());
                b.dsProduto.setPrefHeight(dsProduto.getHeight());
                b.dsProduto.setPrefWidth(dsProduto.getWidth());
                b.dsProduto.setLayoutX(dsProduto.getLayoutX());
                b.dsProduto.setLayoutY(LayoutYAtend);
                b.dsProduto.getStyleClass().addAll(this.dsProduto.getStyleClass());
                b.qtProduto.setEditable(qtProduto.isEditable());
                b.qtProduto.setPrefHeight(qtProduto.getHeight());
                b.qtProduto.setPrefWidth(qtProduto.getWidth());
                b.qtProduto.setLayoutX(qtProduto.getLayoutX());
                b.qtProduto.setLayoutY(LayoutYAtend);
                b.qtProduto.getStyleClass().addAll(this.qtProduto.getStyleClass());
                b.qtPaga.setEditable(qtPaga.isEditable());
                b.qtPaga.setPrefHeight(qtPaga.getHeight());
                b.qtPaga.setPrefWidth(qtPaga.getWidth());
                b.qtPaga.setLayoutX(qtPaga.getLayoutX());
                b.qtPaga.setLayoutY(LayoutYAtend);
                b.qtPaga.getStyleClass().addAll(this.qtPaga.getStyleClass());
                b.lblCadProd.setPrefHeight(lblCadProd.getHeight());
                b.lblCadProd.setPrefWidth(lblCadProd.getWidth());
                b.lblCadProd.setLayoutX(lblCadProd.getLayoutX());
                b.lblCadProd.setLayoutY(LayoutYAtend + 30);
                b.lblCadProd.getStyleClass().addAll(this.lblCadProd.getStyleClass());
                b.btnPesqProd.setPrefHeight(btnPesqProd.getHeight());
                b.btnPesqProd.setPrefWidth(btnPesqProd.getWidth());
                b.btnPesqProd.setLayoutX(btnPesqProd.getLayoutX());
                b.btnPesqProd.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnPesqProd, IconButtonHit.ICON_PESQUISA);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYAtend);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painel.getChildren().add(b.cdProduto);
                painel.getChildren().add(b.btnPesqProd);
                painel.getChildren().add(b.dsProduto);
                painel.getChildren().add(b.qtProduto);
                painel.getChildren().add(b.qtPaga);
                painel.getChildren().add(b.btnAdd);
                painel.getChildren().add(b.btnRem);
                painel.getChildren().add(b.lblCadProd);
                LayoutYAtend += (cdProduto.getHeight() + 17);
            }
            addValidacao(b, i, total);
        }
        painel.setPrefHeight(LayoutYAtend + 10);
    }

    public void addValidacao(AtendProdHit atendProdHit, int posicao, int total) {
        FuncaoCampo.mascaraNumeroInteiro(atendProdHit.cdProduto);
        FuncaoCampo.mascaraNumeroDecimal(atendProdHit.qtProduto);

        atendProdHit.cdProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoProduto(atendProdHit);
            }
        });

        atendProdHit.btnPesqProd.setOnAction((ActionEvent event) -> {
            abrirListaProduto(atendProdHit);
        });
        atendProdHit.qtProduto.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!atendProdHit.qtProduto.getText().equals("")) {
                    Double valor = Double.parseDouble(atendProdHit.qtProduto.getText());
                    atendProdHit.qtProduto.setText(Numero.doubleToReal(valor, 2));
                }
            }
        });

        atendProdHit.btnAdd.setOnAction((ActionEvent event) -> {
            AtendProdHit b = new AtendProdHit();
            listAtendProd.add(posicao + 1, b);
            atualizaLista();
        });

        atendProdHit.btnRem.setOnAction((ActionEvent event) -> {
            if (atendProdHit.atendProd != null) {
                if (atendProdHit.atendProd.getQtPaga() > 0) {
                    Alerta.AlertaError("Negado!", "Não é possivel deletar um item com quantidade paga.");
                    return;
                }
            }

            if (total == 1) {
                AtendProdHit b = new AtendProdHit();
                listAtendProd.add(b);
            }
            if (atendProdHit.atendProd == null) {
                listAtendProd.remove(atendProdHit);
            } else {
                listAtendProd.get(posicao).isExcluir = true;
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

    public class AtendProdHit {

        AtendimentoProduto atendProd;
        TextField cdProduto = new TextField();
        Button btnPesqProd = new Button();
        TextField dsProduto = new TextField();
        TextField qtProduto = new TextField();
        TextField qtPaga = new TextField();
        Button btnAdd = new Button();
        Button btnRem = new Button();
        Label lblCadProd = new Label();
        public boolean isExcluir = false;
    }

}
