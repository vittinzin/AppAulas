package com.vitor.aulas.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.AtividadeDbController;
import com.vitor.aulas.model.Atividade;

import java.util.List;

public class AlunoAtividadesActivity extends AppCompatActivity {

    private LinearLayout containerAtividades;
    private AtividadeDbController dbController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_atividades);

        containerAtividades = findViewById(R.id.containerAtividades);
        dbController = new AtividadeDbController(this);

        // Recebe o CPF do aluno logado
        String alunoCpf = getIntent().getStringExtra("usuario_cpf");
        if (alunoCpf == null) {
            Toast.makeText(this, "CPF do aluno não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<Atividade> atividades = dbController.getAtividadesDoAluno(alunoCpf);

        if (atividades.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("Nenhuma atividade encontrada.");
            emptyTv.setTextSize(16f);
            containerAtividades.addView(emptyTv);
        } else {
            for (Atividade atividade : atividades) {
                // Título
                TextView tituloTv = new TextView(this);
                tituloTv.setText(atividade.getTitulo());
                tituloTv.setTextSize(16f);
                tituloTv.setTypeface(null, Typeface.BOLD);
                tituloTv.setPadding(0, 8, 0, 0);

                // Descrição
                TextView descricaoTv = new TextView(this);
                descricaoTv.setText(atividade.getDescricao());
                descricaoTv.setTextSize(14f);
                descricaoTv.setPadding(0, 4, 0, 8);

                // Adiciona ao container
                containerAtividades.addView(tituloTv);
                containerAtividades.addView(descricaoTv);
            }
        }
    }
}
