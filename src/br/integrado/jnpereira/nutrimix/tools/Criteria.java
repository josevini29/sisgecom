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
    private String sql = new String();

    public Criteria(Object classe) {
        this.classe = classe;
    }

    public void AddAnd(String campo, Object valor, boolean consideraNulo) throws Exception {
        if (valor != null || consideraNulo) {
            Field field;
            try {
                field = classe.getClass().getDeclaredField(campo);
            } catch (Exception ex) {
                throw new Exception("Erro ao obter campo " + campo + ".\n" + ex.toString());
            }
            if (field.getType().getSimpleName().toLowerCase().equals("string")) {
                if (!sql.equals("")) {
                    sql += " AND ";
                }
                valor = (String) "'" + valor + "' ";
                String dsClasse = (String) classe.getClass().getSimpleName();
                String proposicao = "$" + dsClasse + "." + campo + "$ " + (valor == null ? "is " : "= ") + valor;
            } else if (field.getType().getSimpleName().toLowerCase().equals("integer")) {
                if (!String.valueOf(valor).equals("")) {
                    if (!sql.equals("")) {
                        sql += " AND ";
                    }
                    valor = String.valueOf(valor);
                    String dsClasse = (String) classe.getClass().getSimpleName();
                    String proposicao = "$" + dsClasse + "." + campo + "$ " + (valor == null ? "is " : "= ") + valor;
                    sql += proposicao;
                }
            }
        }
    }

    public void AddAndBetweenDate(String campo, String dtInicio, String dtFim) throws Exception {
        if (dtInicio != null && dtFim != null) {
            if (!dtInicio.equals("") && !dtFim.equals("")) {
                Field field;
                try {
                    field = classe.getClass().getDeclaredField(campo);
                } catch (Exception ex) {
                    throw new Exception("Erro ao obter campo " + campo + ".\n" + ex.toString());
                }
                if (field.getType().getSimpleName().toLowerCase().equals("date")) {
                    if (!sql.equals("")) {
                        sql += "AND ";
                    }
                    dtInicio = "'" + dtInicio + " 00:00:00'";
                    dtFim = "'" + dtFim + " 23:59:59' ";
                    String dsClasse = (String) classe.getClass().getSimpleName();
                    String proposicao = "$" + dsClasse + "." + campo + "$ BETWEEN " + dtInicio + " AND " + dtFim;
                    sql += proposicao;
                }
            }
        }
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
        private Object valor1;
        private Object valor2;
        private String tipo;
        private boolean nulo;

        public CriteriaItem(String campo, Object valor1, Object valor2, String tipo, boolean nulo) {
            this.campo = campo;
            this.valor1 = valor1;
            this.valor2 = valor2;
            this.tipo = tipo;
            this.nulo = nulo;
        }

    }
}
