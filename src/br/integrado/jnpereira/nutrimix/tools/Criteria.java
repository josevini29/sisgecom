/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Criteria {

    private ArrayList<CriteriaItem> criterios = new ArrayList<>();

    public void AddAnd(String campo, Object classe, Object valor, boolean consideraNulo) {
        criterios.add(new CriteriaItem(campo, classe, valor, "AND", consideraNulo));
    }

    public String getWhereSql() throws Exception {
        String sql = "";
        for (CriteriaItem item : criterios) {
            if (item.valor != null || item.nulo) {
                Field field;
                try {
                    field = item.classe.getClass().getDeclaredField(item.campo);
                } catch (Exception ex) {
                    throw new Exception("Erro ao obter campo " + item.campo + ".\n" + ex.toString());
                }
                if (field.getType().getSimpleName().toLowerCase().equals("string")) {
                    //if (!((String) item.valor).equals("")) {
                    String tipo = (String) item.tipo;
                    String valor = (String) "'" + item.valor + "' ";
                    String classe = (String) item.classe.getClass().getSimpleName();
                    String proposicao = "$" + classe + "." + item.campo + "$ = " + valor;
                    //}
                } else if (field.getType().getSimpleName().toLowerCase().equals("integer")) {
                    if (!String.valueOf(item.valor).equals("")) {
                        String tipo = (String) item.tipo;
                        String valor = (String) item.valor;
                        String classe = (String) item.classe.getClass().getSimpleName();
                        String proposicao = "$" + classe + "." + item.campo + "$ = " + valor;
                        sql += proposicao;
                    }
                }
            }
        }
        return sql;
    }

    private class CriteriaItem {

        private String campo;
        private Object classe;
        private Object valor;
        private String tipo;
        private boolean nulo;

        public CriteriaItem(String campo, Object classe, Object valor, String tipo, boolean nulo) {
            this.campo = campo;
            this.valor = valor;
            this.classe = classe;
            this.tipo = tipo;
            this.nulo = nulo;
        }

    }
}
