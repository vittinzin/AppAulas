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

        // ... (Verificações de campo vazio permanecem)

        String tipoEsperado = isDocente ? "Docente" : "Aluno";

        // 1. Tenta buscar o usuário exato (email E tipo)
        // NOTE: Você precisa implementar o método getUsuarioPorEmailETipo()
        Usuario u = dbController.getUsuarioPorEmailETipo(email, tipoEsperado);

        if (u != null) {
            // 2. Se o usuário foi encontrado, verifica a senha
            if (SenhaUtil.checkPassword(senha, u.getSenha())) {

                // 3. LOGIN BEM-SUCEDIDO: Inicia a Activity correta
                Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                Intent intent;
                if (u.getTipo().equalsIgnoreCase("Aluno")) {
                    intent = new Intent(LoginActivity.this, AlunoActivity.class);
                } else { // Docente
                    intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                }

                intent.putExtra("usuario_cpf", u.getCpf());
                intent.putExtra("usuario_email", u.getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                // 4. SENHA INCORRETA: O usuário existe, mas a senha está errada.
                Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 5. USUÁRIO NÃO ENCONTRADO (Email ou Tipo estão errados)
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }
}