package br.integrado.jnpereira.nutrimix.tools;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import br.integrado.jnpereira.nutrimix.controle.EstoqueControl;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.CondicaoPagto;
import br.integrado.jnpereira.nutrimix.modelo.FormaPagto;
import java.util.List;

public class TrataCombo {

    public static void setValueComboSexo(ChoiceBox combo, String selecionado) {
        combo.getItems().clear();
        combo.setItems(FXCollections.observableArrayList("Feminino", "Masculino"));
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            if (selecionado.equals("F")) {
                model.select(0);
            } else {
                model.select(1);
            }
        }
    }

    public static String getValueComboSexo(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();

        if (model.getSelectedIndex() < 0) {
            return null;
        }

        if (model.getSelectedIndex() == 0) {
            return "F";
        } else {
            return "M";
        }
    }

    public static void setValueComboCivil(ChoiceBox combo, String selecionado) {
        combo.getItems().clear();
        combo.setItems(FXCollections.observableArrayList("Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)"));
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "1":
                    model.select(0);
                    break;
                case "2":
                    model.select(1);
                    break;
                case "3":
                    model.select(2);
                    break;
                case "4":
                    model.select(3);
                    break;
                case "5":
                    model.select(4);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboCivil(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();

        switch (model.getSelectedIndex()) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "4";
            case 4:
                return "5";
            default:
                return null;
        }
    }

    public static void setValueComboEndereco(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList("Comercial", "Residêncial"));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "1":
                    model.select(0);
                    break;
                case "2":
                    model.select(1);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboEndereco(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        switch (model.getSelectedIndex()) {
            case 0:
                return "1";
            case 1:
                return "2";
            default:
                return null;
        }
    }

    public static void setValueComboTipoUsoTel(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList("Pessoal", "Comercial"));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "1":
                    model.select(0);
                    break;
                case "2":
                    model.select(1);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboTipoUsoTel(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        switch (model.getSelectedIndex()) {
            case 0:
                return "1";
            case 1:
                return "2";
            default:
                return null;
        }
    }

    public static void setValueComboTipoTel(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList("Móvel", "Fixo"));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "1":
                    model.select(0);
                    break;
                case "2":
                    model.select(1);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboTipoTel(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        switch (model.getSelectedIndex()) {
            case 0:
                return "1";
            case 1:
                return "2";
            default:
                return null;
        }
    }

    public static void setValueComboTpAjustEstoq(ChoiceBox combo, Integer selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList(EstoqueControl.getAllTipoAjuste()));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado - 1);
        }
    }

    public static Integer getValueComboTpAjustEstoq(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            return EstoqueControl.getAllTipoAjuste().get(model.getSelectedIndex()).getCdTpAjuste();
        }
        return null;
    }

    public static void setValueComboTpMovtoEstoque(ChoiceBox combo, Integer selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList(EstoqueControl.getAllTipoMovtoEstoque()));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado - 1);
        }
    }

    public static Integer getValueComboTpMovtoEstoque(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            return EstoqueControl.getAllTipoMovtoEstoque().get(model.getSelectedIndex()).getCdTpMovto();
        }
        return null;
    }

    public static void setValueComboEntradaSaida(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "E":
                    model.select(0);
                    break;
                case "S":
                    model.select(1);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboEntradaSaida(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        switch (model.getSelectedIndex()) {
            case 0:
                return "E";
            case 1:
                return "S";
            default:
                return null;
        }
    }

    public static void setValueComboStAtendimento(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList("Pendente", "Finalizado", "Cancelado"));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            switch (selecionado.replace(" ", "")) {
                case "1":
                    model.select(0);
                    break;
                case "2":
                    model.select(1);
                    break;
                case "3":
                    model.select(2);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getValueComboStAtendimento(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        switch (model.getSelectedIndex()) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
            default:
                return null;
        }
    }

    public static void setValueComboTpCondicaoPagto(ChoiceBox combo, Integer selecionado) {
        if (combo.getItems().isEmpty()) {
            try {
                Dao dao = new Dao();
                ArrayList<Object> objs = dao.getAllWhere(new CondicaoPagto(), "WHERE $inAtivo$ = 'T'");
                ArrayList<CondicaoHit> hits = new ArrayList<>();
                int i = 0;
                for (Object obj : objs) {
                    CondicaoHit hit = new CondicaoHit();
                    hit.condicao = (CondicaoPagto) obj;
                    hits.add(hit);
                    if (selecionado == hit.condicao.getCdCondicao()) {
                        selecionado = i;
                    }
                    i++;
                }
                combo.setItems(FXCollections.observableArrayList(hits));
            } catch (Exception ex) {
                ex.printStackTrace();
                Alerta.AlertaError("Atenção!", "Nenhuma condição de pagamento cadastrada!");
                return;
            }
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado);
        }
    }

    public static Integer getValueComboTpCondicaoPagto(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            List lista = combo.getItems();
            CondicaoHit cond = (CondicaoHit) lista.get(model.getSelectedIndex());
            return cond.condicao.getCdCondicao();
        }
        return null;
    }

    public static class CondicaoHit {

        CondicaoPagto condicao;

        @Override
        public String toString() {
            return condicao.getCdCondicao() + ": " + condicao.getDsCondicao();
        }
    }

    public static void setValueComboTpFormaPagto(ChoiceBox combo, Integer selecionado) {
        if (combo.getItems().isEmpty()) {
            try {
                Dao dao = new Dao();
                ArrayList<Object> objs = dao.getAllWhere(new FormaPagto(), "WHERE $inAtivo$ = 'T'");
                ArrayList<FormaHit> hits = new ArrayList<>();
                int i = 0;
                for (Object obj : objs) {
                    FormaHit hit = new FormaHit();
                    hit.forma = (FormaPagto) obj;
                    hits.add(hit);
                    if (selecionado == hit.forma.getCdFormaPagto()) {
                        selecionado = i;
                    }
                    i++;
                }
                combo.setItems(FXCollections.observableArrayList(hits));
            } catch (Exception ex) {
                ex.printStackTrace();
                Alerta.AlertaError("Atenção!", "Nenhuma forma de pagamento cadastrada!");
                return;
            }
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado);
        }
    }

    public static Integer getValueComboTpFormaPagto(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            List lista = combo.getItems();
            FormaHit forma = (FormaHit) lista.get(model.getSelectedIndex());
            return forma.forma.getCdFormaPagto();
        }
        return null;
    }

    public static class FormaHit {

        FormaPagto forma;

        @Override
        public String toString() {
            return forma.getCdFormaPagto() + ": " + forma.getDsFormaPagto();
        }
    }

    public static String getTpConta(int tpConta) {
        switch (tpConta) {
            case 1:
                return "Compra";
            case 2:
                return "Venda";
            case 3:
                return "Despesa";
        }
        return null;
    }

    public static String getTpSitConta(int tpConta) {
        switch (tpConta) {
            case 1:
                return "Pendente";
            case 2:
                return "Paga";
            case 3:
                return "Cancelada";
        }
        return null;
    }

    public static String getTpEntradaSaida(String tpMovto) {
        switch (tpMovto) {
            case "E":
                return "Entrada";
            case "S":
                return "Saída";
        }
        return null;
    }

    public static void setValueComboSitConta(ChoiceBox combo, Integer selecionado) {
        combo.getItems().clear();
        combo.setItems(FXCollections.observableArrayList("", getTpSitConta(1), getTpSitConta(2), getTpSitConta(3)));
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            if (selecionado != null) {
                model.select(selecionado);
            }
        }
    }

    public static Integer getValueComboSitConta(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();

        switch (model.getSelectedIndex()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return null;
        }
    }
}
