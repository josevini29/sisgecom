package br.integrado.jnpereira.nutrimix.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;

public class Data {

    public static Date getAgora() {
        Date agora = new Date();
        return agora;
    }
    
    public static Date getAgora2() {
        Date agora = new Date();
        String data = AmericaToBrasilSemHora(agora);
        try {
            return StringToDate(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void autoComplete(TextField campo) {
        if (campo.getText().length() == 10) {
            return;
        }
        String data = AmericaToBrasilSemHora(getAgora());
        if (campo.getText().length() == 2) {
            campo.setText(campo.getText() + data.substring(2, 10));
        }
        if (campo.getText().length() == 5) {
            campo.setText(campo.getText() + data.substring(5, 10));
        }
    }

    public static Date StringToDate(String data) throws Exception {
        if (data == null) {
            return null;
        } else {
            if (data.equals("")) {
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(data);
        } catch (ParseException ex) {
            throw new Exception("Data inválida!");
        }
    }

    public static String BrasilToAmerica(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(data);
    }

    public static String BrasilToAmericaSemHora(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(data);
    }

    public static String AmericaToBrasilSemHora(Date data) {
        if (data != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(data);
        } else {
            return "";
        }
    }

    public static String AmericaToBrasil(Date data) {
        if (data != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return df.format(data);
        } else {
            return "";
        }
    }

    public static String getDiaSemana(int dia) {
        switch (dia) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda-Feira";
            case 3:
                return "Terça-Feira";
            case 4:
                return "Quarta-Feira";
            case 5:
                return "Quinta-Feira";
            case 6:
                return "Sexta-Feira";
            case 7:
                return "Sábado-Feira";
            default:
                return "Não definido";
        }
    }

}
