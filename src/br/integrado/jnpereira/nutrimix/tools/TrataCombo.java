package br.integrado.jnpereira.nutrimix.tools;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import br.integrado.jnpereira.nutrimix.controle.ProdutoController;

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
            combo.setItems(FXCollections.observableArrayList(ProdutoController.getAllTipoAjuste()));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado - 1);
        }
    }

    public static Integer getValueComboTpAjustEstoq(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            return ProdutoController.getAllTipoAjuste().get(model.getSelectedIndex()).getCdTpAjuste();
        }
        return null;
    }

    public static void setValueComboTpMovtoEstoque(ChoiceBox combo, Integer selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList(ProdutoController.getAllTipoMovtoEstoque()));
        }
        if (selecionado != null) {
            SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
            model.select(selecionado - 1);
        }
    }

    public static Integer getValueComboTpMovtoEstoque(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            return ProdutoController.getAllTipoMovtoEstoque().get(model.getSelectedIndex()).getCdTpMovto();
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

}
