package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitor.aulas.model.BancoDeDados_Login;
import com.vitor.aulas.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DBController_Login {

    private final SQLiteDatabase db;

    // Nome da tabela e colunas
    public static final String TABELA_USUARIOS = "usuarios";
    public static final String COL_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_CPF = "cpf";
    public static final String COL_SENHA = "senha";
    public static final String COL_TIPO = "tipo";

    public DBController_Login(Context context) {
        BancoDeDados_Login banco = new BancoDeDados_Login(context);
        this.db = banco.getWritableDatabase();
    }

    // --- CADASTRAR ---
    public long cadastrarUsuario(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put(COL_EMAIL, usuario.getEmail());
        valores.put(COL_CPF, usuario.getCpf());
        valores.put(COL_SENHA, usuario.getSenha());
        valores.put(COL_TIPO, usuario.getTipo());

        return db.insert(TABELA_USUARIOS, null, valores);
    }

    // --- LOGIN ---
    public Usuario autenticarUsuario(String cpf, String senha) {
        Usuario usuario = null;

        try (Cursor cursor = db.query(
                TABELA_USUARIOS,
                null,
                COL_CPF + "=? AND " + COL_SENHA + "=?",
                new String[]{cpf, senha},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                usuario = mapearUsuario(cursor);
            }
        }
        return usuario; // se for null = login inválido
    }

    // --- BUSCAR POR CPF ---
    public Usuario buscarUsuarioPorCpf(String cpf) {
        Usuario usuario = null;

        try (Cursor cursor = db.query(
                TABELA_USUARIOS,
                null,
                COL_CPF + "=?",
                new String[]{cpf},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                usuario = mapearUsuario(cursor);
            }
        }
        return usuario;
    }

    // --- LISTAR TODOS ---
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        try (Cursor cursor = db.query(
                TABELA_USUARIOS,
                null,
                null,
                null,
                null,
                null,
                COL_EMAIL + " ASC")) {

            while (cursor.moveToNext()) {
                lista.add(mapearUsuario(cursor));
            }
        }
        return lista;
    }

    // --- MAPEAMENTO ---
    private Usuario mapearUsuario(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
        String cpf = cursor.getString(cursor.getColumnIndexOrThrow(COL_CPF));
        String senha = cursor.getString(cursor.getColumnIndexOrThrow(COL_SENHA));
        String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO));

        return new Usuario(id, email, cpf, senha, tipo);
    }
}
