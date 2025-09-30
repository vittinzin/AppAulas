package com.vitor.aulas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RegisterDb extends SQLiteOpenHelper implements BaseColumns {

    public static final String DB_NAME = "Users";
    public static final String TABLE = "UserData";
    public static final String ID = "id";
    public static final String CPF = "cpf";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TIPO = "tipo";
    public static final int VERSION = 6;

    public RegisterDb(Context context){
        super(context, DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String DB = "CREATE TABLE " + RegisterDb.TABLE + " ( "
                + RegisterDb.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RegisterDb.EMAIL + " text, " +
                RegisterDb.CPF + " text, " +
                RegisterDb.PASSWORD + " text, " +
                RegisterDb.TIPO + " text)";

        sqLiteDatabase.execSQL(DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}