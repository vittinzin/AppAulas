package com.vitor.aulas.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;

public class AlunoActivity extends AppCompatActivity {

    private TextView welcomeTxt;
    private Button minhasAtividadesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        welcomeTxt = findViewById(R.id.welcomeTxt);
        minhasAtividadesBtn = findViewById(R.id.minhasAtividadesBtn);

        // Recebe o email do aluno logado
        String email = getIntent().getStringExtra("usuario_email");
        welcomeTxt.setText("Bem-vindo, Aluno: " + email);

        // Botão leva para a tela de atividades
        minhasAtividadesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AlunoActivity.this, AlunoAtividadesActivity.class);
            intent.putExtra("usuario_email", email); // passa email para filtrar atividades do aluno
            startActivity(intent);
        });
    }
}

