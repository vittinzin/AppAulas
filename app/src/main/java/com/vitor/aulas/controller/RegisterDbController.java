package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vitor.aulas.db.RegisterDb;
import com.vitor.aulas.model.Usuario;
import com.vitor.aulas.util.SenhaUtil;

import java.util.ArrayList;
import java.util.List;

public class RegisterDbController {

    private RegisterDb rdb;

    public RegisterDbController(Context context) {
        rdb = new RegisterDb(context);
    }

    public void insert(Usuario usuario) {
        SQLiteDatabase db = null;
        try {
            db = rdb.getWritableDatabase();
            ContentValues data = new ContentValues();
            data.put(RegisterDb.EMAIL, usuario.getEmail());
            data.put(RegisterDb.CPF, usuario.getCpf());
            data.put(RegisterDb.PASSWORD, SenhaUtil.hashPassword(usuario.getSenha()));
            data.put(RegisterDb.TIPO, usuario.getTipo());

            long result = db.insert(RegisterDb.TABLE, null, data);
            if (result == -1) {
                Log.e("DB", "Erro ao inserir usuário");
            } else {
                Log.d("DB", "Usuário inserido com sucesso, id=" + result);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = rdb.getReadableDatabase();
            cursor = db.query(RegisterDb.TABLE,
                    new String[]{RegisterDb.EMAIL, RegisterDb.CPF, RegisterDb.PASSWORD, RegisterDb.TIPO},
                    null, null, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.EMAIL));
                String cpf = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.CPF));
                String senha = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.PASSWORD));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow(RegisterDb.TIPO));
                usuarios.add(new Usuario(email, cpf, senha, tipo));
            }

            Log.d("DB", "Total de usuários no banco: " + usuarios.size());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return usuarios;
    }

    public boolean redefinirSenha(String email, String novaSenha) {
        SQLiteDatabase db = null;
        try {
            db = rdb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(RegisterDb.PASSWORD, SenhaUtil.hashPassword(novaSenha));
            int rows = db.update(RegisterDb.TABLE, values, RegisterDb.EMAIL + " = ?", new String[]{email});
            return rows > 0;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean verificaUsuarioPorEmail(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = rdb.getReadableDatabase();
            cursor = db.query(RegisterDb.TABLE,
                    new String[]{RegisterDb.EMAIL},
                    RegisterDb.EMAIL + " = ?",
                    new String[]{email},
                    null, null, null);

            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}
