/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.tools;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

/**
 *
 * @author Jose Vinicius
 */
public class Alerta {

    public static void AlertaError(String titulo, String texto) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static void AlertaInfo(String titulo, String texto) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static boolean AlertaConfirmation(String titulo, String texto) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.initStyle(StageStyle.UTILITY);
        ButtonType buttonSim = new ButtonType("Sim", ButtonData.YES);
        ButtonType buttonNao = new ButtonType("Não", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonSim, buttonNao);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == buttonSim);
    }

}
