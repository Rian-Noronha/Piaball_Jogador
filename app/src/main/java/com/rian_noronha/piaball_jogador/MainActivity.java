package com.rian_noronha.piaball_jogador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.dados_cadastrais.CadastroActivity;
import com.rian_noronha.piaball_jogador.dados_cadastrais.LoginActivity;
import com.rian_noronha.piaball_jogador.nav_drawer.NavDrawerActivity;

public class MainActivity extends IntroActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()

                .background(R.color.white)
                .fragment(R.layout.intro1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro2)
                .build());

        addSlide(new FragmentSlide.Builder()

                .background(R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .canGoBackward(true)
                .build());

    }

    public void irNavDrawer(){
        startActivity(new Intent(MainActivity.this, NavDrawerActivity.class));
    }

    public void verificarUsuarioLogado(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        if(firebaseAuth.getCurrentUser() != null){//se o usuário existir
            irNavDrawer();
        }


    }

    public void irSeCadastrar(View view){
        startActivity(new Intent(MainActivity.this, CadastroActivity.class));
    }

    public void irParaTelaLogin(View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }
}