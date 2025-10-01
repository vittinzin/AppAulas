package com.vitor.aulas.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.RegisterDbController;
import com.vitor.aulas.model.Usuario;
import com.vitor.aulas.util.SenhaUtil;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Switch switch1;
    private TextView alunoTxt, docenteTxt, telaCadastro;
    private EditText emailEt, senhaEt;
    private ImageView olhoImg;
    private Button loginBtn;
    private RegisterDbController dbController;
    private boolean isDocente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        switch1 = findViewById(R.id.switch1);
        docenteTxt = findViewById(R.id.DocenteTxt);
        alunoTxt = findViewById(R.id.alunoTxt);
        emailEt = findViewById(R.id.emailEt);
        senhaEt = findViewById(R.id.senhaEt);
        olhoImg = findViewById(R.id.olhoImg);
        telaCadastro = findViewById(R.id.cadastroTxt);
        loginBtn = findViewById(R.id.loginBtn);

        dbController = new RegisterDbController(this);

        telaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
        });

        olhoImg.setOnClickListener(new View.OnClickListener() {
            boolean senhaVisivel = false;

            @Override
            public void onClick(View v) {
                if (senhaVisivel) {
                    senhaEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    senhaEt.setSelection(senhaEt.length());
                    senhaVisivel = false;
                    olhoImg.setImageResource(R.drawable.fechado);
                } else {
                    senhaEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    senhaEt.setSelection(senhaEt.length());
                    senhaVisivel = true;
                    olhoImg.setImageResource(R.drawable.aberto);
                }
            }
        });

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDocente = isChecked;
            if (isChecked) {
                alunoTxt.setTypeface(null, Typeface.NORMAL);
                docenteTxt.setTypeface(null, Typeface.BOLD);
                emailEt.setHint("Email do docente");
            } else {
                alunoTxt.setTypeface(null, Typeface.BOLD);
                docenteTxt.setTypeface(null, Typeface.NORMAL);
                emailEt.setHint("Email do aluno");
            }
        });

        loginBtn.setOnClickListener(v -> validarLogin());
    }

    private void validarLogin() {
        String email = emailEt.getText().toString().trim();
        String senha = senhaEt.getText().toString().trim();

        if (email.isEmpty()) {
            emailEt.setError("Campo obrigatório");
            emailEt.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            senhaEt.setError("Campo obrigatório");
            senhaEt.requestFocus();
            return;
        }

        List<Usuario> usuarios = dbController.getAll();
        boolean encontrado = false;
        String tipoEsperado = isDocente ? "Docente" : "Aluno";

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getTipo().equalsIgnoreCase(tipoEsperado)) {

                if (SenhaUtil.checkPassword(senha, u.getSenha())) {
                    encontrado = true;

                    if (u.getTipo().equalsIgnoreCase("Aluno")) {
                        Intent intent = new Intent(LoginActivity.this, AlunoActivity.class);
                        intent.putExtra("usuario_cpf", u.getCpf());
                        intent.putExtra("usuario_email", u.getEmail());
                        startActivity(intent);
                        finish();
                    } else if (u.getTipo().equalsIgnoreCase("Docente")) {
                        Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                        intent.putExtra("usuario_email", u.getEmail());
                        intent.putExtra("usuario_cpf", u.getCpf());
                        startActivity(intent);
                        finish();
                    }
                    break;
                }
            }
        }

        if (!encontrado) {
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }
}