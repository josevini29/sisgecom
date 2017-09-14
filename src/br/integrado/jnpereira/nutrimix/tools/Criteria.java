/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class Criteria {

    private ArrayList<CriteriaItem> criterios = new ArrayList<>();

    private Object classe = new Object();
    private String orderBy = new String();

    public Criteria(Object classe) {
        this.classe = classe;
    }

    public void AddAnd(String campo, Object valor, boolean consideraNulo) {
        criterios.add(new CriteriaItem(campo, valor, "AND", consideraNulo));
    }

    public void AddOrderByAsc(String campo) {
        String dsClasse = (String) classe.getClass().getSimpleName();
        String proposicao = "$" + dsClasse + "." + campo + "$ ASC";
        orderBy += (!orderBy.equals("") ? ", " + proposicao : proposicao);
    }

    public void AddOrderByDesc(String campo) {
        String dsClasse = (String) classe.getClass().getSimpleName();
        String proposicao = "$" + dsClasse + "." + campo + "$ DESC";
        orderBy += (!orderBy.equals("") ? ", " + proposicao : proposicao);
    }

    public String getWhereSql() throws Exception {
        String sql = "";
        Iterator it = criterios.iterator();
        for (int i = 0; it.hasNext(); i++) {
            CriteriaItem item = (CriteriaItem) it.next();
            if (item.valor != null || item.nulo) {
                Field field;
                try {
                    field = classe.getClass().getDeclaredField(item.campo);
                } catch (Exception ex) {
                    throw new Exception("Erro ao obter campo " + item.campo + ".\n" + ex.toString());
                }
                if (field.getType().getSimpleName().toLowerCase().equals("string")) {
                    //if (!((String) item.valor).equals("")) {
                    if (i > 0) {
                        sql += item.tipo + " ";
                    }
                    String tipo = (String) item.tipo;
                    String valor = (String) "'" + item.valor + "' ";
                    String dsClasse = (String) classe.getClass().getSimpleName();
                    String proposicao = "$" + dsClasse + "." + item.campo + "$ = " + valor;
                    //}
                } else if (field.getType().getSimpleName().toLowerCase().equals("integer")) {
                    if (!String.valueOf(item.valor).equals("")) {
                        if (i > 0) {
                            sql += item.tipo + " ";
                        }
                        String tipo = (String) item.tipo;
                        String valor = (String) item.valor;
                        String dsClasse = (String) classe.getClass().getSimpleName();
                        String proposicao = "$" + dsClasse + "." + item.campo + "$ = " + valor;
                        sql += proposicao;
                    }
                }
            }
        }

        if (!sql.equals("")) {
            sql = " WHERE " + sql;
        }
        if (!orderBy.equals("")) {
            sql += (" ORDER BY " + orderBy);
        }

        return sql;
    }

    private class CriteriaItem {

        private String campo;
        private Object valor;
        private String tipo;
        private boolean nulo;

        public CriteriaItem(String campo, Object valor, String tipo, boolean nulo) {
            this.campo = campo;
            this.valor = valor;
            this.tipo = tipo;
            this.nulo = nulo;
        }

    }
}
