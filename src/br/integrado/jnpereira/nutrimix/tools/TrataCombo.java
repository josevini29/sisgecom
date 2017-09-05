package br.integrado.jnpereira.nutrimix.tools;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;

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

    public static void setValueComboTpAjustEstoq(ChoiceBox combo, String selecionado) {
        if (combo.getItems().isEmpty()) {
            combo.setItems(FXCollections.observableArrayList(getAllTipoAjuste()));
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

    public static Integer getValueComboTpAjustEstoq(ChoiceBox combo) {
        SingleSelectionModel<ChoiceBox> model = combo.getSelectionModel();
        if (model.getSelectedIndex() > -1) {
            return getAllTipoAjuste().get(model.getSelectedIndex()).getCdTpAjuste();
        }
        return null;
    }

    public static ArrayList<TipoAjusteEstoque> getAllTipoAjuste() {
        ArrayList<TipoAjusteEstoque> tipos = new ArrayList<>();
        tipos.add(new TipoAjusteEstoque(1, "Consumo Interno", "S"));
        tipos.add(new TipoAjusteEstoque(2, "Correção de Quantidade (Entrada)", "E"));
        tipos.add(new TipoAjusteEstoque(3, "Correção de Quantidade (Saída)", "S"));
        tipos.add(new TipoAjusteEstoque(4, "Desperdício", "S"));
        tipos.add(new TipoAjusteEstoque(5, "Validade Vencida", "S"));
        tipos.add(new TipoAjusteEstoque(6, "Quebra/CAT", "S"));
        tipos.add(new TipoAjusteEstoque(7, "Roubo", "S"));
        return tipos;
    }

    public static class TipoAjusteEstoque {

        private Integer cdTpAjuste;
        private String dsTpAjuste;
        private String tpAjuste;

        public TipoAjusteEstoque(Integer cdTpAjuste, String dsTpAjuste, String tpAjuste) {
            this.cdTpAjuste = cdTpAjuste;
            this.dsTpAjuste = dsTpAjuste;
            this.tpAjuste = tpAjuste;
        }

        public Integer getCdTpAjuste() {
            return cdTpAjuste;
        }

        public void setCdTpAjuste(Integer cdTpAjuste) {
            this.cdTpAjuste = cdTpAjuste;
        }

        public String getDsTpAjuste() {
            return dsTpAjuste;
        }

        public void setDsTpAjuste(String dsTpAjuste) {
            this.dsTpAjuste = dsTpAjuste;
        }

        public String getTpAjuste() {
            return tpAjuste;
        }

        public void setTpAjuste(String tpAjuste) {
            this.tpAjuste = tpAjuste;
        }

        @Override
        public String toString() {
            return getDsTpAjuste();
        }
    }

}
