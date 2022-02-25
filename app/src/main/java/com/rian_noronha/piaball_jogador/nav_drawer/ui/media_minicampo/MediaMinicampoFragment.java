package com.rian_noronha.piaball_jogador.nav_drawer.ui.media_minicampo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;
import com.rian_noronha.piaball_jogador.model.Usuario;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaMinicampoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaMinicampoFragment extends Fragment {

    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerUsuarios;
    private DatabaseReference jogosJogadorRef;
    private Query jogosMinicampoRef;
    private ValueEventListener valueEventListenerJogoMinicampo;

    private ValueEventListener valueEventListenerJogosMinicampo;

    private MaterialCalendarView calendarViewMediasMinicampo;
    private String mesAnoSelecionado = "";

    private TextView textSaudacaoMediasMinicampo, textMediaGols, textMediaAssistencias, textDesarmes, textMediaFinalizacoes,
            textMediaCartaoAmarelo, textMediaCartaoVermelho;
    private double mediaGols, mediaFinalizacoes, mediaDesarmes, mediaAssistencias, mediaCartoesAmarelos, mediaCartoesVermelhos;
    private double somaGols, somaFinalizacoes, somaDesarmes, somaAssistencias, somaCartoesAmarelos, somaCartoesVermelhos;
    private double totJogos = 0;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MediaMinicampoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaMinicampoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaMinicampoFragment newInstance(String param1, String param2) {
        MediaMinicampoFragment fragment = new MediaMinicampoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_media_minicampo, container, false);


        this.textSaudacaoMediasMinicampo = view.findViewById(R.id.textSaudacaoMediasMinicampo);

        this.textMediaGols              = view.findViewById(R.id.textMediaGolsMinicampo);
        this.textMediaAssistencias      = view.findViewById(R.id.textMediaAssistenciasMinicampo);
        this.textDesarmes               = view.findViewById(R.id.textMediaDesarmesMinicampo);
        this.textMediaFinalizacoes      = view.findViewById(R.id.textMediaFinalizacoesMinicampo);
        this.textMediaCartaoAmarelo     = view.findViewById(R.id.textMediaCartaoAmareloMinicampo);
        this.textMediaCartaoVermelho    = view.findViewById(R.id.textMediaCartaoVermelhoMinicampo);

        this.calendarViewMediasMinicampo = view.findViewById(R.id.calendarViewMediasMinicampo);
        configurarCalendarViewMediasJogadorMinicampo();


        return view;

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
                textSaudacaoMediasMinicampo.setText("Iae, " + (usuario.getNome()).toUpperCase() + ":)");
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
        jogosMinicampoRef = jogosJogadorRef.orderByChild("modalidade")
                .equalTo("minicampo");
        valueEventListenerJogosMinicampo = jogosMinicampoRef.addValueEventListener(new ValueEventListener() {
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


    public void configurarCalendarViewMediasJogadorMinicampo(){
        this.calendarViewMediasMinicampo.setTitleMonths(ConfiguracaoCalendar.pegarMesesAno());

        CalendarDay dataAtual = this.calendarViewMediasMinicampo.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
        this.mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        this.calendarViewMediasMinicampo.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                zerarValores();

                jogosMinicampoRef.removeEventListener(valueEventListenerJogosMinicampo);
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
        jogosMinicampoRef.removeEventListener(valueEventListenerJogosMinicampo);
    }

    @Override
    public void onStart() {
        super.onStart();
        saudarUsuario();
        mostrarMedias();
    }
}