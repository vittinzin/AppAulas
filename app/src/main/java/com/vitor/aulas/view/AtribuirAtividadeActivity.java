package com.vitor.aulas.view;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vitor.aulas.R;
import com.vitor.aulas.controller.DatabaseController;
import com.vitor.aulas.model.Aluno;
import com.vitor.aulas.model.Atividade;

public class AtribuirAtividadeActivity extends AppCompatActivity {

    private EditText cpfAlunoEt, tituloEt, descricaoEt;
    private Button atribuirBtn;
    private DatabaseController dbController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atribuir_atividade);

        cpfAlunoEt = findViewById(R.id.cpfAlunoEt);
        tituloEt = findViewById(R.id.tituloEt);
        descricaoEt = findViewById(R.id.descricaoEt);
        atribuirBtn = findViewById(R.id.atribuirBtn);

        dbController = new DatabaseController(this);

        atribuirBtn.setOnClickListener(v -> {
            String cpf = cpfAlunoEt.getText().toString().trim();
            String titulo = tituloEt.getText().toString().trim();
            String descricao = descricaoEt.getText().toString().trim();

            if(cpf.isEmpty() || titulo.isEmpty() || descricao.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Aluno aluno = dbController.getAlunoByCpf(cpf);
            if(aluno == null){
                Toast.makeText(this, "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // CORREÇÃO: usar CPF do aluno, não o ID
            Atividade atividade = new Atividade(0, titulo, descricao, aluno.getCpf());
            long id = dbController.insertAtividade(atividade);

            if(id != -1){
                Toast.makeText(this, "Atividade atribuída com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atribuir atividade", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
