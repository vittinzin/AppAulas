package com.vitor.aulas.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfessorDb extends SQLiteOpenHelper {

    public static final String DB_NAME = "ProfessoresDB";
    public static final int VERSION = 2;
    public static final String TABLE = "Professores";

    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String CPF = "cpf";
    public static final String EMAIL = "email";

    public ProfessorDb(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME + " TEXT, " +
                CPF + " TEXT, " +
                EMAIL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
