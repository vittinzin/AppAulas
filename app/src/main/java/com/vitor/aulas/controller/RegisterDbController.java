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

    private SQLiteDatabase db;





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

    public boolean cpfExiste(String cpf) {
        SQLiteDatabase db = rdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserData WHERE cpf = ?", new String[]{cpf});

        boolean cpfExiste = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return cpfExiste;
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

    public Usuario getUsuarioPorEmailETipo(String email, String tipo) {
        SQLiteDatabase db = rdb.getReadableDatabase();
        Usuario usuario = null;

        // Colunas que você quer de volta (exemplo)
        String[] colunas = {"email", "tipo","cpf", "password"};

        // WHERE email=? AND tipo=?
        String selecao = "email = ? AND tipo = ?";
        String[] argumentos = {email, tipo};

        Cursor cursor = db.query("UserData", // substitua pelo nome da sua tabela
                colunas,
                selecao,
                argumentos,
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Crie e preencha o objeto Usuario com os dados do cursor
            usuario = new Usuario();
            usuario.setCpf(cursor.getString(cursor.getColumnIndexOrThrow("cpf")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            usuario.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
            // ... adicione outros campos conforme necessário
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return usuario;
    }
}
