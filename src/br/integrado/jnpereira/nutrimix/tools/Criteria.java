/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.tools;

import java.util.ArrayList;

public class Criteria {

    private ArrayList<CriteriaItem> criterios = new ArrayList<>();

    public void AddAnd(String campo, Class classe, Object valor) {
        criterios.add(new CriteriaItem(campo, classe, valor, "AND"));
    }

    public String getWhereSql() {
        String sql = new String();
        for (CriteriaItem item : criterios) {
            if (item.valor != null) {
                if (item.valor instanceof String) {
                    if (!((String) item.valor).equals("")) {
                        String tipo = (String) item.tipo;
                        String valor = (String) "'" + item.valor + "' ";
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

        public CriteriaItem(String campo, Object classe, Object valor, String tipo) {
            this.campo = campo;
            this.valor = valor;
            this.classe = classe;
            this.tipo = tipo;
        }

    }
}
