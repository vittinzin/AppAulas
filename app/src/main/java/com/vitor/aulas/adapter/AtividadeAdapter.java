package com.vitor.aulas.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vitor.aulas.R;
import com.vitor.aulas.model.Atividade;
import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

    private List<Atividade> atividades;

    public AtividadeAdapter(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atividade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Atividade a = atividades.get(position);
        holder.titulo.setText(a.getTitulo());
        holder.descricao.setText(a.getDescricao());
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descricao;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloAtividade);
            descricao = itemView.findViewById(R.id.descricaoAtividade);
        }
    }
}

