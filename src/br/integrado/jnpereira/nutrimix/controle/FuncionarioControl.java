package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Cidade;
import br.integrado.jnpereira.nutrimix.modelo.Funcionario;
import br.integrado.jnpereira.nutrimix.modelo.Endereco;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Telefone;
import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import br.integrado.jnpereira.nutrimix.tools.Numero;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import br.integrado.jnpereira.nutrimix.tools.TrataCombo;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FuncionarioControl implements Initializable {

    @FXML
    AnchorPane painel;
    @FXML
    TextField cdFuncionario;
    @FXML
    TextField dsFuncionario;
    @FXML
    TextField dsCargo;
    @FXML
    TextField dtAdmissao;
    @FXML
    TextField dtDemissao;
    @FXML
    Label lblCadastro;
    @FXML

    TextField nrCpf;
    @FXML
    TextField nrRg;
    @FXML
    TextField dtNasc;
    @FXML
    ChoiceBox tpSexo;
    @FXML
    ChoiceBox tpCivil;
    @FXML
    TextField nrPis;
    @FXML
    TextField dsEmail;

    @FXML
    TextField cdCidade;
    @FXML
    TextField dsCidade;
    @FXML
    TextField cdCep;
    @FXML
    TextField dsLogradouro;
    @FXML
    ChoiceBox tpEndereco;
    @FXML
    TextField nrImovel;
    @FXML
    TextField dsBairro;
    @FXML
    TextField dsComplemento;
    @FXML
    Label lblPosEnd;

    @FXML
    ChoiceBox tpTelefone;
    @FXML
    ChoiceBox tpUso;
    @FXML
    TextField nrDdd;
    @FXML
    TextField nrTelefone;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRem;
    @FXML
    AnchorPane painelTelefone;

    Integer nrOccEndereco;
    Dao dao = new Dao();
    Pessoa pessoa;
    Funcionario funcionario;
    ArrayList<EnderecoHit> enderecosHit;
    ArrayList<EnderecoHit> enderecosHitExcluir = new ArrayList<>();
    ArrayList<TelefonesHit> listTelefone = new ArrayList<>();
    double LayoutYTelefone;

    public void iniciaTela() {
        atualizaEnderecoHit();
        atualizaTelefoneHit();
    }

    @FXML
    public void pesquisarCidade() {
        Tela tela = new Tela();
        String valor = tela.abrirListaGenerica(new Cidade(), "cdCidade", "dsCidade", null, "Lista de Cidades");
        if (valor != null) {
            cdCidade.setText(valor);
            validaCodigoCidade();
        }
    }

    @FXML
    public void pesquisarFuncionario() {
        Tela tela = new Tela();
        String valor = tela.abrirListaPessoa(new Funcionario(), false);
        if (valor != null) {
            cdFuncionario.setText(valor);
            validaCodigoFuncionario();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FuncaoCampo.mascaraNumeroInteiro(cdFuncionario);
        FuncaoCampo.mascaraNumeroInteiro(cdCidade);
        FuncaoCampo.mascaraRG(nrRg);
        FuncaoCampo.mascaraCPF(nrCpf);
        FuncaoCampo.mascaraData(dtNasc);
        FuncaoCampo.mascaraData(dtAdmissao);
        FuncaoCampo.mascaraData(dtDemissao);
        FuncaoCampo.mascaraPIS(nrPis);
        FuncaoCampo.mascaraCEP(cdCep);
        FuncaoCampo.mascaraTexto(dsEmail, 80);
        FuncaoCampo.mascaraTexto(dsCargo, 50);
        FuncaoCampo.mascaraTexto(dsFuncionario, 200);
        FuncaoCampo.mascaraTexto(dsLogradouro, 100);
        FuncaoCampo.mascaraTexto(dsComplemento, 100);
        FuncaoCampo.mascaraTexto(nrImovel, 15);
        FuncaoCampo.mascaraTexto(dsBairro, 60);
        TrataCombo.setValueComboSexo(tpSexo, null);
        TrataCombo.setValueComboCivil(tpCivil, null);
        TrataCombo.setValueComboEndereco(tpEndereco, null);
        tpTelefone.setVisible(false);
        tpUso.setVisible(false);
        nrTelefone.setVisible(false);
        nrDdd.setVisible(false);
        btnAdd.setVisible(false);
        btnRem.setVisible(false);
        cdFuncionario.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoFuncionario();
            }
        });
        nrCpf.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCpf();
            }
        });
        cdCidade.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                validaCodigoCidade();
            }
        });
        cdCidade.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            if (novo.equals("")) {
                enderecosHit.get(nrOccEndereco).setCdCidade(null);
            } else {
                enderecosHit.get(nrOccEndereco).setCdCidade(Integer.parseInt(novo));
            }
        });
        dtNasc.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtNasc.getText().equals("")) {
                    try {
                        Date data = Data.StringToDate(dtNasc.getText());
                        if (data.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Dt. Nascimento não pode ser maior que a data atual.");
                            dtNasc.requestFocus();
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtNasc.requestFocus();
                    }
                }
            }
        });
        dtAdmissao.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtAdmissao.getText().equals("")) {
                    try {
                        Date data = Data.StringToDate(dtAdmissao.getText());
                        if (data.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Dt. de Admissão não pode ser maior que a data atual.");
                            dtAdmissao.requestFocus();
                        }
                        if (!dtDemissao.getText().equals("")) {
                            Date dataDem = Data.StringToDate(dtDemissao.getText());
                            if (dataDem.after(data)) {
                                Alerta.AlertaError("Campo inválido", "Dt. de Demissão não pode ser anterior a data de admissão.");
                                dtAdmissao.requestFocus();
                            }
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtAdmissao.requestFocus();
                    }
                }
            }
        });
        dtDemissao.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!dtDemissao.getText().equals("")) {
                    try {
                        Date data = Data.StringToDate(dtDemissao.getText());
                        if (data.after(new Date())) {
                            Alerta.AlertaError("Campo inválido", "Dt. de Demissão não pode ser maior que a data atual.");
                            dtDemissao.requestFocus();
                        }
                        if (!dtAdmissao.getText().equals("")) {
                            Date dataAdm = Data.StringToDate(dtAdmissao.getText());
                            if (dataAdm.after(data)) {
                                Alerta.AlertaError("Campo inválido", "Dt. de Admissão não pode ser posterior a data de Demissão.");
                                dtDemissao.requestFocus();
                            }
                        }
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        dtDemissao.requestFocus();
                    }
                }
            }
        });
        cdCep.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            enderecosHit.get(nrOccEndereco).setCdCep(Numero.RemoveMascara(novo));
        });
        dsLogradouro.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            enderecosHit.get(nrOccEndereco).setDsLogradouro(novo);
        });
        nrImovel.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            enderecosHit.get(nrOccEndereco).setNrImovel(novo);
        });
        dsBairro.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            enderecosHit.get(nrOccEndereco).setDsBairro(novo);
        });
        dsComplemento.textProperty().addListener((ObservableValue<? extends String> obs, String velho, String novo) -> {
            enderecosHit.get(nrOccEndereco).setDsComplemeto(novo);
        });
        tpEndereco.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            if (TrataCombo.getValueComboEndereco(tpEndereco) != null) {
                enderecosHit.get(nrOccEndereco).setTpEndereco(Integer.parseInt(TrataCombo.getValueComboEndereco(tpEndereco)));
            } else {
                enderecosHit.get(nrOccEndereco).setTpEndereco(null);
            }
        });

    }

    private void validaCodigoFuncionario() {
        if (!cdFuncionario.getText().equals("") & funcionario == null) {
            try {
                funcionario = new Funcionario();
                funcionario.setCdFuncionario(Integer.parseInt(cdFuncionario.getText()));
                dao.get(funcionario);
                dsCargo.setText(funcionario.getDsCargo());
                dtAdmissao.setText(Data.AmericaToBrasilSemHora(funcionario.getDtAdmissao()));
                dtDemissao.setText(Data.AmericaToBrasilSemHora(funcionario.getDtDemissao()));
                nrPis.setText(Numero.NumeroToPIS(funcionario.getNrPis()));
                lblCadastro.setText(Numero.getCadastro(funcionario.getCdUserCad(), funcionario.getDtCadastro()));

                pessoa = new Pessoa();
                pessoa.setCdPessoa(funcionario.getCdPessoa());
                dao.get(pessoa);
                dsFuncionario.setText(pessoa.getDsPessoa());
                dsEmail.setText(pessoa.getDsEmail());

                nrCpf.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                nrRg.setText(Numero.NumeroToRG(pessoa.getNrRg()));
                dtNasc.setText(Data.AmericaToBrasilSemHora(pessoa.getDtNasc()));
                TrataCombo.setValueComboSexo(tpSexo, pessoa.getTpSexo());
                TrataCombo.setValueComboCivil(tpCivil, pessoa.getTpCivil());

                nrCpf.setEditable(false);
                cdFuncionario.setEditable(false);
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                funcionario = null;
                cdFuncionario.requestFocus();
                return;
            }
            atualizaEnderecoHit();
            atualizaTelefoneHit();
        }
    }

    public void validaCodigoCpf() {
        if (!nrCpf.getText().equals("")) {
            String cpf = Numero.RemoveMascara(nrCpf.getText());
            if (pessoa != null) {
                if (pessoa.getNrCpfCnpj().equals(cpf)) {
                    return;
                }
            }
            if (!Numero.isCPF(cpf)) {
                Alerta.AlertaError("CPF inválido", "CPF não é um numero válido.");
                nrCpf.requestFocus();
                return;
            }
            try {
                ArrayList<Object> pessoaProc = dao.getAllWhere(new Pessoa(), " WHERE $nrCpfCnpj$ = '" + cpf + "' AND $tpPessoa$ = 'F'");
                pessoa = null;
                enderecosHit = null;
                enderecosHitExcluir = new ArrayList<>();
                listTelefone = new ArrayList<>();
                for (Object obj : pessoaProc) {
                    Pessoa pessoaObj = (Pessoa) obj;
                    String where = funcionario != null ? " WHERE $cdPessoa$ = " + pessoaObj.getCdPessoa() + " AND $cdFuncionario$ != " + funcionario.getCdFuncionario()
                            : " WHERE $cdPessoa$ = " + pessoaObj.getCdPessoa();
                    ArrayList<Object> funcPes = dao.getAllWhere(new Funcionario(), where);
                    for (Object objFun : funcPes) {
                        Funcionario fun = (Funcionario) objFun;
                        if (fun.getDtDemissao() == null) {
                            Alerta.AlertaError("CPF inválido", "Já existe Funcionario ativo com este CPF.");
                            nrCpf.requestFocus();
                            return;
                        }
                    }
                    pessoa = pessoaObj;
                    nrCpf.setText(Numero.NumeroToCPF(pessoa.getNrCpfCnpj()));
                    nrRg.setText(Numero.NumeroToRG(pessoa.getNrRg()));
                    dtNasc.setText(Data.AmericaToBrasilSemHora(pessoa.getDtNasc()));
                    dsFuncionario.setText(pessoa.getDsPessoa());
                    dsEmail.setText(pessoa.getDsEmail());
                    TrataCombo.setValueComboSexo(tpSexo, pessoa.getTpSexo());
                    TrataCombo.setValueComboCivil(tpCivil, pessoa.getTpCivil());
                }
                atualizaEnderecoHit();
                atualizaTelefoneHit();
            } catch (Exception ex) {
            }
        } else {
            pessoa = null;
        }
    }

    @FXML
    public void salvar() {

        if (dsFuncionario.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo nome do funcionário é obrigatório.");
            return;
        }

        if (nrCpf.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo CPF do funcionário é obrigatório.");
            return;
        }

        if (nrRg.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo RG do funcionário é obrigatório.");
            return;
        }

        if (dtNasc.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo Dt. Nascimento do funcionário é obrigatório.");
            return;
        }

        if (TrataCombo.getValueComboSexo(tpSexo) == null) {
            Alerta.AlertaError("Campo inválido", "Campo Sexo do funcionário é obrigatório.");
            return;
        }

        if (TrataCombo.getValueComboSexo(tpCivil) == null) {
            Alerta.AlertaError("Campo inválido", "Campo Estado Civil do funcionário é obrigatório.");
            return;
        }

        if (nrPis.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Campo Nª PIS do funcionário é obrigatório.");
            return;
        }

        if (dtAdmissao.getText().equals("")) {
            Alerta.AlertaError("Campo inválido", "Data de admissão do funcionário é obrigatória.");
            return;
        }

        if (funcionario != null) {
            if (funcionario.getDtDemissao() != null) {
                Alerta.AlertaError("Campo inválido", "Funcionário com demissão não pode ser alterado, realize outro cadastro de funcionário.");
                return;
            }
        }

        /*for (EnderecoHit endAlt : enderecosHit) {
            if (endAlt.getCdCidade() == null) {
                Alerta.AlertaError("Campo inválido!", "Campo cidade é obrigatório.");
                return;
            }
            if (endAlt.getCdCidade().equals("")) {
                Alerta.AlertaError("Campo inválido!", "Campo cidade é obrigatório.");
                return;
            }
        }*/

        for (EnderecoHit endAlt : enderecosHit) {
            if (endAlt.getCdCidade() != null) {
                if (!endAlt.getCdCidade().equals("")) {
                    if (trataNulo(endAlt.getCdCep()).equals("")) {
                        Alerta.AlertaError("Campo inválido!", "Campo CEP é obrigatório.");
                        return;
                    }
                    if (trataNulo(endAlt.getDsLogradouro()).equals("")) {
                        Alerta.AlertaError("Campo inválido!", "Campo Logradouro é obrigatório.");
                        return;
                    }
                    if (trataNulo(TrataCombo.getValueComboEndereco(tpEndereco)).equals("")) {
                        Alerta.AlertaError("Campo inválido!", "Campo CEP é obrigatório.");
                        return;
                    }
                    if (trataNulo(endAlt.getNrImovel()).equals("")) {
                        Alerta.AlertaError("Campo inválido!", "Campo CEP é obrigatório.");
                        return;
                    }
                    if (trataNulo(endAlt.getDsBairro()).equals("")) {
                        Alerta.AlertaError("Campo inválido!", "Campo CEP é obrigatório.");
                        return;
                    }
                }
            }
        }

        for (TelefonesHit tel : listTelefone) {
            if (TrataCombo.getValueComboTipoTel(tel.tpTelefone) != null
                    || TrataCombo.getValueComboTipoUsoTel(tel.tpUso) != null
                    || !tel.nrDdd.getText().equals("") || !tel.nrTelefone.getText().equals("")) {
                if (TrataCombo.getValueComboTipoTel(tel.tpTelefone) == null) {
                    Alerta.AlertaError("Campo inválido!", "Tipo telefone é obrigatório.");
                    return;
                }
                if (TrataCombo.getValueComboTipoUsoTel(tel.tpUso) == null) {
                    Alerta.AlertaError("Campo inválido!", "Tipo uso de telefone é obrigatório.");
                    return;
                }
                if (tel.nrDdd.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido!", "Campo DDD é obrigatório.");
                    return;
                }
                if (tel.nrTelefone.getText().equals("")) {
                    Alerta.AlertaError("Campo inválido!", "Telefone é obrigatório.");
                    return;
                }
            }
        }

        dao.autoCommit(false);

        if (pessoa == null) {
            pessoa = new Pessoa();
        }

        pessoa.setTpPessoa("F");
        pessoa.setNrCpfCnpj(Numero.RemoveMascara(nrCpf.getText()));
        pessoa.setNrRg(Numero.RemoveMascara(nrRg.getText()));
        try {
            pessoa.setDtNasc(Data.StringToDate(dtNasc.getText()));
        } catch (Exception ex) {
            Alerta.AlertaError("Erro", ex.getMessage());
            limpar();
            return;
        }
        pessoa.setTpSexo(TrataCombo.getValueComboSexo(tpSexo));
        pessoa.setTpCivil(TrataCombo.getValueComboCivil(tpCivil));
        pessoa.setDsPessoa(dsFuncionario.getText());
        pessoa.setDsEmail(dsEmail.getText());

        try {
            if (pessoa.getCdPessoa() == null) {
                dao.save(pessoa);
            } else {
                dao.update(pessoa);
            }

            //Excluir endereços marcados para deleção
            for (EnderecoHit endExcluir : enderecosHitExcluir) {
                if (endExcluir.getCdEndereco() != null && endExcluir.getCdPessoa() != null) {
                    Endereco end = new Endereco();
                    end.setCdEndereco(endExcluir.getCdEndereco());
                    end.setCdPessoa(endExcluir.getCdPessoa());
                    dao.delete(end);
                } else {
                    Alerta.AlertaError("Erro!", "Não encontrado chave para exclusão do endereço.");
                    return;
                }
            }

            //Adiciona ou altera os endereços
            for (EnderecoHit endAlt : enderecosHit) {
                if (endAlt.getCdEndereco() != null && endAlt.getCdPessoa() != null) {
                    Endereco end = new Endereco();
                    end.setCdEndereco(endAlt.getCdEndereco());
                    end.setCdPessoa(endAlt.getCdPessoa());
                    end.setCdCidade(endAlt.getCdCidade());
                    end.setTpEndereco(Integer.parseInt(TrataCombo.getValueComboEndereco(tpEndereco)));
                    end.setDsLogradouro(endAlt.getDsLogradouro());
                    end.setDsComplemeto(endAlt.getDsComplemeto());
                    end.setNrImovel(endAlt.getNrImovel());
                    end.setCdCep(Numero.RemoveMascara(endAlt.getCdCep()));
                    end.setDsBairro(endAlt.getDsBairro());
                    dao.update(end);
                } else {
                    Endereco end = new Endereco();
                    end.setCdPessoa(pessoa.getCdPessoa());
                    end.setCdCidade(endAlt.getCdCidade());
                    end.setTpEndereco(Integer.parseInt(TrataCombo.getValueComboEndereco(tpEndereco)));
                    end.setDsLogradouro(endAlt.getDsLogradouro());
                    end.setDsComplemeto(endAlt.getDsComplemeto());
                    end.setNrImovel(endAlt.getNrImovel());
                    end.setCdCep(Numero.RemoveMascara(endAlt.getCdCep()));
                    end.setDsBairro(endAlt.getDsBairro());
                    dao.save(end);
                }
            }

            for (TelefonesHit tel : listTelefone) {
                if (!tel.nrTelefone.getText().equals("")) {
                    if (tel.isExcluir) {
                        if (tel.cdPessoa != null && tel.cdTelefone != null) {
                            Telefone telDelete = new Telefone();
                            telDelete.setCdPessoa(tel.cdPessoa);
                            telDelete.setCdTelefone(tel.cdTelefone);
                            dao.delete(telDelete);
                        }
                    } else {
                        if (tel.cdPessoa != null && tel.cdTelefone != null) {
                            Telefone telUpdate = new Telefone();
                            telUpdate.setCdPessoa(tel.cdPessoa);
                            telUpdate.setCdTelefone(tel.cdTelefone);
                            telUpdate.setTpTelefone(TrataCombo.getValueComboTipoTel(tel.tpTelefone));
                            telUpdate.setTpUso(TrataCombo.getValueComboTipoUsoTel(tel.tpUso));
                            telUpdate.setNrDdd(tel.nrDdd.getText());
                            telUpdate.setNrTelefone(Numero.RemoveMascara(tel.nrTelefone.getText()));
                            dao.update(telUpdate);
                        } else {
                            Telefone telSave = new Telefone();
                            telSave.setCdPessoa(pessoa.getCdPessoa());
                            telSave.setTpTelefone(TrataCombo.getValueComboTipoTel(tel.tpTelefone));
                            telSave.setTpUso(TrataCombo.getValueComboTipoUsoTel(tel.tpUso));
                            telSave.setNrDdd(tel.nrDdd.getText());
                            telSave.setNrTelefone(Numero.RemoveMascara(tel.nrTelefone.getText()));
                            dao.save(telSave);
                        }
                    }
                }
            }

            if (funcionario == null) {
                funcionario = new Funcionario();
                funcionario.setCdPessoa(pessoa.getCdPessoa());
                funcionario.setDtCadastro(Data.getAgora());
                funcionario.setNrPis(Numero.RemoveMascara(nrPis.getText()));
                funcionario.setCdUserCad(MenuControl.usuarioAtivo);
                funcionario.setDsCargo(dsCargo.getText());
                try {
                    funcionario.setDtAdmissao(Data.StringToDate(dtAdmissao.getText()));
                    if (!dtDemissao.getText().equals("")) {
                        funcionario.setDtDemissao(Data.StringToDate(dtDemissao.getText()));
                    }
                } catch (Exception ex) {
                    Alerta.AlertaError("Erro", ex.getMessage());
                    limpar();
                    return;
                }
                dao.save(funcionario);
            } else {
                funcionario.setCdPessoa(pessoa.getCdPessoa());
                funcionario.setNrPis(Numero.RemoveMascara(nrPis.getText()));
                funcionario.setDsCargo(dsCargo.getText());
                try {
                    funcionario.setDtAdmissao(Data.StringToDate(dtAdmissao.getText()));
                    if (!dtDemissao.getText().equals("")) {
                        funcionario.setDtDemissao(Data.StringToDate(dtDemissao.getText()));
                    }
                } catch (Exception ex) {
                    Alerta.AlertaError("Erro", ex.getMessage());
                    limpar();
                    return;
                }
                dao.update(funcionario);
            }
            dao.commit();
            Integer cod = funcionario.getCdFuncionario();
            limpar();
            cdFuncionario.setText(cod.toString());
            validaCodigoFuncionario();
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.getMessage());
            dao.rollback();
            return;
        }
        Alerta.AlertaInfo("Concluído", "Funcionario salvo com sucesso!");
    }

    @FXML
    public void limpar() {
        limparTela();
    }

    private void limparTela() {
        funcionario = null;
        pessoa = null;
        enderecosHitExcluir = new ArrayList<>();
        FuncaoCampo.limparCampos(painel);
        cdFuncionario.setEditable(true);
        nrCpf.setEditable(true);
        atualizaEnderecoHit();
        atualizaTelefoneHit();
        lblCadastro.setText("");
    }

    //Códigos referentes a atualização de endereço
    private void atualizaEnderecoHit() {
        enderecosHit = new ArrayList<>();
        if (pessoa != null) {
            try {
                ArrayList<Object> enderecos = dao.getAllWhere(new Endereco(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa() + " ORDER BY $cdEndereco$ ASC");
                for (Object obj : enderecos) {
                    Endereco end = (Endereco) obj;
                    EnderecoHit endHit = new EnderecoHit();
                    endHit.setCdEndereco(end.getCdEndereco());
                    endHit.setCdPessoa(end.getCdPessoa());
                    endHit.setCdCidade(end.getCdCidade());
                    endHit.setTpEndereco(end.getTpEndereco());
                    endHit.setDsLogradouro(end.getDsLogradouro());
                    endHit.setDsComplemeto(end.getDsComplemeto());
                    endHit.setNrImovel(end.getNrImovel());
                    endHit.setDsBairro(end.getDsBairro());
                    endHit.setCdCep(end.getCdCep());
                    enderecosHit.add(endHit);
                }
            } catch (Exception ex) {
                nrOccEndereco = null;
                enderecosHit = null;
                Alerta.AlertaError("Erro!", ex.getMessage());
                return;
            }
            if (!enderecosHit.isEmpty()) {
                nrOccEndereco = -1;
                nextEndereco();
            } else {
                adicionaEndereco();
            }
        } else {
            adicionaEndereco();
        }
    }

    private void validaCodigoCidade() {
        if (!cdCidade.getText().equals("")) {
            try {
                Cidade cidade = new Cidade();
                cidade.setCdCidade(Integer.parseInt(cdCidade.getText()));
                dao.get(cidade);
                dsCidade.setText(cidade.getDsCidade());
            } catch (Exception ex) {
                Alerta.AlertaError("Notificação", ex.getMessage());
                cdCidade.requestFocus();
            }
        } else {
            dsCidade.setText("");
        }
    }

    @FXML
    private void nextEndereco() {
        if (nrOccEndereco != null) {
            if (nrOccEndereco + 1 < enderecosHit.size()) {
                nrOccEndereco += 1;
                cdCidade.setText(trataNulo(enderecosHit.get(nrOccEndereco).getCdCidade()));
                validaCodigoCidade();
                if (enderecosHit.get(nrOccEndereco).getCdCep() == null) {
                    cdCep.setText("");
                } else {
                    cdCep.setText(Numero.NumeroToCEP(enderecosHit.get(nrOccEndereco).getCdCep()));
                }
                dsLogradouro.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsLogradouro()));
                if (enderecosHit.get(nrOccEndereco).getTpEndereco() == null) {
                    TrataCombo.setValueComboEndereco(tpEndereco, null);
                } else {
                    TrataCombo.setValueComboEndereco(tpEndereco, enderecosHit.get(nrOccEndereco).getTpEndereco().toString());
                }
                nrImovel.setText(trataNulo(enderecosHit.get(nrOccEndereco).getNrImovel()));
                dsBairro.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsBairro()));
                dsComplemento.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsComplemeto()));
                lblPosEnd.setText(nrOccEndereco + 1 + " / " + enderecosHit.size());
            }
        }
    }

    private String trataNulo(String valor) {
        if (valor == null) {
            return "";
        }
        return valor;
    }

    private String trataNulo(Integer valor) {
        if (valor == null) {
            return "";
        }
        return valor.toString();
    }

    @FXML
    private void previousEndereco() {
        if (nrOccEndereco != null) {
            if (nrOccEndereco + 1 > 1) {
                nrOccEndereco -= 1;
                cdCidade.setText(trataNulo(enderecosHit.get(nrOccEndereco).getCdCidade()));
                validaCodigoCidade();
                if (enderecosHit.get(nrOccEndereco).getCdCep() == null) {
                    cdCep.setText("");
                } else {
                    if (!enderecosHit.get(nrOccEndereco).getCdCep().equals("")) {
                        cdCep.setText(Numero.NumeroToCEP(enderecosHit.get(nrOccEndereco).getCdCep()));
                    } else {
                        cdCep.setText("");
                    }
                }
                dsLogradouro.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsLogradouro()));
                if (enderecosHit.get(nrOccEndereco).getTpEndereco() == null) {
                    TrataCombo.setValueComboEndereco(tpEndereco, null);
                } else {
                    TrataCombo.setValueComboEndereco(tpEndereco, enderecosHit.get(nrOccEndereco).getTpEndereco().toString());
                }
                nrImovel.setText(trataNulo(enderecosHit.get(nrOccEndereco).getNrImovel()));
                dsBairro.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsBairro()));
                dsComplemento.setText(trataNulo(enderecosHit.get(nrOccEndereco).getDsComplemeto()));
                lblPosEnd.setText(nrOccEndereco + 1 + " / " + enderecosHit.size());
            }
        }
    }

    @FXML
    private void adicionaEndereco() {
        EnderecoHit endHit = new EnderecoHit();
        endHit.inNovo = true;
        enderecosHit.add(endHit);
        nrOccEndereco = enderecosHit.size() - 2;
        nextEndereco();
    }

    @FXML
    private void removeEndereco() {
        if (!enderecosHit.get(nrOccEndereco).inNovo) {
            enderecosHitExcluir.add(enderecosHit.get(nrOccEndereco));
        }
        enderecosHit.remove(nrOccEndereco.intValue());
        if (enderecosHit.isEmpty()) {
            adicionaEndereco();
        }
        if (nrOccEndereco == 0) {
            nrOccEndereco += 1;
        }
        previousEndereco();
    }

    public class EnderecoHit extends Endereco {

        public boolean inNovo;
    }
    //Fim Codigo de endereco

    //Inicio codigo da lista de telefornes
    public void atualizaTelefoneHit() {
        try {
            ArrayList<Object> telefones = new ArrayList<>();
            if (pessoa != null) {
                telefones = dao.getAllWhere(new Telefone(), "WHERE $cdPessoa$ = " + pessoa.getCdPessoa() + " ORDER BY $cdTelefone$ ASC");
            }
            listTelefone.clear();
            if (telefones.isEmpty()) {
                listTelefone.add(getTelefoneNovo());
            }
            for (Object obj : telefones) {
                Telefone telefone = (Telefone) obj;
                ChoiceBox tpTelefone = new ChoiceBox();
                ChoiceBox tpUso = new ChoiceBox();
                TextField nrTelefone = new TextField();
                TextField nrDdd = new TextField();
                TrataCombo.setValueComboTipoTel(tpTelefone, telefone.getTpTelefone());
                TrataCombo.setValueComboTipoUsoTel(tpUso, telefone.getTpUso());
                nrTelefone.setText(Numero.NumeroToTelefone(telefone.getNrTelefone()));
                nrDdd.setText(telefone.getNrDdd());
                TelefonesHit telefoneHit = new TelefonesHit();
                telefoneHit.cdPessoa = telefone.getCdPessoa();
                telefoneHit.cdTelefone = telefone.getCdTelefone();
                telefoneHit.tpTelefone = tpTelefone;
                telefoneHit.tpUso = tpUso;
                telefoneHit.nrTelefone = nrTelefone;
                telefoneHit.nrDdd = nrDdd;
                listTelefone.add(telefoneHit);
            }
        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", "Erro ao iniciar tela.\n" + ex.toString());
        }
        atualizaListaTelefone();
    }

    public void atualizaListaTelefone() {
        int total = 0;
        for (TelefonesHit b : listTelefone) {
            if (!b.isExcluir) {
                total++;
            }
        }

        LayoutYTelefone = this.nrTelefone.getLayoutY();
        painelTelefone.getChildren().clear();
        Iterator it = listTelefone.iterator();
        for (int i = 0; it.hasNext(); i++) {
            TelefonesHit b = (TelefonesHit) it.next();
            if (!b.isExcluir) {
                b.tpTelefone.setPrefHeight(this.tpTelefone.getHeight());
                b.tpTelefone.setPrefWidth(this.tpTelefone.getWidth());
                b.tpTelefone.setLayoutX(this.tpTelefone.getLayoutX());
                b.tpTelefone.setLayoutY(LayoutYTelefone);
                b.tpUso.setPrefHeight(this.tpUso.getHeight());
                b.tpUso.setPrefWidth(this.tpUso.getWidth());
                b.tpUso.setLayoutX(this.tpUso.getLayoutX());
                b.tpUso.setLayoutY(LayoutYTelefone);
                b.nrTelefone.setPrefHeight(this.nrTelefone.getHeight());
                b.nrTelefone.setPrefWidth(this.nrTelefone.getWidth());
                b.nrTelefone.setLayoutX(this.nrTelefone.getLayoutX());
                b.nrTelefone.setLayoutY(LayoutYTelefone);
                b.nrDdd.setPrefHeight(this.nrDdd.getHeight());
                b.nrDdd.setPrefWidth(this.nrDdd.getWidth());
                b.nrDdd.setLayoutX(this.nrDdd.getLayoutX());
                b.nrDdd.setLayoutY(LayoutYTelefone);
                b.btnAdd.setPrefHeight(btnAdd.getHeight());
                b.btnAdd.setPrefWidth(btnAdd.getWidth());
                b.btnAdd.setLayoutX(btnAdd.getLayoutX());
                b.btnAdd.setLayoutY(LayoutYTelefone);
                IconButtonHit.setIcon(b.btnAdd, IconButtonHit.ICON_ADD);
                b.btnRem.setPrefHeight(btnRem.getHeight());
                b.btnRem.setPrefWidth(btnRem.getWidth());
                b.btnRem.setLayoutX(btnRem.getLayoutX());
                b.btnRem.setLayoutY(LayoutYTelefone);
                IconButtonHit.setIcon(b.btnRem, IconButtonHit.ICON_EXCLUIR);
                painelTelefone.getChildren().add(b.tpTelefone);
                painelTelefone.getChildren().add(b.tpUso);
                painelTelefone.getChildren().add(b.nrDdd);
                painelTelefone.getChildren().add(b.nrTelefone);
                painelTelefone.getChildren().add(b.btnAdd);
                painelTelefone.getChildren().add(b.btnRem);
                LayoutYTelefone += (this.nrTelefone.getHeight() + 5);
            }
            addValidacaoTelefone(b, i, total);
        }

        painelTelefone.setPrefHeight(LayoutYTelefone
                + 10);
    }

    public void addValidacaoTelefone(TelefonesHit banco, int posicao, int total) {
        FuncaoCampo.mascaraTelefone(banco.nrTelefone);
        FuncaoCampo.mascaraTexto(banco.nrDdd, 3);

        banco.btnAdd.setOnAction((ActionEvent event) -> {

            listTelefone.add(posicao + 1, getTelefoneNovo());
            atualizaListaTelefone();
        });

        banco.btnRem.setOnAction((ActionEvent event) -> {
            if (total == 1) {
                listTelefone.add(getTelefoneNovo());
            }
            listTelefone.get(posicao).isExcluir = true;
            atualizaListaTelefone();
        });

    }

    private TelefonesHit getTelefoneNovo() {
        ChoiceBox tpTelefone = new ChoiceBox();
        ChoiceBox tpUso = new ChoiceBox();
        TextField nrTelefone = new TextField();
        TextField nrDdd = new TextField();
        TrataCombo.setValueComboTipoTel(tpTelefone, null);
        TrataCombo.setValueComboTipoUsoTel(tpUso, null);
        nrTelefone.setText("");
        nrDdd.setText("");
        TelefonesHit telefoneHit = new TelefonesHit();
        telefoneHit.tpTelefone = tpTelefone;
        telefoneHit.tpUso = tpUso;
        telefoneHit.nrTelefone = nrTelefone;
        telefoneHit.nrDdd = nrDdd;
        return telefoneHit;
    }

    public class TelefonesHit {

        public Integer cdTelefone;
        public Integer cdPessoa;
        public ChoiceBox tpTelefone;
        public ChoiceBox tpUso;
        public TextField nrDdd;
        public TextField nrTelefone;
        public Button btnAdd;
        public Button btnRem;
        public boolean isExcluir = false;

        public TelefonesHit() {
            this.btnAdd = new Button();
            this.btnRem = new Button();
        }
    }

}
