package com.vitor.aulas.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.DatabaseController;
import com.vitor.aulas.model.Usuario;
import com.vitor.aulas.util.SenhaUtil;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Switch switch1;
    private TextView alunoTxt, docenteTxt, telaCadastro, tvForgotPassword;
    private EditText emailEt, senhaEt;
    private ImageView olhoImg;
    private Button loginBtn;
    private DatabaseController dbController;
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
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        dbController = new DatabaseController(this);

        // Navegar para tela de cadastro
        telaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
        });

        // Mostrar/ocultar senha
        olhoImg.setOnClickListener(new View.OnClickListener() {
            boolean senhaVisivel = false;

            @Override
            public void onClick(View v) {
                if (senhaVisivel) {
                    senhaEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    olhoImg.setImageResource(R.drawable.fechado);
                } else {
                    senhaEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    olhoImg.setImageResource(R.drawable.aberto);
                }
                senhaEt.setSelection(senhaEt.length());
                senhaVisivel = !senhaVisivel;
            }
        });

        // Alternar tipo de usuário
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

        // Esqueci minha senha
        tvForgotPassword.setOnClickListener(v -> mostrarDialogRedefinirSenha());
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

        List<Usuario> usuarios = dbController.getAllUsuarios();
        boolean encontrado = false;
        String tipoEsperado = isDocente ? "Docente" : "Aluno";

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getTipo().equalsIgnoreCase(tipoEsperado)) {
                if (SenhaUtil.checkPassword(senha, u.getSenha())) {
                    encontrado = true;
                    if (u.getTipo().equalsIgnoreCase("Aluno")) {
                        startActivity(new Intent(LoginActivity.this, AlunoActivity.class)
                                .putExtra("usuario_cpf", u.getCpf())
                                .putExtra("usuario_email", u.getEmail()));
                    } else {
                        startActivity(new Intent(LoginActivity.this, ProfessorActivity.class)
                                .putExtra("usuario_cpf", u.getCpf())
                                .putExtra("usuario_email", u.getEmail()));
                    }
                    finish();
                    break;
                }
            }
        }

        if (!encontrado) {
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogRedefinirSenha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Redefinir senha");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputEmail = new EditText(this);
        inputEmail.setHint("Digite seu email");
        layout.addView(inputEmail);

        final EditText inputSenha = new EditText(this);
        inputSenha.setHint("Digite a nova senha");
        inputSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(inputSenha);

        builder.setView(layout);
        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = inputEmail.getText().toString().trim();
            String novaSenha = inputSenha.getText().toString().trim();

            if (email.isEmpty() || novaSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exists = dbController.verificaUsuarioPorEmail(email);
            if (exists) {
                dbController.redefinirSenha(email, novaSenha);
                Toast.makeText(this, "Senha redefinida com sucesso.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email não encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
