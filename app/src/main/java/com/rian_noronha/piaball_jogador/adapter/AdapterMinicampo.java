package com.rian_noronha.piaball_jogador.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rian_noronha.piaball_jogador.R;
import com.rian_noronha.piaball_jogador.model.JogoJogador;

import java.util.ArrayList;
import java.util.List;

public class AdapterMinicampo extends RecyclerView.Adapter<AdapterMinicampo.MyViewHolder> {

    private List<JogoJogador> jogosJogadorMinicampo = new ArrayList<>();
    private Context context;

    public AdapterMinicampo(List<JogoJogador> jogosJogadorMinicampo, Context context) {
        this.jogosJogadorMinicampo = jogosJogadorMinicampo;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_jogos_minicampo, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JogoJogador jogoJogador = this.jogosJogadorMinicampo.get(position);
        holder.textData.setText(jogoJogador.getData());
        holder.textGol.setText(String.valueOf(jogoJogador.getQntGol()));
        holder.textAssistencia.setText(String.valueOf(jogoJogador.getQntAssistencia()));
        holder.textDesarme.setText(String.valueOf(jogoJogador.getQntDesarme()));
        holder.textFinalizacao.setText(String.valueOf(jogoJogador.getQntFinalizacao()));
        holder.textRival.setText(String.valueOf(jogoJogador.getNomeTimeRival().toUpperCase()));
        holder.textTimeCasa.setText(String.valueOf(jogoJogador.getNomeSeuTime().toUpperCase()));
        holder.textNota.setText(String.valueOf(jogoJogador.getNotaPeloJogo()));
        holder.textVermelho.setText(String.valueOf(jogoJogador.getTotVermelho()));
        holder.textAmarelo.setText(String.valueOf(jogoJogador.getTotCartaoAmarelo()));

        if(jogoJogador.getGolSeuTime() > jogoJogador.getGolTimeRival()){
            holder.textGolTimeCasa.setTextColor(context.getResources().getColor(R.color.verde_fraco));
        }

        holder.textGolTimeCasa.setText(String.valueOf(jogoJogador.getGolSeuTime()));

        holder.textGolRival.setText(String.valueOf(jogoJogador.getGolTimeRival()));

    }

    @Override
    public int getItemCount() {
        return jogosJogadorMinicampo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textData, textGol, textAssistencia, textDesarme, textFinalizacao,
                textAmarelo, textVermelho, textRival, textTimeCasa, textGolRival,
                textGolTimeCasa, textNota;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textData = itemView.findViewById(R.id.textDataMinicampo);
            textGol = itemView.findViewById(R.id.textGolMinicampo);
            textAssistencia = itemView.findViewById(R.id.textAssistenciaMinicampo);
            textDesarme = itemView.findViewById(R.id.textDesarmeMinicampo);
            textFinalizacao = itemView.findViewById(R.id.textFinalizacaoMinicampo);
            textAmarelo = itemView.findViewById(R.id.textCartaoAmareloMinicampo);
            textVermelho = itemView.findViewById(R.id.textCartaoVermelhoMinicampo);
            textRival = itemView.findViewById(R.id.textTimeForaMinicampo);
            textTimeCasa = itemView.findViewById(R.id.textTimeCasaMinicampo);
            textGolRival = itemView.findViewById(R.id.textTimeForaPlacarMinicampo);
            textGolTimeCasa = itemView.findViewById(R.id.textTimeCasaPlacarMinicampo);
            textNota = itemView.findViewById(R.id.textNotaMinicampo);

        }
    }


}
