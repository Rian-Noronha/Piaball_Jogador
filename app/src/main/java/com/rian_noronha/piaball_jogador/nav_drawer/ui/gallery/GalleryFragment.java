package com.rian_noronha.piaball_jogador.nav_drawer.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.rian_noronha.piaball_jogador.adapter.AdapterMinicampo;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoCalendar;
import com.rian_noronha.piaball_jogador.config.ConfiguracaoFirebase;
import com.rian_noronha.piaball_jogador.databinding.FragmentGalleryBinding;
import com.rian_noronha.piaball_jogador.helper.Base64Custom;
import com.rian_noronha.piaball_jogador.model.JogoJogador;
import com.rian_noronha.piaball_jogador.model.Usuario;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {
    private MaterialCalendarView calendarViewMinicampo;
    private RecyclerView recyclerViewMinicampo;
    private String mesAnoSelecionado = "";
    private ValueEventListener valueEventListenerJogosMinicampo;
    private ValueEventListener valueEventListenerUsuarios;
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference jogoRef;
    private Query jogosMinicampoRef;
    private DatabaseReference usuarioRef;
    private AdapterMinicampo adapterMinicampo;
    private List<JogoJogador> jogosMinicampo = new ArrayList<>();
    private JogoJogador jogoJogadorMinicampo;
    private TextView saudacaoMinicampo;

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        this.calendarViewMinicampo = root.findViewById(R.id.calendarViewMinicampo);
        this.recyclerViewMinicampo = root.findViewById(R.id.recyclerMinicampo);

        this.saudacaoMinicampo = root.findViewById(R.id.textSaudacaoMinicampo);

        adapterMinicampo = new AdapterMinicampo(jogosMinicampo, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewMinicampo.setLayoutManager(layoutManager);
        recyclerViewMinicampo.setHasFixedSize(true);
        recyclerViewMinicampo.setAdapter(adapterMinicampo);
        recyclerViewMinicampo.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        configurarCalendarViewMinicampo();

        swipeMinicampo();

        return root;
    }


    public void swipeMinicampo(){
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

                excluirJogoMinicampo(viewHolder);

            }
        };

        new ItemTouchHelper((itemTouch)).attachToRecyclerView(recyclerViewMinicampo);

    }

    private void excluirJogoMinicampo(final RecyclerView.ViewHolder viewHolder) {

        //Botar um alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        //1. título
        alertDialog.setTitle("Excluir o jogo");
        //2. mensagem
        alertDialog.setMessage("Quer mesmo excluir o jogo?");
        //3. tirar o cancelamento
        alertDialog.setCancelable(false);

        //configurar lado positivo
        alertDialog.setPositiveButton("Quero", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();

                jogoJogadorMinicampo = jogosMinicampo.get(position);
                String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                jogoRef = databaseReference.child("jogo_jogador")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                jogoRef.child(jogoJogadorMinicampo.getChave()).removeValue();

                adapterMinicampo.notifyItemRemoved(position);

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
                adapterMinicampo.notifyDataSetChanged();
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


    public void ativarInteracao(){
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(userEmail);

        usuarioRef = databaseReference.child("usuarios")
                .child(idUsuario);

        valueEventListenerUsuarios = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                saudacaoMinicampo.setText("E aí, " + (usuario.getNome().toUpperCase()) + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void recuperarJogosMinicampo(){
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(userEmail);

        jogoRef = databaseReference.child("jogo_jogador")
                .child(idUsuario)
                .child(mesAnoSelecionado);
        jogosMinicampoRef = jogoRef.orderByChild("modalidade")
                .equalTo("minicampo");
        valueEventListenerJogosMinicampo = jogosMinicampoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jogosMinicampo.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                   JogoJogador jogoJogador = dados.getValue(JogoJogador.class);
                   jogoJogador.setChave(dados.getKey());
                   jogosMinicampo.add(jogoJogador);

                }

                adapterMinicampo.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void configurarCalendarViewMinicampo(){
        this.calendarViewMinicampo.setTitleMonths(ConfiguracaoCalendar.pegarMesesAno());

        CalendarDay dataAtual = this.calendarViewMinicampo.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
        mesAnoSelecionado = (mesSelecionado + "" + (dataAtual.getYear()));

        this.calendarViewMinicampo.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = (mesSelecionado + "" + (date.getYear()));

                //terá retorno nulo
                //jogosMinicampoRef.removeEventListener(valueEventListenerJogosMinicampo);
                recuperarJogosMinicampo();
            }
        });


    }


    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuarios);
        jogosMinicampoRef.removeEventListener(valueEventListenerJogosMinicampo);
    }

    @Override
    public void onStart() {
        super.onStart();
        ativarInteracao();
        recuperarJogosMinicampo();
    }
}