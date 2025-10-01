package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitor.aulas.model.Atividade;

import java.util.ArrayList;
import java.util.List;

public class AtividadeDbController extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aulas.db";
    private static final int DATABASE_VERSION = 2;

    public AtividadeDbController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS atividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "descricao TEXT," +
                "aluno_cpf TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS atividades");
        onCreate(db);
    }


    public long insertAtividade(Atividade atividade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("titulo", atividade.getTitulo());
        cv.put("descricao", atividade.getDescricao());
        cv.put("aluno_cpf", atividade.getAlunoCpf());
        long id = db.insert("atividades", null, cv);
        db.close();
        return id;
    }


    public List<Atividade> getAtividadesDoAluno(String alunoCpf) {
        List<Atividade> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM atividades WHERE aluno_cpf = ?",
                new String[]{alunoCpf}
        );

        if (cursor.moveToFirst()) {
            do {
                Atividade atividade = new Atividade();
                atividade.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                atividade.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                atividade.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                atividade.setAlunoCpf(alunoCpf);
                lista.add(atividade);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }
}
