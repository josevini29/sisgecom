/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.controle;

import br.integrado.jnpereira.nutrimix.dao.Conexao;
import br.integrado.jnpereira.nutrimix.tools.Tela;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Jose Vinicius
 */
public class SISGECOM extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Conexao cn = new Conexao();
        cn.conecta();
        Tela tela = new Tela();
        tela.abrirMenu(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
