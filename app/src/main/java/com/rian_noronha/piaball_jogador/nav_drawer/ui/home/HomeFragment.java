package com.rian_noronha.piaball_jogador.nav_drawer.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.rian_noronha.piaball_jogador.adapter.AdapterCampoGrande;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoCalendar;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.databinding.FragmentHomeBinding;
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;
import com.rian_noronha.piaball_jogador.model.Usuario;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private MaterialCalendarView calendarViewCampoGrande;
    private RecyclerView recyclerViewCampoGrande;
    private String mesAnoSelecionado = "";
    private ValueEventListener valueEventListenerJogosCampoGrande;
    private ValueEventListener valueEventListenerUsuarios;
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference jogoRef;
    private Query jogosCampoGrandeRef;
    private DatabaseReference usuarioRef;
    private AdapterCampoGrande adapterCampoGrande;
    private List<JogoJogador> jogosCampoGrande = new ArrayList<>();
    private JogoJogador jogoJogadorCampoGrande;
    private TextView saudacaoCampoGrande;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        this.calendarViewCampoGrande = root.findViewById(R.id.calendarViewCampoGrande);
        this.recyclerViewCampoGrande = root.findViewById(R.id.recyclerCampoGrande);

        this.saudacaoCampoGrande = root.findViewById(R.id.textSaudacaoCampoGrande);

        adapterCampoGrande = new AdapterCampoGrande(jogosCampoGrande, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewCampoGrande.setLayoutManager(layoutManager);
        recyclerViewCampoGrande.setHasFixedSize(true);
        recyclerViewCampoGrande.setAdapter(adapterCampoGrande);
        recyclerViewCampoGrande.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        configurarCalendarViewCampoGrande();
        swipeCampoGrande();
        return root;


    }

    public void swipeCampoGrande(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;

                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirJogoCampoGrande(viewHolder);

            }
        };

        new ItemTouchHelper((itemTouch)).attachToRecyclerView(recyclerViewCampoGrande);

    }

    private void excluirJogoCampoGrande(final RecyclerView.ViewHolder viewHolder) {

        //Botar um alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        //1. título
        alertDialog.setTitle("Excluir o jogo");
        //2. mensagem
        alertDialog.setMessage("Quer mesmo excluir o jogo?");
        //3. tirar o cancelamento
        alertDialog.setCancelable(false);

        //lado positivo
        alertDialog.setPositiveButton("Quero", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();

                jogoJogadorCampoGrande = jogosCampoGrande.get(position);
                String userEmail = firebaseAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(userEmail);

                jogoRef = databaseReference.child("jogo_jogador")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                jogoRef.child(jogoJogadorCampoGrande.getChave()).removeValue();

                adapterCampoGrande.notifyItemRemoved(position);
            }
        });

        //configurar lado negativo

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        getContext(),
                        "Tá beleza!",
                        Toast.LENGTH_SHORT
                ).show();

                //avisar o adapter para o item voltar
                adapterCampoGrande.notifyDataSetChanged();
            }
        });

        //criar o alert d.
        AlertDialog alert = alertDialog.create();
        //mostrar o alert d.
        alert.show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //apenas mostra o nome do usuário na tela dos seus jogos
    public void ativarInteracao(){
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(userEmail);

        usuarioRef = databaseReference.child("usuarios")
                .child(idUsuario);
        valueEventListenerUsuarios = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                saudacaoCampoGrande.setText("E aí, " + (usuario.getNome().toUpperCase()) + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void recuperarJogosCampoGrande(){

        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(userEmail);

        jogoRef = databaseReference.child("jogo_jogador")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        jogosCampoGrandeRef = jogoRef.orderByChild("modalidade")
                .equalTo("campo_grande");
        valueEventListenerJogosCampoGrande = jogosCampoGrandeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jogosCampoGrande.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    JogoJogador jogoJogador = dados.getValue(JogoJogador.class);
                    jogoJogador.setChave(dados.getKey());
                    jogosCampoGrande.add(jogoJogador);
                }

                adapterCampoGrande.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void configurarCalendarViewCampoGrande(){
        this.calendarViewCampoGrande.setTitleMonths(ConfiguracaoCalendar.pegarMesesAno());

        CalendarDay dataAtual = this.calendarViewCampoGrande.getCurrentDate();

        //o format coloca um 0 à esquerda do mês para bater com mes-ano do firebase.
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
        this.mesAnoSelecionado = (mesSelecionado + "" + (dataAtual.getYear()));

        this.calendarViewCampoGrande.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = (mesSelecionado + "" + (date.getYear()));

                Log.i("MesAno", mesAnoSelecionado);

                jogosCampoGrandeRef.removeEventListener(valueEventListenerJogosCampoGrande);
                recuperarJogosCampoGrande();

            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        //parar processos quando o aplicação for interrompida
        jogosCampoGrandeRef.removeEventListener(valueEventListenerJogosCampoGrande);
        usuarioRef.removeEventListener(valueEventListenerUsuarios);
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarJogosCampoGrande();
        ativarInteracao();
    }
}