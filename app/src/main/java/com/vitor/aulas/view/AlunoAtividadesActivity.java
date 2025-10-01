package com.vitor.aulas.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vitor.aulas.R;
import com.vitor.aulas.adapter.AtividadeAdapter;
import com.vitor.aulas.controller.AtividadeDbController;
import com.vitor.aulas.model.Atividade;

import java.util.List;

public class AlunoAtividadesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;
    private AtividadeDbController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_atividades);

        recyclerView = findViewById(R.id.recyclerViewAtividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbController = new AtividadeDbController(this);


        String alunoCpf = getIntent().getStringExtra("usuario_cpf");
        List<Atividade> atividades = dbController.getAtividadesDoAluno(alunoCpf);

        adapter = new AtividadeAdapter(atividades);
        recyclerView.setAdapter(adapter);
    }
}
