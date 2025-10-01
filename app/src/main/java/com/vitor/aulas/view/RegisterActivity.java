package com.vitor.aulas.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.RegisterDbController;
import com.vitor.aulas.controller.RegisterController;
import com.vitor.aulas.db.RegisterDb;
import com.vitor.aulas.model.Usuario;

import java.util.List;

public class RegisterActivity extends BaseActivity {

    private Switch switch2;
    private TextView alunoTxt2, docenteTxt2;
    private EditText emailEt2, cpfEt, senhaEt2, confirmarSenhaEt;
    private Button loginBtn2;
    private ImageView imageView2;
    private RegisterDb rdb;
    private RegisterController rc;
    private RegisterDbController dblo;

    private boolean isDocente = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_register);

        switch2 = findViewById(R.id.switch2);
        docenteTxt2 = findViewById(R.id.docenteTxt2);
        alunoTxt2 = findViewById(R.id.alunoTxt2);
        emailEt2 = findViewById(R.id.emailEt2);
        cpfEt = findViewById(R.id.cpfEt);
        senhaEt2 = findViewById(R.id.senhaEt2);
        confirmarSenhaEt = findViewById(R.id.confirmarSenhaEt);
        loginBtn2 = findViewById(R.id.loginBtn2);
        imageView2 = findViewById(R.id.imageView2);

        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDocente = isChecked;
            if (isChecked) {
                alunoTxt2.setTypeface(null, android.graphics.Typeface.NORMAL);
                docenteTxt2.setTypeface(null, android.graphics.Typeface.BOLD);
                emailEt2.setHint("Email do docente");
                cpfEt.setHint("CPF do docente");
            } else {
                alunoTxt2.setTypeface(null, android.graphics.Typeface.BOLD);
                docenteTxt2.setTypeface(null, android.graphics.Typeface.NORMAL);
                emailEt2.setHint("Email do aluno");
                cpfEt.setHint("CPF do aluno");
            }
        });

        loginBtn2.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String email = emailEt2.getText().toString().trim();
        String cpf = cpfEt.getText().toString().trim();
        String senha = senhaEt2.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEt.getText().toString().trim();

        if (!isEmailValido(email)) {
            Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
            return;
        }

        rc = new RegisterController();
        dblo = new RegisterDbController(this);

        String alunoOu;
        if (isDocente) {
           alunoOu = "Docente";
        } else {
            alunoOu = "Aluno";
        }

        Usuario usuario = new Usuario(
                emailEt2.getText().toString(),
                cpfEt.getText().toString(),
                senhaEt2.getText().toString(),
                alunoOu
        );

        if (rc.confirmRegister(usuario) == -1) {
            dblo.insert(usuario);
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            List<Usuario> list = dblo.getAll();
            Log.d("DB", "Usuarios no banco agora: " + list.size());
        } else {
            Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isEmailValido(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }
}
