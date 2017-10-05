package br.integrado.jnpereira.nutrimix.tools;

import java.text.SimpleDateFormat;

public class CustomDateNoTime extends java.sql.Date {
    
    public CustomDateNoTime(long date) {
        super(date);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(this);
    }
}