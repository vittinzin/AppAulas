package com.vitor.aulas.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.vitor.aulas.R;

public class LoginActivity extends BaseActivity {

    private Switch switch1;
    private TextView alunoTxt, docenteTxt, telaCadastro;
    private EditText emailEt, senhaEt;
    private ImageView olhoImg;

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

        telaCadastro.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alunoTxt.setTypeface(null, Typeface.NORMAL);
                    docenteTxt.setTypeface(null, Typeface.BOLD);
                    emailEt.setHint("Email do docente");

                } else {
                    alunoTxt.setTypeface(null, Typeface.BOLD);
                    docenteTxt.setTypeface(null, Typeface.NORMAL);
                    emailEt.setHint("Email do aluno");
                }
            }
        });
    }
}