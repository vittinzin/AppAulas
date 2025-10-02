package com.vitor.aulas.view;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.DatabaseController;
import com.vitor.aulas.model.Turma;

public class CriarTurmaActivity extends AppCompatActivity {

    private EditText nomeEt, anoEt;
    private Button criarBtn;
    private DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_turma);

        nomeEt = findViewById(R.id.nomeEt);
        anoEt = findViewById(R.id.anoEt);
        criarBtn = findViewById(R.id.criarBtn);

        dbController = new DatabaseController(this);

        criarBtn.setOnClickListener(v -> {
            String nome = nomeEt.getText().toString().trim();
            String ano = anoEt.getText().toString().trim();

            if(nome.isEmpty() || ano.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Turma turma = new Turma(0, nome, ano);
            long id = dbController.insertTurma(turma);

            if(id != -1){
                Toast.makeText(this, "Turma criada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao criar turma", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
