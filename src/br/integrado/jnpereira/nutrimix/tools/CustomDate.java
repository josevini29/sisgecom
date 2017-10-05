package br.integrado.jnpereira.nutrimix.tools;

import java.text.SimpleDateFormat;

public class CustomDate extends java.sql.Date {
    
    public CustomDate(long date) {
        super(date);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this);
    }
}