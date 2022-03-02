package com.rian_noronha.piaball_jogador.add_jogos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.helper.DateCustom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;

public class AddJogoActivity extends AppCompatActivity {

    private TextView textGol, textDesarme, textAssistencia, textFinalizacao, textGolTimeFora, textGolTimeCasa, textNota;
    private EditText editData,editRival, editTimeCasa;
    private SeekBar seekBarGol, seekBarDesarme, seekBarAssistencia, seekBarFinalizacao, seekBarGolTimeFora, seekBarGolTimeCasa, seekBarNota;
    private RadioGroup radioGroupAddAmarelo, radioGroupModalidade;
    private RadioButton radioAddUmAmarelo, radioAddDoisAmarelos, radioAddVermelho, radioMinicampo, radioCampoGrande;


    private Integer totGol, totDesarme, totAssistencia, totFinalizacao, totGolTimeFora, totGolTimeCasa, totNota;


    private Button btnCadastrarJogo;
    private JogoJogador jogador;
    private boolean radioModalidadeValidado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jogo);

        getSupportActionBar().setTitle("Bote seu jogo");

        textGol             = findViewById(R.id.textQntGols);
        textDesarme         = findViewById(R.id.textQntDesarmes);
        textAssistencia     = findViewById(R.id.textQntAssistencias);
        textFinalizacao     = findViewById(R.id.textQntFinalizacoes);
        textGolTimeCasa     = findViewById(R.id.textGolsTimeCasa);
        textGolTimeFora     = findViewById(R.id.textGolsRival);
        textNota            = findViewById(R.id.textNotaJogo);

        editData        = findViewById(R.id.editDataJogo);
        editRival       = findViewById(R.id.editRival);
        editTimeCasa    = findViewById(R.id.editAddTimeCasa);


        seekBarGol          = findViewById(R.id.seekBarQntGols);
        seekBarDesarme      = findViewById(R.id.seekBarQntDesarmes);
        seekBarAssistencia  = findViewById(R.id.seekBarQntAssistencias);
        seekBarFinalizacao  = findViewById(R.id.seekBarQntFinalizacoes);
        seekBarGolTimeCasa  = findViewById(R.id.seekBarQntGolsTimeCasa);
        seekBarGolTimeFora  = findViewById(R.id.seekBarQntGolsRival);
        seekBarNota         = findViewById(R.id.seekBarNotaJogo);


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


        //pegar os eventos de mudança de cada seekBar
        ouvirGols();
        ouvirDesarmes();
        ouvirAssistencia();
        ouvirFinalizacao();
        ouvirGolsTimeCasa();
        ouvirGolsRival();
        ouvirNotaJogo();



        this.btnCadastrarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data         = editData.getText().toString();
                String timeCasa     = editTimeCasa.getText().toString();
                String rival        = editRival.getText().toString();


                boolean camposValidados = validarCampos(data, timeCasa, rival);

                if((radioMinicampo.isChecked() != false) || (radioCampoGrande.isChecked() != false)){
                    radioModalidadeValidado = true;
                }

                if(camposValidados){

                    if(radioModalidadeValidado){
                        //salvando o jogo do jogador de campo grande
                        jogador = new JogoJogador();

                        jogador.setData(data);
                        jogador.setQntAssistencia(totAssistencia);
                        jogador.setQntDesarme(totDesarme);
                        jogador.setQntFinalizacao(totFinalizacao);
                        jogador.setGolSeuTime(totGolTimeCasa);
                        jogador.setGolTimeRival(totGolTimeFora);
                        jogador.setQntGol(totGol);
                        jogador.setNotaPeloJogo(totNota);
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
                    }else{
                        mostrarToast("Bote a modalidade!");
                    }

                }


            }
        });


    }


    public void ouvirAssistencia(){
        totAssistencia = 0;
        seekBarAssistencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totAssistencia = progress;
                textAssistencia.setText("Botando " + totAssistencia + " assistência(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirGols(){
        totGol = 0;
        seekBarGol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totGol = progress;
                textGol.setText("Botando " + totGol + " gol(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirDesarmes(){
        totDesarme = 0;
        seekBarDesarme.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totDesarme = progress;
                textDesarme.setText("Botando " + totDesarme + " desarme(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirFinalizacao(){
        totFinalizacao = 0;
        seekBarFinalizacao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totFinalizacao = progress;
                textFinalizacao.setText("Botando " + totFinalizacao + " finalizão(ões)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirGolsTimeCasa(){
        totGolTimeCasa = 0;
        seekBarGolTimeCasa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totGolTimeCasa = progress;
                textGolTimeCasa.setText("Botando " + totGolTimeCasa + " gol(s) para o time da casa");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirGolsRival(){

        totGolTimeFora = 0;
        seekBarGolTimeFora.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totGolTimeFora = progress;
                textGolTimeFora.setText("Botando " + totGolTimeFora + " gol(s) para o time de fora");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void ouvirNotaJogo(){
        totNota = 0;
        seekBarNota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                totNota = progress;
                textNota.setText("Botando " + totNota + " para sua nota pelo jogo");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    public boolean validarCampos(String data, String timeCasa, String rival){

        boolean camposValidados = false;

        if(!data.isEmpty()){
            if(!timeCasa.isEmpty()){
                if(!rival.isEmpty()){
                    camposValidados = true;
                }else{
                    mostrarToast("Bote o time de fora*");
                }
            }else{
                mostrarToast("Bote o seu time*");
            }
        }else{
            mostrarToast("Bote a data*");
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