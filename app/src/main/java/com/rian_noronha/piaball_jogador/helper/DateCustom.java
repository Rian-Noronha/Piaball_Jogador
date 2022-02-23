package com.rian_noronha.piaball_jogador.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustom {

    public static String pegarDataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data){
        String[] retornoData = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;
        return mesAno;
    }

    public static String pegarHoraAtual(){
        /*Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);*/
        Date dataAtual = new Date();
        String hora = new SimpleDateFormat(("HH:mm")).format(dataAtual);

        return hora;

    }



}
