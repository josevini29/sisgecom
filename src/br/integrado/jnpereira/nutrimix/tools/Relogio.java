/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class Relogio extends Thread {

    private final Label label;

    public Relogio(Label label) {
        this.label = label;
    }

    private void atualizaRelogio() {
        Platform.runLater(() -> {
            try {
                SimpleDateFormat df = new SimpleDateFormat("EEEEEE, dd 'de' MMMM 'de' yyyy HH:mm:ss");
                label.setText(df.format(new Date()));
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    label.setText("Erro contate o suporte: " + ex.toString());
                });
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                atualizaRelogio();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Platform.runLater(() -> {
                    label.setText("Erro: " + ex.toString());
                });
                return;
            }
        }
    }
}
