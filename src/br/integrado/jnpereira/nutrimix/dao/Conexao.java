package br.integrado.jnpereira.nutrimix.dao;

import br.integrado.jnpereira.nutrimix.tools.Alerta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jose Vinicius 23/06/2016 Conexao com banco de dados Oracle 11g
 */
public class Conexao {
    //Postgres

    private final String DRIVER = "org.postgresql.Driver";
    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String SCHEMA = "nutrimix";
    private final String USER = "postgres";
    private final String PASSWORD = "root";

    /*//MySQL
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/topicosbd";
    private final String SCHEMA = "topicosbd";
    private final String USER = "root";
    private final String PASSWORD = "root";*/
    private static Connection Conexao;
    private Statement statement;
    private boolean AUTO_COMMIT = true;

    public Connection conecta() {
        if (Conexao != null) {
            return Conexao;
        } else {
            try {
                Class.forName(DRIVER);
                Conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conectado em Postgres.");
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 12505) {
                    Alerta.AlertaError("Erro!", "Banco de dados está fechado." + ex.toString());
                    System.exit(0);
                } else {
                    Alerta.AlertaError("Erro!", "Erro ao tentar conectar ao banco." + ex.toString());
                    System.exit(0);
                }
                return null;
            } catch (ClassNotFoundException ex) {
                Alerta.AlertaError("Erro!", "A um erro na biblioteca de conexão ou não foi encontrada.");
                return null;
            }
        }
        return Conexao;
    }

    public String getSchema() {
        return SCHEMA.toUpperCase();
    }

    public void desconecta() {
        try {
            Conexao.close();
            System.out.println("Desconectado de PostgreSQL.");
        } catch (SQLException ex) {
            Alerta.AlertaError("Erro!", "Erro ao tentar desconectar do banco.");
        }

    }

    public void autoCommit(boolean auto) {
        AUTO_COMMIT = auto;
    }

    public void commit() {
        try {
            System.out.println("SQL -> COMMIT;");
            Conexao.commit();
        } catch (SQLException ex) {
            System.out.println("Erro Commit -> " + ex.toString());
            //Alerta.AlertaError("Erro!", "Erro ao tentar commit do banco.");
        }

    }

    public void rollback() {
        try {
            System.out.println("SQL -> ROLLBACK;");
            Conexao.rollback();
        } catch (SQLException ex) {
            System.out.println("Erro Rollback -> " + ex.toString());
            //Alerta.AlertaError("Erro!", "Erro ao tentar rollback do banco.");
        }

    }

    protected boolean executarSQL(String sql) {
        conecta();
        try {
            Conexao.setAutoCommit(AUTO_COMMIT);
            statement = Conexao.createStatement();
            System.out.println((AUTO_COMMIT?"AC:ON":"AC:OFF")+" SQL -> " + sql);
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Alerta.AlertaError("Erro!", "Erro ao executar SQL." + ex.toString() + "\n SQL: " + sql);
            return false;
        }
        return true;
    }

    protected ResultSet executarQuerySQL(String sql) {
        conecta();
        try {
            Conexao.setAutoCommit(AUTO_COMMIT);
            statement = Conexao.createStatement();
            System.out.println("SQL -> " + sql);
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            Alerta.AlertaError("Erro!", "Erro ao executar SQL." + ex.toString() + "\n SQL: " + sql);
            return null;
        }
    }
}
