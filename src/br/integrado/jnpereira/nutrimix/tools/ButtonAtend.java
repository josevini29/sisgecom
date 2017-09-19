package br.integrado.jnpereira.nutrimix.tools;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import br.integrado.jnpereira.nutrimix.modelo.Atendimento;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.TextAlignment;

public class ButtonAtend extends Thread {

    private AnchorPane paneAtend;
    private final Dao dao = new Dao();
    private double layoutY;
    private double layoutX;
    DecimalFormat decFormat = new DecimalFormat("00");
    double spaceHeight = 17;
    double spaceWidth = 24;
    int btnAtendWidth = 97;
    int btnAtendHeight = 102;
    int btnAcertoWidth = 97;
    int btnAcertoHeight = 26;
    int qtLinha;

    public ButtonAtend() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension tela = tk.getScreenSize();
        qtLinha = (int) (tela.width / (spaceWidth + btnAtendWidth));
        layoutY = spaceHeight;
        layoutX = spaceWidth;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            load();
        });
    }

    private void load() {
        try {
            paneAtend.getChildren().clear();
            ArrayList<Object> atendimentos = dao.getAllWhere(new Atendimento(), " WHERE $Atendimento.stAtend$ = '1' ORDER BY $Atendimento.nrMesa$ ASC");
            Iterator it = atendimentos.iterator();
            int f = 0;
            for (int i = 0; it.hasNext(); i++) {
                f++;
                Atendimento atend = (Atendimento) it.next();
                AtendimentoHit atendHit = new AtendimentoHit();
                atendHit.atend = atend;
                addButton(atendHit);
                layoutX += (btnAtendWidth + spaceWidth);
                if (f >= qtLinha) {
                    layoutY += (btnAcertoHeight + btnAtendHeight + spaceHeight);
                    layoutX = spaceWidth;
                    f = 0;
                }
                paneAtend.setPrefHeight(layoutY);
            }

        } catch (Exception ex) {
            Alerta.AlertaError("Erro!", ex.toString());
        }
    }

    private void addButton(AtendimentoHit atendHit) {
        Button btnAtend = atendHit.btnAtend;
        btnAtend.setPrefSize(btnAtendWidth, btnAtendHeight);
        btnAtend.setLayoutX(layoutX);
        btnAtend.setLayoutY(layoutY);
        btnAtend.setText("Mesa " + decFormat.format(atendHit.atend.getNrMesa()));
        btnAtend.setGraphicTextGap(20);
        btnAtend.setContentDisplay(ContentDisplay.BOTTOM);
        btnAtend.setAlignment(Pos.TOP_CENTER);
        btnAtend.setMnemonicParsing(false);
        btnAtend.setTextAlignment(TextAlignment.CENTER);
        IconButtonHit.setIconAtend(btnAtend, IconButtonHit.ICON_MESA);
        paneAtend.getChildren().add(btnAtend);
        btnAtend.setOnAction((ActionEvent event) -> {

        });

        Button btnAcerto = atendHit.btnAcerto;
        btnAcerto.setPrefSize(btnAcertoWidth, btnAcertoHeight);
        btnAcerto.setLayoutX(layoutX);
        btnAcerto.setLayoutY(layoutY + btnAtendHeight);
        btnAcerto.setText("Acerto");
        btnAcerto.setContentDisplay(ContentDisplay.LEFT);
        btnAcerto.setAlignment(Pos.BOTTOM_CENTER);
        btnAcerto.setTextAlignment(TextAlignment.LEFT);
        IconButtonHit.setIconComTexto(btnAcerto, IconButtonHit.ICON_DINHEIRO);
        paneAtend.getChildren().add(btnAcerto);
    }

    public AnchorPane getPaneAtend() {
        return paneAtend;
    }

    public void setPaneAtend(AnchorPane paneAtend) {
        this.paneAtend = paneAtend;
    }

    private class AtendimentoHit {
        Atendimento atend;
        Button btnAtend = new Button();
        Button btnAcerto = new Button();
    }

}
