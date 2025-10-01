package com.vitor.aulas.controller;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vitor.aulas.db.AlunoDb;
import com.vitor.aulas.model.Turma;
import java.util.ArrayList;
import java.util.List;

public class TurmaDbController {
    private AlunoDb dbHelper;

    public TurmaDbController(Context context) {
        dbHelper = new AlunoDb(context);
    }

    public long insertTurma(Turma turma) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.TURMA_NOME, turma.getNome());
        cv.put(AlunoDb.TURMA_ANO, turma.getAno());
        long id = db.insert(AlunoDb.TABLE_TURMA, null, cv);
        db.close();
        return id;
    }

    public List<Turma> getAllTurmas() {
        List<Turma> turmas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlunoDb.TABLE_TURMA,
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            turmas.add(new Turma(
                    cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ANO))
            ));
        }
        cursor.close();
        db.close();
        return turmas;
    }
}

