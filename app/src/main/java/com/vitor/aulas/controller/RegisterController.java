package com.vitor.aulas.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitor.aulas.model.Usuario;

public class RegisterController {
    private static final String prefs = "prefs";
    private static final String ITENS_KEY = "login";
    private SharedPreferences sp;

    public RegisterController(){
    }
    public RegisterController (Context context){
        sp = context.getSharedPreferences(prefs, Context.MODE_PRIVATE);
    }

    public int confirmRegister (Usuario register) {
        String[] registerInfos = new String[] {
                register.getCpf(), register.getEmail(), register.getSenha()
        };

        for (int i = 0; i < registerInfos.length; i++) {
            if (registerInfos[i] == null || registerInfos[i].trim().isEmpty()){
                return i;
            }
        }
        return -1;
    }


    public String getSavedName() {
        return sp.getString("name", null);
    }
}
