package com.vitor.aulas.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.AlunoDbController;
import com.vitor.aulas.controller.TurmaDbController;
import com.vitor.aulas.model.Aluno;

public class RegistrarAlunoActivity extends AppCompatActivity {

    private EditText nomeEt, cpfEt;
    private Spinner turmaSpinner;
    private Button registrarBtn;
    private ImageButton btnVoltar;
    private AlunoDbController alunoController;
    private TurmaDbController turmaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_aluno);

        nomeEt = findViewById(R.id.nomeEt);
        cpfEt = findViewById(R.id.cpfEt);
        turmaSpinner = findViewById(R.id.turmaSpinner);
        registrarBtn = findViewById(R.id.registrarBtn);
        btnVoltar = findViewById(R.id.btnVoltar);

        alunoController = new AlunoDbController(this);
        turmaController = new TurmaDbController(this);

        btnVoltar.setOnClickListener(v -> finish());

        String[] turmas = {"Turma A", "Turma B", "Turma C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                turmas
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turmaSpinner.setAdapter(adapter);

        registrarBtn.setOnClickListener(v -> {
            String nome = nomeEt.getText().toString().trim();
            String cpf = cpfEt.getText().toString().trim();
            String turmaSelecionada = turmaSpinner.getSelectedItem().toString();

            if (nome.isEmpty() || cpf.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int turmaId = turmaSpinner.getSelectedItemPosition() + 1;

            Aluno aluno = new Aluno(0, nome, cpf, turmaId);
            long id = alunoController.insertAluno(aluno);

            if (id != -1) {
                Toast.makeText(this, "Aluno registrado na " + turmaSelecionada, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao registrar aluno", Toast.LENGTH_SHORT).show();
            }
        });
    }
}