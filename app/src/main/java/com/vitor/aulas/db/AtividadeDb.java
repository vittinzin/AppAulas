package com.vitor.aulas.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AtividadeDb extends SQLiteOpenHelper {

    public static final String DB_NAME = "AtividadesDB";
    public static final int VERSION = 1;
    public static final String TABLE = "Atividades";

    public static final String ID = "id";
    public static final String TITULO = "titulo";
    public static final String DESCRICAO = "descricao";
    public static final String ALUNO_ID = "aluno_id";

    public AtividadeDb(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITULO + " TEXT, " +
                DESCRICAO + " TEXT, " +
                ALUNO_ID + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}

