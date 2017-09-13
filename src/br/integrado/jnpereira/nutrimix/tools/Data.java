package br.integrado.jnpereira.nutrimix.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TextField;

public class Data {

    public static Date getAgora() {
        Date agora = new Date();
        return agora;
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(data);
    }

}
