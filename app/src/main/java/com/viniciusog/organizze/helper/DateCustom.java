package com.viniciusog.organizze.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustom {

    public static String dataAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date()).toString();
    }

    public static String mesAnoDataEscolhida(String data) {
        String retornoData[] = data.split("/");
        String mes = retornoData[1];
        String ano = retornoData[2];
        String mesAno = mes + ano;
        return mesAno;
    }
}