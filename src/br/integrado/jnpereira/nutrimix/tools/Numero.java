package br.integrado.jnpereira.nutrimix.tools;

import static br.integrado.jnpereira.nutrimix.controle.MenuControl.usuarioAtivo;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Funcionario;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.InputMismatchException;

public class Numero {

    public static String getCadastro(int cdUsercad, Date dtCadastro) {
        String nome = "<Nome não Encontrado>";
        try {
            Usuario usuario = new Usuario();
            Dao dao = new Dao();
            usuario.setCdUsuario(cdUsercad);
            dao.get(usuario);
            if (usuario.getCdFuncionario() == null) {
                nome = usuario.getDsLogin().toUpperCase();
            } else {
                Funcionario func = new Funcionario();
                func.setCdFuncionario(usuario.getCdFuncionario());
                dao.get(func);
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(func.getCdPessoa());
                dao.get(pessoa);
                nome = pessoa.getDsPessoa();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao buscar usuário do cadastro.");
        }
        return "Usuário: " + cdUsercad + " - " + nome + "  Data Cadastro: " + Data.AmericaToBrasilSemHora(dtCadastro);
    }
    
    public static String getCadastroComHora(int cdUsercad, Date dtCadastro) {
        String nome = "<Nome não Encontrado>";
        try {
            Usuario usuario = new Usuario();
            Dao dao = new Dao();
            usuario.setCdUsuario(cdUsercad);
            dao.get(usuario);
            if (usuario.getCdFuncionario() == null) {
                nome = usuario.getDsLogin().toUpperCase();
            } else {
                Funcionario func = new Funcionario();
                func.setCdFuncionario(usuario.getCdFuncionario());
                dao.get(func);
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(func.getCdPessoa());
                dao.get(pessoa);
                nome = pessoa.getDsPessoa();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alerta.AlertaError("Erro!", "Erro ao buscar usuário do cadastro.");
        }
        return "Usuário: " + cdUsercad + " - " + nome + "  Data Cadastro: " + Data.AmericaToBrasil(dtCadastro);
    }

    public static String doubleToR$(Double valor) {
        DecimalFormat decFormat = new DecimalFormat("R$ 0.00");
        return decFormat.format(valor);
    }

    public static String doubleToReal(Double valor, int qtDecimal) {
        if (valor == null) {
            return "";
        }
        String formato = "";
        for (int i = 1; i <= qtDecimal; i++) {
            formato += "0";
        }
        DecimalFormat decFormat = new DecimalFormat("0." + formato);
        return decFormat.format(valor).replace(",", ".");
    }

    public static double RealToDouble(String real) {
        real = real.replace(",", ".");
        return Double.parseDouble(real);
    }

    public static String NumeroToCPF(String cpfNum) {
        return cpfNum.substring(0, 3) + "." + cpfNum.substring(3, 6) + "."
                + cpfNum.substring(6, 9) + "-" + cpfNum.substring(9, 11);
    }

    public static String NumeroToCNPJ(String cpfNum) {
        return cpfNum.substring(0, 2) + "." + cpfNum.substring(2, 5) + "."
                + cpfNum.substring(5, 8) + "/" + cpfNum.substring(8, 12) + "-" + cpfNum.substring(12, 14);
    }

    public static String NumeroToPIS(String cpfNum) {
        return cpfNum.substring(0, 3) + "." + cpfNum.substring(3, 8) + "."
                + cpfNum.substring(8, 10) + "-" + cpfNum.substring(10, 11);
    }

    public static String NumeroToRG(String cpfNum) {
        return cpfNum.substring(0, 2) + "." + cpfNum.substring(2, 5) + "."
                + cpfNum.substring(5, 8) + "-" + cpfNum.substring(8, 9);
    }

    public static String NumeroToCEP(String cpfNum) {
        return cpfNum.substring(0, 2) + "." + cpfNum.substring(2, 5) + "-"
                + cpfNum.substring(5, 8);
    }

    public static String NumeroToTelefone(String cpfNum) {
        return cpfNum.substring(0, 5) + "-" + cpfNum.substring(5, 9);
    }

    public static String RemoveMascara(String campo) {
        campo = campo.replace(".", "");
        campo = campo.replace(",", "");
        campo = campo.replace("/", "");
        campo = campo.replace("-", "");

        return campo;
    }

    public static boolean isCPF(String CPF) {
// considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
// converte o i-esimo caractere do CPF em um numero:
// por exemplo, transforma o caractere '0' no inteiro 0         
// (48 eh a posicao de '0' na tabela ASCII)         
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

// Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static boolean isCNPJ(String CNPJ) {
// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
                || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333")
                || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
                || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777")
                || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999")
                || (CNPJ.length() != 14)) {
            return (false);
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

// "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
// converte o i-ésimo caractere do CNPJ em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

// Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static Double arredondaDecimal(Double vlMovto, int i) {
        if (vlMovto == null){
            vlMovto = 0.00;
        }
        String valor = doubleToReal(vlMovto, i);
        return Double.parseDouble(valor);
    }
}
