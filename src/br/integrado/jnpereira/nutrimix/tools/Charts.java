package br.integrado.jnpereira.nutrimix.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Charts extends Thread {

    private AnchorPane paneChart;

    Dimension tela;
    double chartHeight;
    double chartWidth;
    double spaceHeight;
    double spaceWidth;

    public Charts() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        tela = tk.getScreenSize();
        chartHeight = tela.height / 2.5;
        chartWidth = tela.width / 2.2;
        spaceHeight = tela.height / 25;
        spaceWidth = tela.width / 37;
    }

    @Override
    public void run() {
        load();
    }

    public void load() {
        try {
            paneChart.getChildren().clear();
            double spaceHeightY = spaceHeight + 15;
            paneChart.setPrefWidth(tela.width - spaceWidth);

            paneChart.getChildren().add(getButtonRefresh());

            //Grafico(1,1)
            Node grafico1 = getGrafPizzaEntradaXSaida();
            grafico1.setLayoutY(spaceHeightY);
            grafico1.setLayoutX(spaceWidth);
            paneChart.getChildren().add(grafico1);

            //Grafico(1,2)
            Node grafico2 = getTesteLinha();
            grafico2.setLayoutY(spaceHeightY);
            grafico2.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            paneChart.getChildren().add(grafico2);
            spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna

            //Grafico(2,1)
            Node grafico3 = getTesteBarra();
            grafico3.setLayoutY(spaceHeightY);
            grafico3.setLayoutX(spaceWidth);
            paneChart.getChildren().add(grafico3);

            //Grafico(2,2)
            Node grafico4 = getGrafPizzaEntradaXSaida();
            grafico4.setLayoutY(spaceHeightY);
            grafico4.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            paneChart.getChildren().add(grafico4);
            spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna
            
                        //Grafico(2,1)
            Node grafico5 = getTesteBarra();
            grafico5.setLayoutY(spaceHeightY);
            grafico5.setLayoutX(spaceWidth);
            paneChart.getChildren().add(grafico5);

            //Grafico(2,2)
            Node grafico6 = getGrafPizzaEntradaXSaida();
            grafico6.setLayoutY(spaceHeightY);
            grafico6.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            paneChart.getChildren().add(grafico6);
            spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna
            
        } catch (Exception ex) {
            paneChart.getChildren().clear();
            paneChart.getChildren().add(getLabelError("Erro ao gerar Dashboard, entre em contato com o suporte!\n" + getStack(ex)));
        }

    }

    public static String getStack(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return (sw.toString());
    }

    public Node getButtonRefresh() {
        Button btnRefresh = new Button();
        btnRefresh.setPrefSize(230, 30);
        btnRefresh.setLayoutX(((spaceWidth * 2) + (chartWidth * 2)) - btnRefresh.getPrefWidth());
        btnRefresh.setLayoutY(8);
        btnRefresh.getStyleClass().add("button_refresh");
        IconButtonHit.setIconComTexto(btnRefresh, IconButtonHit.ICON_REFRESH);
        btnRefresh.setText("Atualizar - " + Data.AmericaToBrasil(Data.getAgora()));
        btnRefresh.setOnAction((ActionEvent event) -> {
            load();
        });
        return btnRefresh;
    }

    public Node getLabelError(String dsError) {
        Label lblRefresh = new Label();
        lblRefresh.setLayoutX(spaceWidth);
        lblRefresh.setLayoutY(8);
        lblRefresh.setText(dsError);
        return lblRefresh;
    }

    public Node getGrafPizzaEntradaXSaida() {
        PieChart graficoPizza = new PieChart();
        graficoPizza.getData().addAll(new PieChart.Data("Trimestre 1", 11),
                new PieChart.Data("Trimestre 2", 1),
                new PieChart.Data("Trimestre 3", 34),
                new PieChart.Data("Trimestre 5", 12));
        graficoPizza.setPrefSize(chartWidth, chartHeight);
        graficoPizza.setTitle("Saídas X Entradas");
        return graficoPizza;
    }

    public Node getTesteLinha() {
        LineChart graficoLinha = new LineChart<>(new CategoryAxis(), new NumberAxis());
        final String T1 = "T1";
        final String T2 = "T2";
        final String T3 = "T3";
        final String T4 = "T4";

        XYChart.Series prod1 = new XYChart.Series();
        prod1.setName("Produto 1");

        prod1.getData().add(new XYChart.Data(T1, 5));
        prod1.getData().add(new XYChart.Data(T2, -2));
        prod1.getData().add(new XYChart.Data(T3, 3));
        prod1.getData().add(new XYChart.Data(T4, 15));

        XYChart.Series prod2 = new XYChart.Series();
        prod2.setName("Produto 2");

        prod2.getData().add(new XYChart.Data(T1, -5));
        prod2.getData().add(new XYChart.Data(T2, -1));
        prod2.getData().add(new XYChart.Data(T3, 12));
        prod2.getData().add(new XYChart.Data(T4, 8));

        XYChart.Series prod3 = new XYChart.Series();
        prod3.setName("Produto 3");

        prod3.getData().add(new XYChart.Data(T1, 12));
        prod3.getData().add(new XYChart.Data(T2, 15));
        prod3.getData().add(new XYChart.Data(T3, 12));
        prod3.getData().add(new XYChart.Data(T4, 20));
        graficoLinha.getData().addAll(prod1, prod2, prod3);
        graficoLinha.setPrefSize(chartWidth, chartHeight);
        graficoLinha.setTitle("Evolução Mês");
        return graficoLinha;
    }

    public Node getTesteBarra() {
        BarChart graficoBarra = new BarChart<>(new CategoryAxis(), new NumberAxis());
        final String T1 = "T1";
        final String T2 = "T2";
        final String T3 = "T3";
        final String T4 = "T4";

        XYChart.Series prod1 = new XYChart.Series();
        prod1.setName("Produto 1");

        prod1.getData().add(new XYChart.Data(T1, 5));
        prod1.getData().add(new XYChart.Data(T2, -2));
        prod1.getData().add(new XYChart.Data(T3, 3));
        prod1.getData().add(new XYChart.Data(T4, 15));

        XYChart.Series prod2 = new XYChart.Series();
        prod2.setName("Produto 2");

        prod2.getData().add(new XYChart.Data(T1, -5));
        prod2.getData().add(new XYChart.Data(T2, -1));
        prod2.getData().add(new XYChart.Data(T3, 12));
        prod2.getData().add(new XYChart.Data(T4, 8));

        XYChart.Series prod3 = new XYChart.Series();
        prod3.setName("Produto 3");

        prod3.getData().add(new XYChart.Data(T1, 12));
        prod3.getData().add(new XYChart.Data(T2, 15));
        prod3.getData().add(new XYChart.Data(T3, 12));
        prod3.getData().add(new XYChart.Data(T4, 20));
        graficoBarra.getData().addAll(prod1, prod2, prod3);
        graficoBarra.setPrefSize(chartWidth, chartHeight);
        graficoBarra.setTitle("Evolução Mês");
        return graficoBarra;
    }

    public AnchorPane getPaneChart() {
        return paneChart;
    }

    public void setPaneChart(AnchorPane paneChart) {
        this.paneChart = paneChart;
    }

}
