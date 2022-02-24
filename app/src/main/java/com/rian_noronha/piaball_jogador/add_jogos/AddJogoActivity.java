package com.rian_noronha.piaball_jogador.add_jogos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.helper.DateCustom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;

public class AddJogoActivity extends AppCompatActivity {

    private EditText editData, editGol, editDesarme, editAssistencia, editFinalizacao, editRival, editTimeCasa, editGolTimeFora, editGolTimeCasa, editNota;
    private RadioGroup radioGroupAddAmarelo, radioGroupModalidade;
    private RadioButton radioAddUmAmarelo, radioAddDoisAmarelos, radioAddVermelho, radioMinicampo, radioCampoGrande;

    private Button btnCadastrarJogo;
    private JogoJogador jogador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jogo);

        this.editData           = findViewById(R.id.editAddData);
        this.editGol            = findViewById(R.id.editAddGol);
        this.editAssistencia    = findViewById(R.id.editAddAssistencia);
        this.editDesarme        = findViewById(R.id.editAddDesarme);
        this.editFinalizacao    = findViewById(R.id.editAddFinalizacao);
        this.editGolTimeCasa    = findViewById(R.id.editAddGolTimeCasa);
        this.editGolTimeFora    = findViewById(R.id.editAddGolTimeFora);
        this.editNota           = findViewById(R.id.editAddNota);
        this.editRival          = findViewById(R.id.editAddRival);
        this.editTimeCasa       = findViewById(R.id.editAddTimeCasa);

        //um e dois referem-se aos cartões amarelo que o jogador possa tomar na partida
        this.radioGroupAddAmarelo   = findViewById(R.id.radioGroupAddAmarelo);
        this.radioAddUmAmarelo      = findViewById(R.id.radioAddUm);
        this.radioAddDoisAmarelos   = findViewById(R.id.radioAddDois);
        this.radioAddVermelho       = findViewById(R.id.radioAddVermelho);

        this.radioGroupModalidade   = findViewById(R.id.radioGroupModalidade);
        this.radioMinicampo         = findViewById(R.id.radioMinicampo);
        this.radioCampoGrande       = findViewById(R.id.radioCampoGrande);

        this.btnCadastrarJogo       = findViewById(R.id.btnCadastrarJogo);

        //mostrar data atual de cara para o jogador
        this.editData.setText(DateCustom.pegarDataAtual());

        this.btnCadastrarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gol          = editGol.getText().toString();
                String data         = editData.getText().toString();
                String assistencia  = editAssistencia.getText().toString();
                String desarme      = editDesarme.getText().toString();
                String finalizacao  = editFinalizacao.getText().toString();
                String golTimeCasa  = editGolTimeCasa.getText().toString();
                String golTimeFora  = editGolTimeFora.getText().toString();
                String notaJogador  = editNota.getText().toString();
                String timeCasa     = editTimeCasa.getText().toString();
                String rival        = editRival.getText().toString();

                boolean camposValidados = validarCampos(data, gol, assistencia, desarme, finalizacao, golTimeCasa, golTimeFora, notaJogador, timeCasa, rival);

                if(camposValidados){
                    //salvando o jogo do jogador de campo grande
                    jogador = new JogoJogador();

                    jogador.setData(data);
                    jogador.setQntAssistencia(Integer.parseInt(assistencia));
                    jogador.setQntDesarme(Integer.parseInt(desarme));
                    jogador.setQntFinalizacao(Integer.parseInt(finalizacao));
                    jogador.setGolSeuTime(Integer.parseInt(golTimeCasa));
                    jogador.setGolTimeRival(Integer.parseInt(golTimeFora));
                    jogador.setQntGol(Integer.parseInt(gol));
                    jogador.setNotaPeloJogo(Integer.parseInt(notaJogador));
                    jogador.setNomeSeuTime(timeCasa);
                    jogador.setNomeTimeRival(rival);
                    if(radioMinicampo.isChecked()){
                        jogador.setModalidade("minicampo");
                    }else if(radioCampoGrande.isChecked()){
                        jogador.setModalidade("campo_grande");
                    }

                    if(radioAddVermelho.isChecked()){
                        jogador.setTotVermelho(1);
                    }

                    if(radioAddUmAmarelo.isChecked()){
                        jogador.setTotCartaoAmarelo(1);
                    }else if(radioAddDoisAmarelos.isChecked()){
                        jogador.setTotCartaoAmarelo(2);
                    }

                    jogador.salvar(data);
                    finish();

                }


            }
        });


    }





    public boolean validarCampos(String data, String gol, String assistencia, String desarme, String finalizacao, String golTimeCasa, String golTimeFora, String nota, String timeCasa, String rival){

        boolean camposValidados = false;

        if(!data.isEmpty()){
            if(!gol.isEmpty()){
                if(!assistencia.isEmpty()){
                    if(!desarme.isEmpty()){
                        if(!finalizacao.isEmpty()){
                            if(!golTimeCasa.isEmpty()){
                                if(!golTimeFora.isEmpty()){
                                    if(!nota.isEmpty()){
                                        if(!timeCasa.isEmpty()){
                                            if(!rival.isEmpty()){
                                                camposValidados = true;
                                            }else{
                                                mostrarToast("Entre com o nome do time rival!");
                                            }
                                        }else{
                                            mostrarToast("Entre com o nome do seu time!");
                                        }
                                    }else{
                                        mostrarToast("Entre com sua nota!");
                                    }
                                }else{
                                    mostrarToast("Entre com os gols do time rival!");
                                }
                            }else{
                                mostrarToast("Entre com os gols do seu time!");
                            }
                        }else{
                            mostrarToast("Entre com suas finalizações!");
                        }
                    }else{
                        mostrarToast("Entre com seus desarmes!");
                    }
                }else{
                    mostrarToast("Entre com suas assistências!");
                }
            }else{
                mostrarToast("Entre com seus gols!");
            }
        }else{
            mostrarToast("Entre com a data!");
        }

        return camposValidados;

    }
    public void mostrarToast(String msg){
        Toast.makeText(
                getApplicationContext(),
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }


}