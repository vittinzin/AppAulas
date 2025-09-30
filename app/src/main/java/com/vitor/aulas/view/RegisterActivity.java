package com.vitor.aulas.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vitor.aulas.R;

public class RegisterActivity extends BaseActivity {

    private Switch switch2;
    private TextView alunoTxt2, docenteTxt2;
    private EditText emailEt2, cpfEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        switch2 = findViewById(R.id.switch2);
        docenteTxt2 = findViewById(R.id.docenteTxt2);
        alunoTxt2 = findViewById(R.id.alunoTxt2);
        emailEt2 = findViewById(R.id.emailEt2);
        cpfEt = findViewById(R.id.cpfEt);

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alunoTxt2.setTypeface(null, Typeface.NORMAL);
                    docenteTxt2.setTypeface(null, Typeface.BOLD);

                } else {
                    alunoTxt2.setTypeface(null, Typeface.BOLD);
                    docenteTxt2.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alunoTxt2.setTypeface(null, Typeface.NORMAL);
                    docenteTxt2.setTypeface(null, Typeface.BOLD);
                    emailEt2.setHint("Email do docente");
                    cpfEt.setHint("CPF do docente");

                } else {
                    alunoTxt2.setTypeface(null, Typeface.BOLD);
                    docenteTxt2.setTypeface(null, Typeface.NORMAL);
                    emailEt2.setHint("Email do aluno");
                    cpfEt.setHint("CPF do aluno");
                }
            }
        });
    }
}