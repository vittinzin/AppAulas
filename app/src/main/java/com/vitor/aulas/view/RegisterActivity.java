package com.vitor.aulas.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vitor.aulas.R;
import com.vitor.aulas.controller.DBController_Agenda;
import com.vitor.aulas.controller.DBController_Login;
import com.vitor.aulas.model.Usuario;

import java.io.File;
import java.io.FileOutputStream;

public class RegisterActivity extends BaseActivity {

    private Switch switch2;
    private TextView alunoTxt2, docenteTxt2;
    private EditText emailEt2, cpfEt, senhaEt2, confirmarSenhaEt;
    private Button loginBtn2;
    private ImageView imageView2;

    private boolean isDocente = false; // controla se o cadastro é docente ou aluno

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
        imageView2 = findViewById(R.id.imageView2);

        // troca aluno/docente
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

        // botão registrar
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

        // usa sempre a foto padrão
        Bitmap bitmap = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
        String pathFoto = salvarImagemEmArquivo(bitmap, cpf);

        // salvar no banco
        DBController_Login db = new DBController_Login(this);
        Usuario usuario = new Usuario(email, cpf, senha, isDocente ? "DOCENTE" : "ALUNO");
        db.cadastrarUsuario(usuario);

        // salvar login
        SharedPreferences prefs = getSharedPreferences("usuarioLogado", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cpf", cpf);
        editor.putString("fotoUsuario", pathFoto);
        editor.apply();

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

        // ir para main
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    private boolean isEmailValido(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }

    private String salvarImagemEmArquivo(Bitmap bitmap, String nomeArquivo) {
        File diretorio = new File(getFilesDir(), "usuarios");
        if (!diretorio.exists()) diretorio.mkdirs();

        File arquivoImagem = new File(diretorio, nomeArquivo + ".jpg");
        try (FileOutputStream out = new FileOutputStream(arquivoImagem)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            return arquivoImagem.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
