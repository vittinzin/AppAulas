package com.vitor.aulas.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.vitor.aulas.model.Usuario;
import java.util.List;

public class RegisterController {
    private static final String PREFS = "prefs";
    private static final String ITENS_KEY = "login";

    private SharedPreferences sp;
    private DatabaseController dbController;

    // Construtor sem contexto (não recomendado para DB)
    public RegisterController() {}

    // Construtor com contexto
    public RegisterController(Context context) {
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        dbController = new DatabaseController(context); // instancia DatabaseController
    }

    // Validação dos campos do registro
    public int confirmRegister(Usuario register) {
        String[] registerInfos = new String[] {
                register.getCpf(),
                register.getEmail(),
                register.getSenha()
        };

        for (int i = 0; i < registerInfos.length; i++) {
            if (registerInfos[i] == null || registerInfos[i].trim().isEmpty()) {
                return i; // retorna índice do campo inválido
            }
        }
        return -1; // todos os campos válidos
    }

    // Salvar usuário no banco
    public void saveUsuario(Usuario usuario) {
        if (dbController != null) {
            dbController.insertUsuario(usuario);
        }
    }

    // Recuperar todos usuários
    public List<Usuario> getUsuarios() {
        if (dbController != null) {
            return dbController.getAllUsuarios();
        }
        return null;
    }

    // Fechar controller e liberar DB
    public void close() {
        if (dbController != null) {
            dbController.close();
        }
    }

    // SharedPreferences
    public String getSavedName() {
        return sp != null ? sp.getString("name", null) : null;
    }

    public void saveName(String name) {
        if (sp != null) {
            sp.edit().putString("name", name).apply();
        }
    }
}
