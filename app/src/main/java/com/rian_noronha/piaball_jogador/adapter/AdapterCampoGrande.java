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

public class AdapterCampoGrande extends RecyclerView.Adapter<AdapterCampoGrande.MyViewHolder> {

    private List<JogoJogador> jogosJogadorCampoGrande = new ArrayList<>();
    private Context context;

    public AdapterCampoGrande(List<JogoJogador> jogosJogadorCampoGrande, Context context) {
        this.jogosJogadorCampoGrande = jogosJogadorCampoGrande;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_jogos_jogador_campo_grande, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JogoJogador jogoJogador = this.jogosJogadorCampoGrande.get(position);
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
        return jogosJogadorCampoGrande.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textData, textGol, textAssistencia, textDesarme, textFinalizacao,
                textAmarelo, textVermelho, textRival, textTimeCasa, textGolRival,
                textGolTimeCasa, textNota;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textData = itemView.findViewById(R.id.textDataCampoGrande);
            textGol = itemView.findViewById(R.id.textGolCampoGrande);
            textAssistencia = itemView.findViewById(R.id.textAssistenciaCampoGrande);
            textDesarme = itemView.findViewById(R.id.textDesarmeCampoGrande);
            textFinalizacao = itemView.findViewById(R.id.textFinalizacaoCampoGrande);
            textAmarelo = itemView.findViewById(R.id.textCartaoAmareloCampoGrande);
            textVermelho = itemView.findViewById(R.id.textCartaoVermelhoCampoGrande);
            textRival = itemView.findViewById(R.id.textTimeForaCampoGrande);
            textTimeCasa = itemView.findViewById(R.id.textTimeCasaCampoGrande);
            textGolRival = itemView.findViewById(R.id.textTimeForaPlacarCampoGrande);
            textGolTimeCasa = itemView.findViewById(R.id.textTimeCasaPlacarCampoGrande);
            textNota = itemView.findViewById(R.id.textNotaCampoGrande);

        }
    }


}
