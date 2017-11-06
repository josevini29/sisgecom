package br.integrado.jnpereira.nutrimix.tools;

import br.integrado.jnpereira.nutrimix.controle.ListaGenericaControl;
import br.integrado.jnpereira.nutrimix.controle.ListaPessoaControl;
import br.integrado.jnpereira.nutrimix.controle.MenuControl;
import br.integrado.jnpereira.nutrimix.controle.ListaAjustEstoqControl;
import br.integrado.jnpereira.nutrimix.controle.ListaPedidoCompraControl;
import br.integrado.jnpereira.nutrimix.controle.LoginControl;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.dao.Senha;
import br.integrado.jnpereira.nutrimix.modelo.PerfilTela;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Tela {

    Dao dao = new Dao();

    final public static String[] MENU = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmMenuFXML.fxml", "SISGECOM - Sistema de Gerenciamento Comercial"};
    @Senha
    final public static String[] CAD_BANCO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadBancoFXML.fxml", "Cadastro de Banco"};
    @Senha
    final public static String[] CAD_GRUPO_PRODUTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadGrupoProdFXML.fxml", "Cadastro de Grupo de Produtos"};
    @Senha
    final public static String[] CAD_TIPO_DESPESA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadTipoDespesaFXML.fxml", "Cadastro de Tipo de Despesa"};
    @Senha
    final public static String[] CAD_UNID_PADRAO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadUnidPadraoFXML.fxml", "Cadastro de Unidade Padrão"};
    @Senha
    final public static String[] CAD_ESTADO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadEstadoFXML.fxml", "Cadastro de Estado"};
    @Senha
    final public static String[] CAD_FORMA_PAGTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFormaPagtoFXML.fxml", "Cadastro de Forma de Pagamento"};
    @Senha
    final public static String[] CAD_CONDICAO_PAGTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadCondicaoPagtoFXML.fxml", "Cadastro de Condição de Pagamento"};
    @Senha
    final public static String[] CAD_PRECO_PROD = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadPrecoProdFXML.fxml", "Cadastro de Preço Produto"};
    @Senha
    final public static String[] CAD_CIDADE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadCidadeFXML.fxml", "Cadastro de Cidade"};
    @Senha
    public static String[] CAD_PRODUTO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadProdutoFXML.fxml", "Cadastro de Produto"};
    @Senha
    final public static String[] CAD_AJUSTPROD = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadAjustEstoqFXML.fxml", "Ajuste de Estoque"};
    @Senha
    final public static String[] CAD_CLIENTE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadClienteFXML.fxml", "Cadastro de Cliente"};
    @Senha
    final public static String[] CAD_FORNE = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFornecedorFXML.fxml", "Cadastro de Fornecedor"};
    @Senha
    final public static String[] CAD_FUNC = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadFuncionarioFXML.fxml", "Cadastro de Funcionário"};
    @Senha
    final public static String[] CON_MOVTO_ESTOQ = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConMovtoEstoqueFXML.fxml", "Consulta Movimento de Estoque"};
    @Senha
    final public static String[] CON_MOVTO_CAIXA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConMovtoCaixaFXML.fxml", "Consulta Movimento de Caixa"};
    @Senha
    final public static String[] CAD_ATEND = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadAtendimentoFXML.fxml", "Atendimento a Mesa"};
    @Senha
    final public static String[] CAD_VENDA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadVendaFXML.fxml", "Venda"};
    @Senha
    final public static String[] CAD_COMPRA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadCompraFXML.fxml", "Compra"};
    @Senha
    final public static String[] CAD_PEDIDO_COMPRA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadPedidoCompraFXML.fxml", "Pedido de Compra"};
    final public static String[] CON_PARCELA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConParcelaFXML.fxml", "Consulta Parcelas"};
    final public static String[] LOGIN = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmLoginFXML.fxml", "Login"};
    final public static String[] CAD_ALTSENHA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadAltSenhaFXML.fxml", "Alteração de Senha"};
    @Senha
    final public static String[] CAD_USUARIO = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadUsuarioFXML.fxml", "Cadastro de Usuário"};
    @Senha
    final public static String[] CAD_PERFIL = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmCadPerfilFXML.fxml", "Cadastro de Pefil"};
    @Senha
    final public static String[] CON_CONTA_RECEBER = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConContasReceberFXML.fxml", "Consulta de Contas a Receber"};
    @Senha
    final public static String[] CON_CONTA_PAGAR = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConContasPagarFXML.fxml", "Consulta de Contas a Pagar"};
    @Senha
    final public static String[] PAG_PARCELA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmPagParcelaFXML.fxml", "Pagamento de Parcela"};
    final public static String[] CON_VENDA_COMPRA = new String[]{"/br/integrado/jnpereira/nutrimix/visao/FrmConVendaCompraFXML.fxml", "Consulta Venda/Compra"};

    public void abrirLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN[0]));
        Parent root = (Parent) loader.load();
        LoginControl controler = (LoginControl) loader.getController();
        controler.setStage(stage);
        controler.iniciaTela();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle(LOGIN[1]);
        stage.show();
    }

    public void abrirMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU[0]));
        Parent root = (Parent) loader.load();
        MenuControl controler = (MenuControl) loader.getController();
        Stage stage = new Stage();
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
            if (!validaAcesso(tela)) {
                return;
            }
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

    public void abrirTelaModalComStage(Stage stagePai, String[] tela) {
        try {
            if (!validaAcesso(tela)) {
                return;
            }
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(tela[0]));
            Parent root = (Parent) loader.load();
            Object controler = loader.getController();
            controler.getClass().getDeclaredField("stage").set(controler, stage);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            Alerta.AlertaError("Erro!", "Erro ao abrir a tela solicitada, entre em contato com o suporte.\n" + ex.toString());
        }
    }

    public void abrirTelaModalComParam(Stage stagePai, String[] tela, Object param) {
        try {
            if (!validaAcesso(tela)) {
                return;
            }

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(tela[0]));
            Parent root = (Parent) loader.load();
            Object controler = loader.getController();
            controler.getClass().getDeclaredField("param").set(controler, param);
            controler.getClass().getDeclaredField("stage").set(controler, stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(stagePai);
            stage.getIcons().add(new Image("/br/integrado/jnpereira/nutrimix/icon/logo.png"));
            stage.setTitle(tela[1]);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.centerOnScreen();
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, (WindowEvent window) -> {
                Method method;
                try {
                    method = controler.getClass().getMethod("iniciaTela");
                    method.invoke(controler);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    stage.close();
                }
            });

            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String abrirListaGenerica(Object obj, String dsCampCod, String dsCampDes, String sqlAdd, String titulo) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/integrado/jnpereira/nutrimix/visao/FrmListaGenericaFXML.fxml"));
            Parent root = (Parent) loader.load();
            ListaGenericaControl controler = (ListaGenericaControl) loader.getController();
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
            ListaPessoaControl controler = (ListaPessoaControl) loader.getController();
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
            ListaAjustEstoqControl controler = (ListaAjustEstoqControl) loader.getController();
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
    
    public String abrirListaPedidoCompra() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/integrado/jnpereira/nutrimix/visao/FrmListaPedidoCompraFXML.fxml"));
            Parent root = (Parent) loader.load();
            ListaPedidoCompraControl controler = (ListaPedidoCompraControl) loader.getController();
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

    private boolean validaAcesso(String[] telaArray) {
        if (MenuControl.usuarioAtivo == 0) { //Admin pula a validação
            return true;
        }
        String cdTela = null;
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Senha.class)) {
                try {
                    if (telaArray.equals(field.get(new Object()))) {
                        cdTela = field.getName();
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (cdTela != null) {
            Usuario usuario = new Usuario();
            try {
                usuario.setCdUsuario(MenuControl.usuarioAtivo);
                dao.get(usuario);
            } catch (Exception ex) {
                Alerta.AlertaError("Erro!", "Erro ao validar acesso a tela.\n" + ex.toString());
                return false;
            }
            PerfilTela perfilTela = new PerfilTela();
            perfilTela.setCdPerfil(usuario.getCdPerfil());
            perfilTela.setCdTela(cdTela);
            try {
                dao.get(perfilTela);
            } catch (Exception ex) {
                Alerta.AlertaError("Acesso Negado!", "Usuário não tem acesso a está tela.");
                return false;
            }
        }
        return true;
    }

}
