/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.integrado.jnpereira.nutrimix.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dao extends Conexao {

    private String TABELA;

    public void save(Object obj) throws Exception {
        validaClasse(obj);
        String sql = sqlInsert(obj); //Gera o SQL de INSERT
        if (sql.contains("RETURNING")) {
            ResultSet result = executarQuerySQL(sql); //Executa o SQL no Banco
            recuperaId(obj, result); //Recupera o ID do insert para colocar no Objeto
        } else {
            executarSQL(sql);
        }

    }

    public void update(Object obj) throws Exception {
        validaClasse(obj);
        String sql = sqlUpdate(obj); //Gera o SQL de UPDATE
        executarSQL(sql); //Executa o SQL no Banco
    }

    public void delete(Object obj) throws Exception {
        get(obj);
        String sql = sqlDelete(obj); //Gera o SQL de DELETE
        executarSQL(sql); //Executa o SQL no Banco
    }

    public void get(Object obj) throws Exception {
        validaClasse(obj);
        String sql = sqlSelect(obj);//Gera o SQL de SELECT
        ResultSet result = executarQuerySQL(sql);
        atribuiVlCampos(obj, result); // joga os valores do resultset no objeto
    }

    public ArrayList<Object> getAllWhere(Object obj, String sqlWhere) throws Exception {
        validaClasse(obj);
        String sql = "SELECT * FROM " + getSchema() + "." + TABELA;
        if (sqlWhere != null) {
            sql += " " + geraSQLGetWhere(obj, sqlWhere);
        }
        sql += ";";
        ResultSet result = executarQuerySQL(sql);
        return listaCamposClasse(obj, result);// joga os valores do resultset no ArrayList
    }

    public long getCountWhere(Object obj, String sqlWhere) throws Exception {
        validaClasse(obj);
        String sql = "SELECT COUNT(*) CONTADOR FROM " + getSchema() + "." + TABELA;
        if (sqlWhere != null) {
            sql += " " + geraSQLGetWhere(obj, sqlWhere);
        }
        sql += ";";
        ResultSet result = executarQuerySQL(sql);
        result.next();
        return result.getLong("CONTADOR");// joga os valores do resultset no ArrayList
    }

    public ResultSet execSQL(String sql) throws Exception {
        sql = geraSQLGetWhere(null, sql);
        return executarQuerySQL(sql);
    }

    //Função: Gera o sql para um insert.
    private String sqlInsert(Object obj) throws Exception {
        String sql;
        String fields = new String();
        String values = new String();
        String ai = new String();
        boolean inAutoIncrement = false;

        Class cls = obj.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Coluna.class)) {
                if (!field.isAnnotationPresent(AutoIncrement.class)) {

                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;

                    if (field.get(obj) != null) {
                        fields += m.nome().toUpperCase() + ",";
                        switch (field.getType().getSimpleName().toLowerCase()) {
                            case "string"://colocar aspa simples em textos
                                String valor = ((String) field.get(obj));
                                validaAspas(valor);
                                values += "'" + valor + "',";
                                break;
                            case "date"://trata campo data
                                SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String data = in.format((Date) field.get(obj));
                                values += "'" + data + "',";
                                break;
                            case "boolean"://Converte padrao java true e false em 1 e 0 para o banco
                                values += (((Boolean) field.get(obj)) ? "'T'," : "'F',");
                                break;
                            default:
                                values += field.get(obj).toString() + ",";
                                break;
                        }
                    }

                } else {
                    inAutoIncrement = true;
                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;
                    ai += m.nome().toUpperCase() + ",";
                }

            }
        }
        values = values.substring(0, values.length() - 1);//Retira a virgula que sobra no final
        fields = fields.substring(0, fields.length() - 1);//Retira a virgula que sobra no final
        if (inAutoIncrement) {
            ai = ai.substring(0, ai.length() - 1);//Retira a virgula que sobra no final
            sql = "INSERT INTO " + getSchema() + "." + TABELA + " (" + fields
                    + ") VALUES (" + values + ") RETURNING " + ai + ";";
        } else {
            sql = "INSERT INTO " + getSchema() + "." + TABELA + " (" + fields
                    + ") VALUES (" + values + ");";
        }

        return sql;
    }

    //Função: Gera o sql para um insert.
    private String sqlUpdate(Object obj) throws Exception {
        String sql;
        String fieldsWhere = new String();
        String fieldsUpdate = new String();

        Class cls = obj.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Coluna.class)) {
                Annotation a = field.getAnnotation(Coluna.class);
                Coluna m = (Coluna) a;
                if (field.isAnnotationPresent(Id.class)) {

                    fieldsWhere += m.nome().toUpperCase() + " = ";

                    trataCampoNull(field, obj);//Trata se a chave de pesquina for nula ou 0.

                    switch (field.getType().getSimpleName().toLowerCase()) {
                        case "string"://colocar aspa simples em textos
                            String valor = ((String) field.get(obj));
                            validaAspas(valor);
                            fieldsWhere += "'" + valor + "'";
                            break;
                        case "date"://trata campo data
                            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String data = in.format((Date) field.get(obj));
                            fieldsWhere += "'" + data + "'";
                            break;
                        case "boolean"://Converte padrao java true e false em 1 e 0 para o banco
                            fieldsWhere += (((Boolean) field.get(obj)) ? "'T'" : "'F'");
                            break;
                        default:
                            fieldsWhere += field.get(obj).toString();
                            break;
                    }
                    fieldsWhere += " AND ";

                } else {

                    fieldsUpdate += m.nome().toUpperCase() + " = ";

                    if (field.get(obj) == null) {
                        fieldsUpdate += "NULL";
                    } else {
                        switch (field.getType().getSimpleName().toLowerCase()) {
                            case "string"://colocar aspa simples em textos
                                String valor = ((String) field.get(obj));
                                validaAspas(valor);
                                fieldsUpdate += "'" + valor + "'";
                                break;
                            case "date"://trata campo data
                                SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String data = in.format((Date) field.get(obj));
                                fieldsUpdate += "'" + data + "'";
                                break;
                            case "boolean"://Converte padrao java true e false em 1 e 0 para o banco
                                fieldsUpdate += (((Boolean) field.get(obj)) ? "'T'" : "'F'");
                                break;
                            default:
                                fieldsUpdate += field.get(obj).toString();
                                break;
                        }
                    }
                    fieldsUpdate += ",";
                }
            }
        }
        fieldsUpdate = fieldsUpdate.substring(0, fieldsUpdate.length() - 1);//Retira a virgula que sobra no final
        fieldsWhere = fieldsWhere.substring(0, fieldsWhere.length() - 5);//Retira o AND que sobra no final
        sql = "UPDATE " + getSchema() + "." + TABELA + " SET " + fieldsUpdate
                + " WHERE " + fieldsWhere + ";";

        return sql;
    }

    //Função: Gera o sql para um delete.
    private String sqlDelete(Object obj) throws Exception {
        String sql;
        String fieldsCriteria = new String();

        Class cls = obj.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Coluna.class)) {
                if (field.isAnnotationPresent(Id.class)) {

                    trataCampoNull(field, obj);//Trata se a chave de pesquina for nula ou 0.

                    String values;
                    switch (field.getType().getSimpleName().toLowerCase()) {
                        case "string"://colocar aspa simples em textos
                            values = "'" + ((String) field.get(obj)) + "'";
                            break;
                        case "date"://trata campo data
                            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String data = in.format(((Date) field.get(obj)).toString());
                            values = "'" + data + "'";
                            break;
                        case "boolean"://Converte padrao java true e false em 1 e 0 para o banco
                            values = (((Boolean) field.get(obj)) ? "'T'" : "'F'");
                            break;
                        default:
                            values = field.get(obj).toString();
                            break;
                    }

                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;
                    fieldsCriteria += m.nome().toUpperCase() + " = " + values + " AND ";
                }
            }
        }
        fieldsCriteria = fieldsCriteria.substring(0, fieldsCriteria.length() - 5);//Retira o AND que sobra no final
        sql = "DELETE FROM " + getSchema() + "." + TABELA
                + " WHERE " + fieldsCriteria + ";";
        return sql;
    }

    //Função: Gera o sql para um select por ID.
    private String sqlSelect(Object obj) throws IllegalArgumentException, IllegalAccessException, Exception {
        String sql;
        String fieldsCriteria = new String();

        Class cls = obj.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Coluna.class)) {
                if (field.isAnnotationPresent(Id.class)) {

                    trataCampoNull(field, obj);//Trata se a chave de pesquina for nula ou 0.

                    String values;
                    switch (field.getType().getSimpleName().toLowerCase()) {
                        case "string"://colocar aspa simples em textos
                            values = "'" + ((String) field.get(obj)) + "'";
                            break;
                        case "boolean"://Converte padrao java true e false em 1 e 0 para o banco
                            values = (((boolean) field.get(obj)) ? "'T'" : "'F'");
                            break;
                        default:
                            values = field.get(obj).toString();
                            break;
                    }

                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;
                    fieldsCriteria += m.nome().toUpperCase() + " = " + values + " AND ";
                }
            }
        }
        fieldsCriteria = fieldsCriteria.substring(0, fieldsCriteria.length() - 5);//Retira o AND que sobra no final
        sql = "SELECT * FROM " + getSchema() + "." + TABELA
                + " WHERE " + fieldsCriteria + ";";
        return sql;
    }

    //Função: Apos um insert atribui os valores das pks geradas por autoincrement no seu determinado atributo.
    private void recuperaId(Object obj, ResultSet result) throws Exception {
        if (result.next()) {
            Class cls = obj.getClass();
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Coluna.class)) {
                    if (field.isAnnotationPresent(AutoIncrement.class)) {
                        for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                            Annotation a = field.getAnnotation(Coluna.class);
                            Coluna m = (Coluna) a;
                            if (m.nome().equals(result.getMetaData().getColumnName(i))) {
                                Integer id = result.getInt(i);
                                field.set(obj, id);
                            }
                        }
                    }
                }
            }
        }
    }

    //Função: Atribui os valores de um resultset para os atributos do objeto quando utiliza um array de retorno.
    public Object atribuiVlCamposArray(Object obj, ResultSet result) throws Exception {
        Object objClone = Class.forName(obj.getClass().getName()).newInstance();
        Class cls = Class.forName(obj.getClass().getName()).newInstance().getClass();
        if (result.next()) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Coluna.class)) {
                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;
                    if (result.getObject(m.nome()) != null) {
                        switch (field.getType().getSimpleName().toLowerCase()) {
                            case "boolean": //Converte padrao 1 e 0 do banco para true e false
                                field.set(objClone, result.getBoolean(m.nome()));
                                break;
                            case "double": //Converte padrao 1 e 0 do banco para true e false
                                field.set(objClone, result.getDouble(m.nome()));
                                break;
                            default:
                                field.set(objClone, result.getObject(m.nome()));
                        }
                    }
                }
            }
        } else {
            return null;
        }
        return objClone;
    }

    //Função: Atribui os valores de um resultset para os atributos do objeto.
    public void atribuiVlCampos(Object obj, ResultSet result) throws Exception {
        Class cls = obj.getClass();
        if (result.next()) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Coluna.class)) {
                    Annotation a = field.getAnnotation(Coluna.class);
                    Coluna m = (Coluna) a;
                    if (result.getObject(m.nome()) != null) {
                        switch (field.getType().getSimpleName().toLowerCase()) {
                            case "boolean": //Converte padrao 1 e 0 do banco para true e false
                                field.set(obj, result.getBoolean(m.nome()));
                                break;
                            case "double": //Converte padrao 1 e 0 do banco para true e false
                                field.set(obj, result.getDouble(m.nome()));
                                break;
                            default:
                                field.set(obj, result.getObject(m.nome()));
                        }
                    }
                }
            }
        } else {
            throw new Exception("Nenhum registro encontrado.");
        }
    }

    //Função: Gera o Array para consultas com mais de um objeto.
    public ArrayList<Object> listaCamposClasse(Object obj, ResultSet result) throws Exception {
        ArrayList<Object> array = new ArrayList<>();
        while (true) {
            Object objAdd = (Object) atribuiVlCamposArray(obj, result);
            if (objAdd == null) {
                break;
            }
            array.add(objAdd);
        }
        return array;
    }

    //Função: Valida se o campo é nulo, vazio ou igual a 0.
    private void trataCampoNull(Field field, Object obj) throws Exception {
        boolean valido = true;
        if (field.get(obj) == null) {
            valido = false;
        } else {
            if (field.get(obj).toString().equals("")) {
                valido = false;
            } else if (!field.getType().getSimpleName().toLowerCase().equals("string")) {
                if (Double.parseDouble(field.get(obj).toString()) == 0) {
                    valido = false;
                }
            }
        }

        if (!valido) {
            throw new Exception("Todos os campos chave são necessários para realizar consulta.");
        }
    }

    //Função: Valida se a classe contém as informações necessarias
    public void validaClasse(Object obj) throws Exception {
        Class cls = obj.getClass();
        if (!cls.isAnnotationPresent(Tabela.class)) {
            throw new Exception("Classe informada não contém a Annotation @Tabela.");
        }

        Annotation a = cls.getAnnotation(Tabela.class);
        Tabela t = (Tabela) a;
        TABELA = t.nome().toUpperCase();

        boolean valida = false;
        for (Field field : cls.getDeclaredFields()) {
            if (field.getType().isPrimitive()) {
                throw new Exception("Variável primitiva não é permitida nas classes de modelos.");
            }
            if (field.isAnnotationPresent(Id.class) & field.isAnnotationPresent(Coluna.class)) {
                valida = true;
            }
        }

        if (!valida) {
            throw new Exception("Classe informada não contém nenhum atributo com a Annotation @Id e @Coluna.");
        }
    }

    private void validaAspas(String valor) throws Exception {
        if (valor.indexOf("'") > 0) {
            throw new Exception("Proibido o uso de aspas simples em decrições.");
        }
    }

    private String geraSQLGetWhere(Object obj, String daoSQL) throws Exception {
        boolean inCampo = false;
        boolean inCampoComp = false;
        String campo = "";
        for (int qtCarac = 0; qtCarac < daoSQL.length(); qtCarac++) {
            if (daoSQL.charAt(qtCarac) == '$' & !inCampo) {
                inCampo = true;
            }

            if (inCampo) {
                campo += daoSQL.charAt(qtCarac);
            }

            if (daoSQL.charAt(qtCarac) == '$' & inCampo & campo.length() > 1) {
                inCampo = false;
                inCampoComp = true;
            }

            if (inCampoComp) {
                try {
                    if (campo.contains(".")) { //Caso tenha tabela.coluna
                        String nmClasse = campo.substring(1, campo.indexOf("."));
                        String nmField = campo.substring(campo.indexOf(".") + 1, campo.length() - 1);
                        Object classe = Class.forName("br.integrado.jnpereira.nutrimix.modelo." + nmClasse).newInstance();
                        Field field = classe.getClass().getDeclaredField(nmField);
                        Annotation a = field.getAnnotation(Coluna.class);
                        Annotation b = classe.getClass().getAnnotation(Tabela.class);
                        Tabela t = (Tabela) b;
                        Coluna m = (Coluna) a;
                        daoSQL = daoSQL.replace(campo, getSchema() + "." + t.nome().toUpperCase() + "." + m.nome().toUpperCase());
                        campo = "";
                        inCampoComp = false;
                        qtCarac = 0;
                    } else { //apenas coluna
                        Field field = obj.getClass().getDeclaredField(campo.substring(1, campo.length() - 1));
                        Annotation a = field.getAnnotation(Coluna.class);
                        Coluna m = (Coluna) a;
                        daoSQL = daoSQL.replace(campo, m.nome().toUpperCase());
                        campo = "";
                        inCampoComp = false;
                        qtCarac = 0;
                    }
                } catch (Exception ex) {
                    throw new Exception("Conjunto '" + campo + "' não encontrado no modelo.");
                }

            }
        }

        inCampo = false;
        inCampoComp = false;
        campo = "";
        for (int qtCarac = 0; qtCarac < daoSQL.length(); qtCarac++) {
            if (daoSQL.charAt(qtCarac) == '&' & !inCampo) {
                inCampo = true;
            }

            if (inCampo) {
                campo += daoSQL.charAt(qtCarac);
            }

            if (daoSQL.charAt(qtCarac) == '&' & inCampo & campo.length() > 1) {
                inCampo = false;
                inCampoComp = true;
            }

            if (inCampoComp) {
                try {
                    Object classe = Class.forName("br.integrado.jnpereira.nutrimix.modelo." + campo.substring(1, campo.length() - 1)).newInstance();
                    Annotation b = classe.getClass().getAnnotation(Tabela.class);
                    Tabela t = (Tabela) b;
                    daoSQL = daoSQL.replace(campo, getSchema() + "." + t.nome().toUpperCase());
                    campo = "";
                    inCampoComp = false;
                    qtCarac = 0;
                } catch (Exception ex) {
                    throw new Exception("Tabela '" + campo + "' não encontrado no modelo.");
                }

            }
        }
        return daoSQL;
    }
}
