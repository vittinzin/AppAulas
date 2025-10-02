package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitor.aulas.db.ProfessorDb;
import com.vitor.aulas.model.Professor;

import java.util.ArrayList;
import java.util.List;

public class ProfessorDbController {

    private SQLiteDatabase db;
    private ProfessorDb pdb;

    public ProfessorDbController(Context context){
        pdb = new ProfessorDb(context);
    }

    public void insertProfessor(Professor professor){
        db = pdb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfessorDb.NOME, professor.getNome());
        values.put(ProfessorDb.CPF, professor.getCpf());
        values.put(ProfessorDb.EMAIL, professor.getEmail());

        db.insert(ProfessorDb.TABLE, null, values);
        db.close();

    }

    public List<Professor> getAllProfessores(){
        List<Professor> professores = new ArrayList<>();
        db = pdb.getReadableDatabase();

        Cursor cursor = db.query(ProfessorDb.TABLE,
                new String[]{ProfessorDb.ID, ProfessorDb.NOME, ProfessorDb.CPF, ProfessorDb.EMAIL},
                null, null, null, null, null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessorDb.ID));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(ProfessorDb.NOME));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow(ProfessorDb.CPF));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ProfessorDb.EMAIL));

            professores.add(new Professor(id, nome, cpf, email));
        }

        cursor.close();
        db.close();
        return professores;
    }
}

