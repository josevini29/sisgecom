package br.integrado.jnpereira.nutrimix.tools;

import br.integrado.jnpereira.nutrimix.controle.FrmListaGenericaFXML;
import br.integrado.jnpereira.nutrimix.controle.FrmListaPessoaFXML;
import br.integrado.jnpereira.nutrimix.controle.FrmMenuFXML;
import br.integrado.jnpereira.nutrimix.controle.FrmListaAjustEstoqFXML;
import java.io.IOException;
import java.lang.reflect.Method;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Tela {

    final public static String[] MENU = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmMenuFXML.fxml", "SISGECOM - Sistema de Gerenciamento Comercial"};
    final public static String[] CAD_BANCO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadBancoFXML.fxml", "Cadastro de Banco"};
    final public static String[] CAD_GRUPO_PRODUTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadGrupoProdFXML.fxml", "Cadastro de Grupo de Produtos"};
    final public static String[] CAD_TIPO_DESPESA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadTipoDespesaFXML.fxml", "Cadastro de Tipo de Despesa"};
    final public static String[] CAD_UNID_PADRAO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadUnidPadraoFXML.fxml", "Cadastro de Unidade Padrão"};
    final public static String[] CAD_ESTADO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadEstadoFXML.fxml", "Cadastro de Estado"};
    final public static String[] CAD_FORMA_PAGTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFormaPagtoFXML.fxml", "Cadastro de Forma de Pagamento"};
    final public static String[] CAD_PRECO_PROD = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadPrecoProdFXML.fxml", "Cadastro de Preço Produto"};
    final public static String[] CAD_CIDADE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadCidadeFXML.fxml", "Cadastro de Cidade"};
    final public static String[] CAD_PRODUTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadProdutoFXML.fxml", "Cadastro de Produto"};
    final public static String[] CAD_AJUSTPROD = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadAjustEstoqFXML.fxml", "Ajuste de Estoque"};
    final public static String[] CAD_CLIENTE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadClienteFXML.fxml", "Cadastro de Cliente"};
    final public static String[] CAD_FORNE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFornecedorFXML.fxml", "Cadastro de Fornecedor"};
    final public static String[] CAD_FUNC = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFuncionarioFXML.fxml", "Cadastro de Funcionário"};
    final public static String[] CON_MOVTO_ESTOQ = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConAjustEstoqFXML.fxml", "Consulta Movimento de Estoque"};

    public void abrirMenu(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU[0]));
        Parent root = (Parent) loader.load();
        FrmMenuFXML controler = (FrmMenuFXML) loader.getController();
        controler.setStage(stage);
        controler.iniciaTela();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
        stage.setTitle(MENU[1]);
        stage.setMaximized(true);
        stage.show();
    }

    public void abrirTelaModal(Stage stagePai, String[] tela) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(tela[0]));
            Parent root = (Parent) loader.load();
            Object controler = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(stagePai);
            stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
            stage.setTitle(tela[1]);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.centerOnScreen();
            stage.show();
            Method method;
            try {
                method = controler.getClass().getMethod("iniciaTela");
                method.invoke(controler);
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao chamar metodo inicia tela.\n" + ex.toString());
                ex.printStackTrace();
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            Alerta.AlertaError("Erro!", "Erro ao abrir a tela solicitada, entre em contato com o suporte.\n" + ex.toString());
        }
    }

    public String abrirListaGenerica(Object obj, String dsCampCod, String dsCampDes, String sqlAdd, String titulo) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/integrado/jnpereira/nutrimix/visao/FrmListaGenericaFXML.fxml"));
            Parent root = (Parent) loader.load();
            FrmListaGenericaFXML controler = (FrmListaGenericaFXML) loader.getController();
            controler.setClasse(obj);
            controler.setDsCampCod(dsCampCod);
            controler.setDsCampDes(dsCampDes);
            controler.setStage(stage);
            controler.setSqlAdd(sqlAdd);
            controler.iniciaTela();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.initOwner(stagePai);
            stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.centerOnScreen();
            stage.showAndWait();
            return controler.getDsRetorno();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao abrir a tela solicitada, entre em contato com o suporte.\n" + ex.toString());
        }
        return null;
    }

    public String abrirListaPessoa(Object obj, boolean inAtivo) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/integrado/jnpereira/nutrimix/visao/FrmListaPessoaFXML.fxml"));
            Parent root = (Parent) loader.load();
            FrmListaPessoaFXML controler = (FrmListaPessoaFXML) loader.getController();
            controler.setClasse(obj);
            controler.setStage(stage);
            controler.setInAtivo(inAtivo);
            controler.iniciaTela();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.centerOnScreen();
            stage.showAndWait();
            return controler.getDsRetorno();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao abrir a tela solicitada, entre em contato com o suporte.\n" + ex.toString());
        }
        return null;
    }
    
    public String abrirListaAjusteEstoq() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/integrado/jnpereira/nutrimix/visao/FrmListaAjustEstoqFXML.fxml"));
            Parent root = (Parent) loader.load();
            FrmListaAjustEstoqFXML controler = (FrmListaAjustEstoqFXML) loader.getController();
            controler.setStage(stage);
            controler.iniciaTela();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.centerOnScreen();
            stage.showAndWait();
            return controler.getDsRetorno();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao abrir a tela solicitada, entre em contato com o suporte.\n" + ex.toString());
        }
        return null;
    }

}
