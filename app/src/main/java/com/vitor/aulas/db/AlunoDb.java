package com.vitor.aulas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlunoDb extends SQLiteOpenHelper {

    public static final String DB_NAME = "SchoolDB";
    public static final int VERSION = 1;

    public static final String TABLE_ALUNO = "Aluno";
    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String CPF = "cpf";
    public static final String TURMA_ID = "turma_id";

    public static final String TABLE_TURMA = "Turma";
    public static final String TURMA_NOME = "nome";
    public static final String TURMA_ANO = "ano";

    public static final String TABLE_ATIVIDADE = "Atividade";
    public static final String ATIVIDADE_TITULO = "titulo";
    public static final String ATIVIDADE_DESC = "descricao";
    public static final String ALUNO_ID = "aluno_id";

    public AlunoDb(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TURMA = "CREATE TABLE " + TABLE_TURMA + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TURMA_NOME + " TEXT, "
                + TURMA_ANO + " TEXT)";
        db.execSQL(CREATE_TURMA);

        String CREATE_ALUNO = "CREATE TABLE " + TABLE_ALUNO + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME + " TEXT, "
                + CPF + " TEXT UNIQUE, "
                + TURMA_ID + " INTEGER, "
                + "FOREIGN KEY(" + TURMA_ID + ") REFERENCES " + TABLE_TURMA + "(" + ID + "))";
        db.execSQL(CREATE_ALUNO);

        String CREATE_ATIVIDADE = "CREATE TABLE " + TABLE_ATIVIDADE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATIVIDADE_TITULO + " TEXT, "
                + ATIVIDADE_DESC + " TEXT, "
                + ALUNO_ID + " INTEGER, "
                + "FOREIGN KEY(" + ALUNO_ID + ") REFERENCES " + TABLE_ALUNO + "(" + ID + "))";
        db.execSQL(CREATE_ATIVIDADE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATIVIDADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALUNO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURMA);
        onCreate(db);
    }
}

