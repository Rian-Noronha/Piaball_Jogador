package com.rian_noronha.piaball_jogador.dados_cadastrais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.model.Usuario;
import com.rian_noronha.piaball_jogador.nav_drawer.NavDrawerActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editEmail, editSenha;
    private Button btnLogar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        this.editEmail      = findViewById(R.id.editEmailLogin);
        this.editSenha      = findViewById(R.id.editSenhaLogin);
        this.btnLogar       = findViewById(R.id.btnLogar);

        this.btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = editEmail.getText().toString();
                String userSenha = editSenha.getText().toString();

                boolean camposValidados = validarCampos(userEmail, userSenha);

                if(camposValidados){
                    usuario = new Usuario();
                    usuario.setEmail(userEmail);
                    usuario.setSenha(userSenha);
                    autenticarUsuario();

                }

            }
        });

    }




    public void autenticarUsuario(){
        this.autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        this.autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                irNavDrawer();
                        }else{

                            String excecao = "";
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não cadastrado. Corra pra se cadastrar ligeiro!";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                excecao = "E-mail e senha botados não batem. Tente de novo!";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(
                                    getApplicationContext(),
                                    excecao,
                                    Toast.LENGTH_SHORT
                            ).show();

                        }
                    }
                });
    }

    public void irNavDrawer(){
        startActivity(new Intent(LoginActivity.this, NavDrawerActivity.class));
    }


    public boolean validarCampos(String userEmail, String userSenha){
        boolean camposValidados = false;

        if(!userEmail.isEmpty()){
            if(!userSenha.isEmpty()){
                camposValidados = true;
            }else{
                mostrarToast("Senha tá errada, se atente direitinho!");
            }
        }else{
                mostrarToast("E-mail tá errado, bote um certinho!");
        }

        return camposValidados;
    }

    public void mostrarToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}