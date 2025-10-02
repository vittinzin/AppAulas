package com.vitor.aulas.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;

public class AlunoActivity extends AppCompatActivity {

    private TextView welcomeTxt;
    private ImageButton minhasAtividadesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        welcomeTxt = findViewById(R.id.welcomeTxt);
        minhasAtividadesBtn = findViewById(R.id.minhasAtividadesBtn);

        String email = getIntent().getStringExtra("usuario_email");
        String nome = email.replace("@aluno.com", "");
        welcomeTxt.setText("Bem-vindo " + nome + "!");

        String cpf = getIntent().getStringExtra("usuario_cpf");

        minhasAtividadesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AlunoActivity.this, AlunoAtividadesActivity.class);
            intent.putExtra("usuario_cpf", cpf); // passa CPF para filtrar
            startActivity(intent);
        });
    }
}

