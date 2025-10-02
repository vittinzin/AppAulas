package com.vitor.aulas.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;

public class ProfessorActivity extends AppCompatActivity {

    private TextView welcomeTxt;
    private Button registrarAlunoBtn, criarTurmaBtn, atribuirAtividadeBtn, gerenciarAtividadesBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        welcomeTxt = findViewById(R.id.welcomeTxt);
        registrarAlunoBtn = findViewById(R.id.registrarAlunoBtn);
        criarTurmaBtn = findViewById(R.id.criarTurmaBtn);
        atribuirAtividadeBtn = findViewById(R.id.atribuirAtividadeBtn);
        gerenciarAtividadesBtn = findViewById(R.id.gerenciarAtividadesBtn);

        // Recebe o email do professor logado
        String email = getIntent().getStringExtra("usuario_email");
        welcomeTxt.setText("Bem-vindo, Professor: " + email);

        // Botões
        registrarAlunoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfessorActivity.this, RegistrarAlunoActivity.class);
            startActivity(intent);
        });

        criarTurmaBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfessorActivity.this, CriarTurmaActivity.class);
            startActivity(intent);
        });

        atribuirAtividadeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfessorActivity.this, AtribuirAtividadeActivity.class);
            startActivity(intent);
        });

        gerenciarAtividadesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfessorActivity.this, AtribuirAtividadeActivity.class);
            startActivity(intent);
        });
    }
}
