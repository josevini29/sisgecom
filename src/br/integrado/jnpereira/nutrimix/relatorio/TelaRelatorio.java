package br.integrado.jnpereira.nutrimix.relatorio;

import br.integrado.jnpereira.nutrimix.tools.Alerta;
import br.integrado.jnpereira.nutrimix.tools.Data;
import br.integrado.jnpereira.nutrimix.tools.FuncaoCampo;
import br.integrado.jnpereira.nutrimix.tools.IconButtonHit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaRelatorio {

    private String nmTela = null;

    public void abrirRelatorio(Stage stagePai, AnchorPane tela) {
        tela.getStylesheets().add("/br/integrado/jnpereira/nutrimix/css/style.css");
        Stage stage = new Stage();
        Scene scene = new Scene(tela);
        stage.setScene(scene);
        stage.initOwner(stagePai);
        stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
        stage.setTitle(nmTela);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.centerOnScreen();
        stage.show();
    }

    public AnchorPane telaEstoqueProduto() {
        AnchorPane painel = new AnchorPane();
        painel.setPrefSize(250, 150);
        nmTela = "Relatório de Estoque";

        CheckBox inEstoqueMin = new CheckBox();
        inEstoqueMin.setText("Apenas Estoque Mínimo");
        inEstoqueMin.setLayoutX(50);
        inEstoqueMin.setLayoutY(50);
        painel.getChildren().add(inEstoqueMin);

        Button btnGerar = new Button();
        btnGerar.setText("Gerar Relatório");
        btnGerar.setLayoutX(60);
        btnGerar.setLayoutY(95);
        IconButtonHit.setIconComTexto(btnGerar, IconButtonHit.ICON_REPORT);
        btnGerar.setOnAction((ActionEvent event) -> {
            Relatorio relatorio = new Relatorio();
            relatorio.gerarRelatorioEstoque(inEstoqueMin.isSelected());
        });

        painel.getChildren().add(btnGerar);
        return painel;
    }

    public AnchorPane telaCaixa() {
        AnchorPane painel = new AnchorPane();
        painel.setPrefSize(250, 150);
        nmTela = "Relatório de Caixa";

        TextField campo = new TextField();
        campo.setText(Data.AmericaToBrasilMes(Data.getAgora()) + "/" + Data.AmericaToBrasilAno(Data.getAgora()));
        campo.setPrefWidth(80);
        campo.setLayoutX(85);
        campo.setLayoutY(50);
        painel.getChildren().add(campo);

        FuncaoCampo.mascaraTexto(campo, 7);
        campo.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!campo.getText().equals("")) {
                    try {
                        Date data = Data.StringToDate("01/"+campo.getText());       
                    } catch (Exception ex) {
                        Alerta.AlertaError("Campo inválido", ex.getMessage());
                        campo.requestFocus();
                    }
                }
            }
        });

        Button btnGerar = new Button();
        btnGerar.setText("Gerar Relatório");
        btnGerar.setLayoutX(60);
        btnGerar.setLayoutY(95);
        IconButtonHit.setIconComTexto(btnGerar, IconButtonHit.ICON_REPORT);
        btnGerar.setOnAction((ActionEvent event) -> {
            Relatorio relatorio = new Relatorio();
            try {
                relatorio.gerarRelatorioCaixa(Data.StringToDate("01/"+campo.getText()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        painel.getChildren().add(btnGerar);
        return painel;
    }

}
