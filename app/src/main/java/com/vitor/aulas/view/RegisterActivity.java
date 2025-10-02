package com.vitor.aulas.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.AlunoDbController;
import com.vitor.aulas.controller.RegisterDbController;
import com.vitor.aulas.controller.RegisterController;
import com.vitor.aulas.controller.ProfessorDbController;
import com.vitor.aulas.model.Aluno;
import com.vitor.aulas.model.Professor;
import com.vitor.aulas.model.Usuario;
import com.vitor.aulas.util.SenhaUtil;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    ImageButton btnVoltar;
    private Switch switch2;
    private TextView alunoTxt2, docenteTxt2;
    private EditText emailEt2, cpfEt, senhaEt2, confirmarSenhaEt;
    private Button loginBtn2;
    private RegisterController rc;
    private RegisterDbController dblo;
    private AlunoDbController alunoController;
    private ProfessorDbController professorController;

    private boolean isDocente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        switch2 = findViewById(R.id.switch2);
        docenteTxt2 = findViewById(R.id.docenteTxt2);
        alunoTxt2 = findViewById(R.id.alunoTxt2);
        emailEt2 = findViewById(R.id.emailEt2);
        cpfEt = findViewById(R.id.cpfEt);
        senhaEt2 = findViewById(R.id.senhaEt2);
        confirmarSenhaEt = findViewById(R.id.confirmarSenhaEt);
        loginBtn2 = findViewById(R.id.loginBtn2);
        btnVoltar = findViewById(R.id.btnVoltar);

        rc = new RegisterController();
        dblo = new RegisterDbController(this);
        alunoController = new AlunoDbController(this);
        professorController = new ProfessorDbController(this);

        btnVoltar.setOnClickListener(v -> finish());

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

        if (email.isEmpty()) {
            emailEt2.setError("Campo obrigatório");
            emailEt2.requestFocus();
            return;
        }

        if (!isEmailValido(email)) {
            emailEt2.setError("E-mail inválido. Use o formato: exemplo@email.com");
            emailEt2.requestFocus();
            return;
        }

        if (cpf.isEmpty()) {
            cpfEt.setError("Campo obrigatório");
            cpfEt.requestFocus();
            return;
        }

        String cpfLimpo = limparCPF(cpf);
        if (!isCPFValido(cpfLimpo)) {
            cpfEt.setError("CPF inválido");
            cpfEt.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            senhaEt2.setError("Campo obrigatório");
            senhaEt2.requestFocus();
            return;
        }

        if (!isSenhaValida(senha)) {
            senhaEt2.setError("A senha deve ter no mínimo 8 caracteres, incluindo letras e números");
            senhaEt2.requestFocus();
            return;
        }

        if (confirmarSenha.isEmpty()) {
            confirmarSenhaEt.setError("Campo obrigatório");
            confirmarSenhaEt.requestFocus();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            confirmarSenhaEt.setError("As senhas não conferem");
            confirmarSenhaEt.requestFocus();
            Toast.makeText(this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dblo.cpfExiste(cpf)){
            cpfEt.setError("CPF inválido");
            cpfEt.requestFocus();
            return;
        }

        String senhaHash = SenhaUtil.hashPassword(senha);

        String tipo = isDocente ? "Docente" : "Aluno";
        Usuario usuario = new Usuario(email, cpfLimpo, senhaHash, tipo);

        if (rc.confirmRegister(usuario) == -1) {

            dblo.insert(usuario);

            if (isDocente) {
                Professor professor = new Professor(0, email, cpfLimpo, email);
                professorController.insertProfessor(professor);
            } else {
                Aluno aluno = new Aluno(0, email, cpfLimpo, 1);
                alunoController.insertAluno(aluno);
            }

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();

            List<Usuario> list = dblo.getAll();
            android.util.Log.d("DB", "Usuarios no banco agora: " + list.size());

        } else {
            Toast.makeText(this, "Erro ao realizar cadastro. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    private boolean isSenhaValida(String senha) {
        if (senha == null || senha.length() < 8) {
            return false;
        }
        boolean temLetra = false;
        boolean temNumero = false;
        for (char s : senha.toCharArray()) {
            if (Character.isLetter(s)) {
                temLetra = true;
            }
            if (Character.isDigit(s)) {
                temNumero = true;
            }
            if (temLetra && temNumero) {
                break;
            }
        }
        return temLetra && temNumero;
    }

    private String limparCPF(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }

    private boolean isCPFValido(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        try {
            int[] numeros = new int[11];
            for (int i = 0; i < 11; i++) {
                numeros[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += numeros[i] * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }
            if (numeros[9] != primeiroDigito) {
                return false;
            }
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += numeros[i] * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }
            return numeros[10] == segundoDigito;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}