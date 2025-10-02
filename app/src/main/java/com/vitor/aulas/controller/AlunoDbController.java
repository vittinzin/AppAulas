package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitor.aulas.db.AlunoDb;
import com.vitor.aulas.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class AlunoDbController {
    private AlunoDb dbHelper;

    public AlunoDbController(Context context) {
        dbHelper = new AlunoDb(context);
    }


    public long insertAluno(Aluno aluno) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.NOME, aluno.getNome());
        cv.put(AlunoDb.CPF, aluno.getCpf());
        cv.put(AlunoDb.TURMA_ID, aluno.getTurmaId());
        long id = db.insert(AlunoDb.TABLE_ALUNO, null, cv);
        db.close();
        return id;
    }

    // Atualizar dados de um aluno existente
    public int updateAluno(Aluno aluno) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.NOME, aluno.getNome());
        cv.put(AlunoDb.CPF, aluno.getCpf());
        cv.put(AlunoDb.TURMA_ID, aluno.getTurmaId());
        int rowsAffected = db.update(AlunoDb.TABLE_ALUNO, cv, AlunoDb.ID + "=?",
                new String[]{String.valueOf(aluno.getId())});
        db.close();
        return rowsAffected;
    }

    // Deletar aluno pelo ID
    public int deleteAluno(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(AlunoDb.TABLE_ALUNO, AlunoDb.ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    // Buscar aluno pelo CPF
    public Aluno getAlunoByCpf(String cpf) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlunoDb.TABLE_ALUNO,
                new String[]{AlunoDb.ID, AlunoDb.NOME, AlunoDb.CPF, AlunoDb.TURMA_ID},
                AlunoDb.CPF + "=?",
                new String[]{cpf}, null, null, null);

        Aluno aluno = null;
        if (cursor != null && cursor.moveToFirst()) {
            aluno = new Aluno(
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
            );
        }
        if (cursor != null) cursor.close();
        db.close();
        return aluno;
    }

    // Buscar aluno pelo ID
    public Aluno getAlunoById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlunoDb.TABLE_ALUNO,
                new String[]{AlunoDb.ID, AlunoDb.NOME, AlunoDb.CPF, AlunoDb.TURMA_ID},
                AlunoDb.ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Aluno aluno = null;
        if (cursor != null && cursor.moveToFirst()) {
            aluno = new Aluno(
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
            );
        }
        if (cursor != null) cursor.close();
        db.close();
        return aluno;
    }

    // Buscar todos os alunos
    public List<Aluno> getAllAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlunoDb.TABLE_ALUNO, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            alunos.add(new Aluno(
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
            ));
        }

        cursor.close();
        db.close();
        return alunos;
    }
}
