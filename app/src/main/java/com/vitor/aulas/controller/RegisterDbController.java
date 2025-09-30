package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vitor.aulas.db.RegisterDb;
import com.vitor.aulas.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RegisterDbController {

    private SQLiteDatabase db;
    private RegisterDb rdb;

    public RegisterDbController(Context context) {
        rdb = new RegisterDb(context);
    }

    public void insert(Usuario usuario) {
        ContentValues data;

        db = rdb.getWritableDatabase();
        data = new ContentValues();
        data.put(RegisterDb.EMAIL, usuario.getEmail());
        data.put(RegisterDb.CPF, usuario.getCpf());
        data.put(RegisterDb.PASSWORD, usuario.getSenha());
        data.put(RegisterDb.TIPO, usuario.getTipo());

        long result = db.insert(RegisterDb.TABLE, null, data);

        if (result == -1) {
            Log.e("DB", "Erro ao inserir usuário");
        } else {
            Log.d("DB", "Usuário inserido com sucesso, id=" + result);
        }
    }
    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        SQLiteDatabase db = rdb.getReadableDatabase();

        Cursor cursor = db.query(RegisterDb.TABLE,
                new String[]{RegisterDb.EMAIL, RegisterDb.CPF, RegisterDb.PASSWORD, RegisterDb.TIPO},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.EMAIL));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.CPF));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.PASSWORD));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.TIPO));
            usuarios.add(new Usuario(email, cpf, senha, tipo));
        }

        cursor.close();
        db.close();

        Log.d("DB", "Total de usuários no banco: " + usuarios.size());
        return usuarios;
    }

}
