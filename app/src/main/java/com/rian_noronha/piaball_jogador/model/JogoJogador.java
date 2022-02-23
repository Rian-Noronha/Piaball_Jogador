package com.rian_noronha.piaball_jogador.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.helper.DateCustom;

public class JogoJogador {

    private int qntAssistencia;
    private int qntGol;
    private int qntFinalizacao;
    private int qntDesarme;
    private int notaPeloJogo;
    private String data;
    private String chave;
    private String modalidade;
    private String nomeTimeRival;
    private String nomeSeuTime;
    private int golTimeRival;
    private int golSeuTime;
    private int totCartaoAmarelo;
    private int totVermelho;


    public JogoJogador(){

    }

    public void salvar(String dataEscolhida){
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);

        databaseReference.child("jogo_jogador")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public int getTotVermelho() {
        return totVermelho;
    }

    public void setTotVermelho(int totVermelho) {
        this.totVermelho = totVermelho;
    }

    public int getTotCartaoAmarelo() {
        return totCartaoAmarelo;
    }

    public void setTotCartaoAmarelo(int totCartaoAmarelo) {
        this.totCartaoAmarelo = totCartaoAmarelo;
    }

    public String getNomeTimeRival() {
        return nomeTimeRival;
    }

    public void setNomeTimeRival(String nomeTimeRival) {
        this.nomeTimeRival = nomeTimeRival;
    }

    public String getNomeSeuTime() {
        return nomeSeuTime;
    }

    public void setNomeSeuTime(String nomeSeuTime) {
        this.nomeSeuTime = nomeSeuTime;
    }

    public int getGolTimeRival() {
        return golTimeRival;
    }

    public void setGolTimeRival(int golTimeRival) {
        this.golTimeRival = golTimeRival;
    }

    public int getGolSeuTime() {
        return golSeuTime;
    }

    public void setGolSeuTime(int golSeuTime) {
        this.golSeuTime = golSeuTime;
    }

    public int getQntAssistencia() {
        return qntAssistencia;
    }

    public void setQntAssistencia(int qntAssistencia) {
        this.qntAssistencia = qntAssistencia;
    }

    public int getQntGol() {
        return qntGol;
    }

    public void setQntGol(int qntGol) {
        this.qntGol = qntGol;
    }

    public int getQntFinalizacao() {
        return qntFinalizacao;
    }

    public void setQntFinalizacao(int qntFinalizacao) {
        this.qntFinalizacao = qntFinalizacao;
    }

    public int getQntDesarme() {
        return qntDesarme;
    }

    public void setQntDesarme(int qntDesarme) {
        this.qntDesarme = qntDesarme;
    }

    public int getNotaPeloJogo() {
        return notaPeloJogo;
    }

    public void setNotaPeloJogo(int notaPeloJogo) {
        this.notaPeloJogo = notaPeloJogo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }
}
