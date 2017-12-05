package br.integrado.jnpereira.nutrimix.tools;

import br.integrado.jnpereira.nutrimix.dao.Dao;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Charts extends Thread {

    private AnchorPane paneChart;

    Dimension tela;
    double chartHeight;
    double chartWidth;
    double spaceHeight;
    double spaceWidth;

    Dao dao = new Dao();

    public Charts() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        tela = tk.getScreenSize();
        chartHeight = tela.height / 2.5;
        chartWidth = tela.width / 2.2;
        spaceHeight = tela.height / 25;
        spaceWidth = tela.width / 37;
        dao.autoCommit(false);
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            load();
        });
    }

    public void load() {
        try {
            paneChart.getChildren().clear();
            double spaceHeightY = spaceHeight + 15;
            paneChart.setPrefWidth(tela.width - spaceWidth);

            paneChart.getChildren().add(getButtonRefresh());

            //Grafico(1,1)
            Node grafico1 = getReceitaVsDespesa();
            grafico1.setLayoutY(spaceHeightY);
            grafico1.setLayoutX(spaceWidth);
            paneChart.getChildren().add(grafico1);
            grafico1.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            abrirGrafico(getReceitaVsDespesa());
                        } catch (Exception ex) {
                            Alerta.AlertaError("Erro!", "Erro ao gerar gráfico.\n" + ex.toString());
                        }
                    }
                }
            });

            //Grafico(1,2)
            Node grafico2 = getGrafPizzaFaturDiaSemana();
            grafico2.setLayoutY(spaceHeightY);
            grafico2.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            paneChart.getChildren().add(grafico2);
            spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna
            grafico2.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            abrirGrafico(getGrafPizzaFaturDiaSemana());
                        } catch (Exception ex) {
                            Alerta.AlertaError("Erro!", "Erro ao gerar gráfico.\n" + ex.toString());
                        }
                    }
                }
            });

            //Grafico(2,1)
            Node grafico3 = getGrafPizzaEstoqueNegativo();
            grafico3.setLayoutY(spaceHeightY);
            grafico3.setLayoutX(spaceWidth);
            paneChart.getChildren().add(grafico3);
            grafico3.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            abrirGrafico(getGrafPizzaEstoqueNegativo());
                        } catch (Exception ex) {
                            Alerta.AlertaError("Erro!", "Erro ao gerar gráfico.\n" + ex.toString());
                        }
                    }
                }
            });

            //Grafico(2,2)
            Node grafico4 = getFormPagtoUsada();
            grafico4.setLayoutY(spaceHeightY);
            grafico4.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            paneChart.getChildren().add(grafico4);
            spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna
            grafico4.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            abrirGrafico(getFormPagtoUsada());
                        } catch (Exception ex) {
                            Alerta.AlertaError("Erro!", "Erro ao gerar gráfico.\n" + ex.toString());
                        }
                    }
                }
            });
            
            //Grafico(2,1)
            //Node grafico5 = getTesteBarra();
            //grafico5.setLayoutY(spaceHeightY);
            //grafico5.setLayoutX(spaceWidth);
            //paneChart.getChildren().add(grafico5);
            //Grafico(2,2)
            //Node grafico6 = getGrafPizzaEntradaXSaida();
            //grafico6.setLayoutY(spaceHeightY);
            //grafico6.setLayoutX(spaceWidth + chartWidth + spaceWidth);
            //paneChart.getChildren().add(grafico6);
            //spaceHeightY += (chartHeight + spaceHeight); //somente no grafico da segundo coluna
        } catch (Exception ex) {
            paneChart.getChildren().clear();
            paneChart.getChildren().add(getLabelError("Erro ao gerar Dashboard, entre em contato com o suporte!\n" + getStack(ex)));
            ex.printStackTrace();
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

    private void abrirGrafico(Node grafico) {
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().add(grafico);
        anchor.getStylesheets().add("/br/integrado/jnpereira/nutrimix/css/style.css");
        Stage stage = new Stage();
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
        if (grafico instanceof LineChart) {
            LineChart graf = (LineChart) grafico;
            stage.setTitle(graf.getTitle());
            anchor.prefWidthProperty().bind(stage.widthProperty());
            anchor.prefHeightProperty().bind(stage.heightProperty());
            graf.prefWidthProperty().bind(anchor.widthProperty());
            graf.prefHeightProperty().bind(anchor.heightProperty());
        } else if (grafico instanceof PieChart) {
            PieChart graf = (PieChart) grafico;
            stage.setTitle(graf.getTitle());
            anchor.prefWidthProperty().bind(stage.widthProperty());
            anchor.prefHeightProperty().bind(stage.heightProperty());
            graf.prefWidthProperty().bind(anchor.widthProperty());
            graf.prefHeightProperty().bind(anchor.heightProperty());
        } else if (grafico instanceof BarChart) {
            BarChart graf = (BarChart) grafico;
            stage.setTitle(graf.getTitle());
            anchor.prefWidthProperty().bind(stage.widthProperty());
            anchor.prefHeightProperty().bind(stage.heightProperty());
            graf.prefWidthProperty().bind(anchor.widthProperty());
            graf.prefHeightProperty().bind(anchor.heightProperty());
        }
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    public Node getGrafPizzaEstoqueNegativo() throws Exception {
        PieChart graficoPizza = new PieChart();
        ResultSet rs;

        String sqlPrdNomal = "SELECT COUNT(*) QT_PRODUTO FROM "
                + "&Produto& WHERE $Produto.inAtivo$ = 'T' AND $Produto.inEstoque$ = 'T' AND $Produto.qtEstqAtual$ > $Produto.qtEstoqMin$";
        rs = dao.execSQL(sqlPrdNomal);
        rs.next();
        graficoPizza.getData().add(new PieChart.Data("Produto Estoque Normal: " + rs.getInt("QT_PRODUTO"), rs.getInt("QT_PRODUTO")));

        String sqlPrdMinimo = "SELECT COUNT(*) QT_PRODUTO FROM "
                + "&Produto& WHERE $Produto.inAtivo$ = 'T' AND $Produto.inEstoque$ = 'T' AND $Produto.qtEstqAtual$ <= $Produto.qtEstoqMin$";
        rs = dao.execSQL(sqlPrdMinimo);
        rs.next();
        graficoPizza.getData().add(new PieChart.Data("Produto Estoque Mínimo: " + rs.getInt("QT_PRODUTO"), rs.getInt("QT_PRODUTO")));

        graficoPizza.setPrefSize(chartWidth, chartHeight);
        graficoPizza.setTitle("Estoque Mínimo");
        return graficoPizza;
    }

    public Node getGrafPizzaFaturDiaSemana() throws Exception {
        String sql = "SELECT TO_CHAR($MovtoCaixa.dtMovto$, 'D') DIA_SEMANA , SUM($MovtoCaixa.vlMovto$) TOTAL "
                + "FROM &MovtoCaixa& GROUP BY DIA_SEMANA;";

        ResultSet rs = dao.execSQL(sql);
        PieChart graficoPizza = new PieChart();
        while (rs.next()) {
            graficoPizza.getData().add(new PieChart.Data(Data.getDiaSemana(rs.getInt("dia_semana")) + ": " + Numero.doubleToR$(rs.getDouble("TOTAL")), rs.getDouble("TOTAL")));
        }
        graficoPizza.setPrefSize(chartWidth, chartHeight);
        graficoPizza.setTitle("Faturamento Dia Semana");
        return graficoPizza;
    }

    public Node getReceitaVsDespesa() throws Exception {

        String sqlReceita = "SELECT TO_CHAR(DT_MOVTO, 'MM-YYYY') MES_ANO, SUM(VL_MOVTO) TOTAL FROM NUTRIMIX.MOVTO_CAIXA \n"
                + "WHERE TP_MOVTO_CAIXA = 'E' AND TO_CHAR(NOW(), 'YYYY') = TO_CHAR(DT_MOVTO, 'YYYY')\n"
                + "GROUP BY MES_ANO;";

        ResultSet rsReceita = dao.execSQL(sqlReceita);

        String sqlDespesa = "SELECT TO_CHAR(DT_MOVTO, 'MM-YYYY') MES_ANO, SUM(VL_MOVTO) TOTAL FROM NUTRIMIX.MOVTO_CAIXA \n"
                + "WHERE TP_MOVTO_CAIXA = 'S' AND TO_CHAR(NOW(), 'YYYY') = TO_CHAR(DT_MOVTO, 'YYYY')\n"
                + "GROUP BY MES_ANO;";

        ResultSet rsDespesa = dao.execSQL(sqlDespesa);

        LineChart graficoLinha = new LineChart<>(
                new CategoryAxis(), new NumberAxis());

        XYChart.Series receita = new XYChart.Series();
        receita.setName("Receita");

        while (rsReceita.next()) {
            receita.getData().add(new XYChart.Data(rsReceita.getString("MES_ANO"), rsReceita.getDouble("TOTAL")));
        }

        XYChart.Series despesa = new XYChart.Series();
        despesa.setName("Despesa");

        while (rsDespesa.next()) {
            despesa.getData().add(new XYChart.Data(rsDespesa.getString("MES_ANO"), rsDespesa.getDouble("TOTAL")));
        }

        graficoLinha.getData().addAll(receita, despesa);
        graficoLinha.setPrefSize(chartWidth, chartHeight);
        graficoLinha.setTitle("Receita Vs Despesa (12 Meses)");
        return graficoLinha;
    }

    public Node getFormPagtoUsada() throws Exception {
        String SQL = "SELECT A.DS_FORMA FORMA,\n"
                + "(((SELECT SUM(VL_MOVTO) FROM NUTRIMIX.MOVTO_CAIXA B WHERE B.CD_FORPAGTO = A.CD_FORMA AND B.TP_MOVTO_CAIXA = 'E') / (SELECT SUM(VL_MOVTO) FROM NUTRIMIX.MOVTO_CAIXA B WHERE B.TP_MOVTO_CAIXA = 'E')) * 100) PER\n"
                + "FROM NUTRIMIX.FORMA_PAGTO A;";

        PieChart graficoPizza = new PieChart();
        ResultSet rs = dao.execSQL(SQL);
        while (rs.next()) {
            graficoPizza.getData().add(new PieChart.Data(rs.getString("FORMA") + ": " + Numero.doubleToReal(rs.getDouble("PER"), 1) + "%", rs.getDouble("PER")));
        }
        graficoPizza.setPrefSize(chartWidth, chartHeight);
        graficoPizza.setTitle("Forma de Pagamento Mais Utilizada");
        return graficoPizza;
    }

    public AnchorPane getPaneChart() {
        return paneChart;
    }

    public void setPaneChart(AnchorPane paneChart) {
        this.paneChart = paneChart;
    }

}
