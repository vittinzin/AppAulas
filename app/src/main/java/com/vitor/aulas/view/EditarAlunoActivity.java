package com.vitor.aulas.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.AlunoDbController;
import com.vitor.aulas.model.Aluno;

public class EditarAlunoActivity extends AppCompatActivity {

    private EditText nomeEt, cpfEt, turmaEt;
    private Button salvarBtn;
    private AlunoDbController alunoDbController;
    private String cpfAluno;
    private Aluno aluno;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_aluno);

        nomeEt = findViewById(R.id.nomeEt);
        cpfEt = findViewById(R.id.cpfEt);
        turmaEt = findViewById(R.id.turmaEt);
        salvarBtn = findViewById(R.id.salvarBtn);

        // Tornar CPF não-editável
        cpfEt.setEnabled(false);

        alunoDbController = new AlunoDbController(this);

        // Recuperar CPF enviado pela intent
        cpfAluno = getIntent().getStringExtra("cpf_aluno");

        if (cpfAluno != null) {
            aluno = alunoDbController.getAlunoByCpf(cpfAluno);
            if (aluno != null) {
                nomeEt.setText(aluno.getNome());
                cpfEt.setText(aluno.getCpf());
                turmaEt.setText(String.valueOf(aluno.getTurmaId()));
            }
        }

        salvarBtn.setOnClickListener(v -> salvarAlteracoes());
    }

    private void salvarAlteracoes() {
        if (aluno == null) return;

        String novoNome = nomeEt.getText().toString().trim();
        String novaTurmaStr = turmaEt.getText().toString().trim();

        if (novoNome.isEmpty() || novaTurmaStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int novaTurma;
        try {
            novaTurma = Integer.parseInt(novaTurmaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID da turma inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Atualizar apenas nome e turma
        aluno.setNome(novoNome);
        aluno.setTurmaId(novaTurma);

        int linhasAtualizadas = alunoDbController.updateAluno(aluno);

        if (linhasAtualizadas > 0) {
            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar aluno!", Toast.LENGTH_SHORT).show();
        }
    }
}
