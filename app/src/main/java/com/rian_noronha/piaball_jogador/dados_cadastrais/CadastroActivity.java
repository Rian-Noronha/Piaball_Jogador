package com.rian_noronha.piaball_jogador.dados_cadastrais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private EditText editNome, editEmail, editSenha;
    private Button btnCadastrar;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().hide();

        this.editNome       = findViewById(R.id.editNomeCadastro);
        this.editEmail      = findViewById(R.id.editEmailCadastro);
        this.editSenha      = findViewById(R.id.editSenhaCadastro);
        this.btnCadastrar   = findViewById(R.id.btnCadastrar);

        this.btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNome = editNome.getText().toString();
                String userEmail = editEmail.getText().toString();
                String userSenha = editSenha.getText().toString();

                boolean camposValidados = validarCampos(userNome, userEmail, userSenha);

                if(camposValidados){
                    usuario = new Usuario();
                    usuario.setNome(userNome);
                    usuario.setEmail(userEmail);
                    usuario.setSenha(userSenha);
                    cadastrarUsuario();
                }

            }
        });


    }


    public void cadastrarUsuario(){
        this.firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        this.firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setIdUsuario(idUsuario);
                            usuario.salvar();
                            finish();
                        }else{
                            String excecao = "";

                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                excecao = "Senha tá fraquinha, tente outra!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Bote um e-mail válido, certinho!";
                            }catch(FirebaseAuthUserCollisionException e){
                                excecao = "Conta já foi botada, se atente direitinho!";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
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


    public boolean validarCampos(String userNome, String userEmail, String userSenha){
        boolean camposValidados = false;

        if(!userNome.isEmpty()){
            if(!userEmail.isEmpty()){
                if(!userSenha.isEmpty()){
                    camposValidados = true;
                }else{
                    mostrarToast("Tá faltando sua SENHA, meu chapa!");
                }
            }else{
                mostrarToast("Tá faltando seu E-MAIL, meu chapa!");
            }
        }else{
            mostrarToast("Tá faltando seu NOME, meu chapa!");
        }

        return camposValidados;

    }


    public void mostrarToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }




}