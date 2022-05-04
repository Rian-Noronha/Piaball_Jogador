package com.rian_noronha.piaball_jogador.nav_drawer.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoCalendar;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.databinding.FragmentSlideshowBinding;
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;
import com.rian_noronha.piaball_jogador.model.Usuario;

import java.text.DecimalFormat;


public class SlideshowFragment extends Fragment {

    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerUsuarios;
    private DatabaseReference jogosJogadorRef;
    private Query jogosCampoGrandeRef;
    private ValueEventListener valueEventListenerJogosCampoGrande;

    private ValueEventListener valueEventListenerJogosJogador;

    private MaterialCalendarView calendarViewMediasCampoGrande;
    private String mesAnoSelecionado = "";

    private TextView textSaudacaoMediasCampoGrande, textMediaGols, textMediaAssistencias, textDesarmes, textMediaFinalizacoes,
            textMediaCartaoAmarelo, textMediaCartaoVermelho;
    private double mediaGols, mediaFinalizacoes, mediaDesarmes, mediaAssistencias, mediaCartoesAmarelos, mediaCartoesVermelhos;
    private double somaGols, somaFinalizacoes, somaDesarmes, somaAssistencias, somaCartoesAmarelos, somaCartoesVermelhos;
    private double totJogos = 0;


    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        //binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        this.textSaudacaoMediasCampoGrande = root.findViewById(R.id.textSaudacaoMediasCampoGrande);

        this.textMediaGols              = root.findViewById(R.id.textMediaGolsCampoGrande);
        this.textMediaAssistencias      = root.findViewById(R.id.textMediaAssistenciasCampoGrande);
        this.textDesarmes               = root.findViewById(R.id.textMediaDesarmesCampoGrande);
        this.textMediaFinalizacoes      = root.findViewById(R.id.textMediaFinalizacoesCampoGrande);
        this.textMediaCartaoAmarelo     = root.findViewById(R.id.textMediaCartaoAmareloCampoGrande);
        this.textMediaCartaoVermelho    = root.findViewById(R.id.textMediaCartaoVermelhoCampoGrande);

        this.calendarViewMediasCampoGrande = root.findViewById(R.id.calendarViewMediasCampoGrande);
        configurarCalendarViewMediasJogadorCampoGrande();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void saudarUsuario(){
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        usuariosRef = databaseReference.child("usuarios")
                .child(idUsuario);
        valueEventListenerUsuarios = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                textSaudacaoMediasCampoGrande.setText("Iae, " + (usuario.getNome()).toUpperCase() + ":)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void mostrarMedias() {

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        //chegar nos nós de jogo_jogador
        jogosJogadorRef = databaseReference.child("jogo_jogador")
                .child(idUsuario)
                .child(mesAnoSelecionado);
        //usar uma query para pegar apenas os jogos de campo grande
        jogosCampoGrandeRef = jogosJogadorRef.orderByChild("modalidade")
                .equalTo("campo_grande");

        valueEventListenerJogosCampoGrande = jogosCampoGrandeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){

                    Log.i("Dados:", dados.toString());

                    JogoJogador jogoJogador = dados.getValue(JogoJogador.class);

                    totJogos++;

                    somaGols += jogoJogador.getQntGol();
                    somaAssistencias += jogoJogador.getQntAssistencia();
                    somaDesarmes += jogoJogador.getQntDesarme();
                    somaCartoesAmarelos += jogoJogador.getTotCartaoAmarelo();
                    somaCartoesVermelhos += jogoJogador.getTotVermelho();
                    somaFinalizacoes += jogoJogador.getQntFinalizacao();


                }

                mediaGols = (somaGols/totJogos);
                mediaAssistencias = (somaAssistencias/totJogos);
                mediaDesarmes = (somaDesarmes/totJogos);
                mediaCartoesAmarelos = (somaCartoesAmarelos/4);
                mediaCartoesVermelhos = (somaCartoesVermelhos/4);
                mediaFinalizacoes = (somaFinalizacoes/totJogos);

                DecimalFormat decimalFormat     = new DecimalFormat("0.##");
                String mediaGolsTxt                = decimalFormat.format(mediaGols);
                String mediaAssistenciasTxt        = decimalFormat.format((mediaAssistencias));
                String mediaDesarmesTxt            = decimalFormat.format((mediaDesarmes));
                String mediaCartoesAmarelosTxt     = decimalFormat.format((mediaCartoesAmarelos));
                String mediaCartoesVermelhosTxt    = decimalFormat.format((mediaCartoesVermelhos));
                String mediaFinalizacoesTxt        = decimalFormat.format((mediaFinalizacoes));


                textMediaGols.setText(mediaGolsTxt + " por partida");
                textMediaAssistencias.setText(mediaAssistenciasTxt + " por partida");
                textDesarmes.setText(mediaDesarmesTxt + " por partida");
                textMediaFinalizacoes.setText(mediaFinalizacoesTxt + " por partida");
                textMediaCartaoAmarelo.setText(mediaCartoesAmarelosTxt + " por mês");
                textMediaCartaoVermelho.setText(mediaCartoesVermelhosTxt + " por mês");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void configurarCalendarViewMediasJogadorCampoGrande(){
        this.calendarViewMediasCampoGrande.setTitleMonths(ConfiguracaoCalendar.pegarMesesAno());

        CalendarDay dataAtual = this.calendarViewMediasCampoGrande.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
        this.mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        this.calendarViewMediasCampoGrande.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                zerarValores();

                //terá retorno nulo
                //jogosCampoGrandeRef.removeEventListener(valueEventListenerJogosCampoGrande);
                mostrarMedias();
            }
        });


    }

    public void zerarValores(){
        totJogos = 0;
        somaGols = somaAssistencias = somaDesarmes = somaCartoesVermelhos = somaCartoesAmarelos = somaFinalizacoes = 0;
    }


    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerUsuarios);
       jogosCampoGrandeRef.removeEventListener(valueEventListenerJogosCampoGrande);
    }

    @Override
    public void onStart() {
        super.onStart();
        saudarUsuario();
        mostrarMedias();
    }
}