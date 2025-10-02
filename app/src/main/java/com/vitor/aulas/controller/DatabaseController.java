package com.vitor.aulas.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vitor.aulas.db.AlunoDb;
import com.vitor.aulas.model.Aluno;
import com.vitor.aulas.model.Atividade;
import com.vitor.aulas.model.Turma;
import com.vitor.aulas.model.Usuario;
import com.vitor.aulas.model.Professor;
import com.vitor.aulas.util.SenhaUtil;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private static final String TAG = "DatabaseController";
    private final AlunoDb alunoDb;
    private final SQLiteDatabase db;

    public DatabaseController(Context context) {
        this.alunoDb = new AlunoDb(context.getApplicationContext());
        this.db = alunoDb.getWritableDatabase(); // Abre DB uma vez e mantém
    }

    // ---------------- Generic helpers ----------------
    public long insert(String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    private boolean tableExists(String tableName) {
        boolean exists = false;
        try (Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName})) {
            exists = cursor.moveToFirst();
        } catch (Exception e) {
            Log.w(TAG, "Erro ao checar existência da tabela " + tableName + ": " + e.getMessage());
        }
        return exists;
    }

    // ---------------- Aluno ----------------
    public long insertAluno(Aluno aluno) {
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.NOME, aluno.getNome());
        cv.put(AlunoDb.CPF, aluno.getCpf());
        cv.put(AlunoDb.TURMA_ID, aluno.getTurmaId());
        return insert(AlunoDb.TABLE_ALUNO, cv);
    }

    public int updateAluno(Aluno aluno) {
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.NOME, aluno.getNome());
        cv.put(AlunoDb.CPF, aluno.getCpf());
        cv.put(AlunoDb.TURMA_ID, aluno.getTurmaId());
        return update(AlunoDb.TABLE_ALUNO, cv, AlunoDb.ID + " = ?", new String[]{String.valueOf(aluno.getId())});
    }

    public int deleteAluno(int id) {
        return delete(AlunoDb.TABLE_ALUNO, AlunoDb.ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Aluno getAlunoByCpf(String cpf) {
        Aluno aluno = null;
        try (Cursor cursor = db.query(
                AlunoDb.TABLE_ALUNO,
                new String[]{AlunoDb.ID, AlunoDb.NOME, AlunoDb.CPF, AlunoDb.TURMA_ID},
                AlunoDb.CPF + " = ?",
                new String[]{cpf}, null, null, null)) {
            if (cursor.moveToFirst()) {
                aluno = new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
                );
            }
        }
        return aluno;
    }

    public Aluno getAlunoById(int id) {
        Aluno aluno = null;
        try (Cursor cursor = db.query(
                AlunoDb.TABLE_ALUNO,
                new String[]{AlunoDb.ID, AlunoDb.NOME, AlunoDb.CPF, AlunoDb.TURMA_ID},
                AlunoDb.ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                aluno = new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
                );
            }
        }
        return aluno;
    }

    public List<Aluno> getAllAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        try (Cursor cursor = db.query(AlunoDb.TABLE_ALUNO, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                alunos.add(new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.CPF)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ID))
                ));
            }
        }
        return alunos;
    }

    // ---------------- Turma ----------------
    public long insertTurma(Turma turma) {
        ContentValues cv = new ContentValues();
        cv.put(AlunoDb.TURMA_NOME, turma.getNome());
        cv.put(AlunoDb.TURMA_ANO, turma.getAno());
        return insert(AlunoDb.TABLE_TURMA, cv);
    }

    public List<Turma> getAllTurmas() {
        List<Turma> turmas = new ArrayList<>();
        try (Cursor cursor = db.query(AlunoDb.TABLE_TURMA, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                turmas.add(new Turma(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.TURMA_ANO))
                ));
            }
        }
        return turmas;
    }

    // ---------------- Atividade ----------------
    public long insertAtividade(Atividade atividade) {
        if (tableExists("atividades")) {
            ContentValues cv = new ContentValues();
            cv.put("titulo", atividade.getTitulo());
            cv.put("descricao", atividade.getDescricao());
            cv.put("aluno_cpf", atividade.getAlunoCpf());
            return insert("atividades", cv);
        } else {
            Aluno aluno = getAlunoByCpf(atividade.getAlunoCpf());
            if (aluno == null) return -1;
            ContentValues cv = new ContentValues();
            cv.put(AlunoDb.ATIVIDADE_TITULO, atividade.getTitulo());
            cv.put(AlunoDb.ATIVIDADE_DESC, atividade.getDescricao());
            cv.put(AlunoDb.ALUNO_ID, aluno.getId());
            return insert(AlunoDb.TABLE_ATIVIDADE, cv);
        }
    }

    public List<Atividade> getAtividadesDoAluno(String alunoCpf) {
        List<Atividade> lista = new ArrayList<>();
        if (tableExists("atividades")) {
            try (Cursor cursor = db.rawQuery(
                    "SELECT * FROM atividades WHERE aluno_cpf = ?",
                    new String[]{alunoCpf})) {
                while (cursor.moveToNext()) {
                    Atividade a = new Atividade();
                    a.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    a.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                    a.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                    a.setAlunoCpf(alunoCpf);
                    lista.add(a);
                }
            }
        } else {
            Aluno aluno = getAlunoByCpf(alunoCpf);
            if (aluno == null) return lista;
            try (Cursor cursor = db.query(AlunoDb.TABLE_ATIVIDADE, null,
                    AlunoDb.ALUNO_ID + " = ?", new String[]{String.valueOf(aluno.getId())},
                    null, null, null)) {
                while (cursor.moveToNext()) {
                    Atividade a = new Atividade();
                    a.setId(cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)));
                    a.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.ATIVIDADE_TITULO)));
                    a.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.ATIVIDADE_DESC)));
                    a.setAlunoCpf(alunoCpf);
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    // ---------------- Usuario ----------------
    public void insertUsuario(Usuario usuario) {
        ContentValues data = new ContentValues();
        data.put(AlunoDb.USER_EMAIL, usuario.getEmail());
        data.put(AlunoDb.USER_CPF, usuario.getCpf());
        data.put(AlunoDb.USER_PASSWORD, SenhaUtil.hashPassword(usuario.getSenha()));
        data.put(AlunoDb.USER_TIPO, usuario.getTipo());
        insert(AlunoDb.TABLE_USER, data);
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Cursor cursor = db.query(AlunoDb.TABLE_USER,
                new String[]{AlunoDb.USER_EMAIL, AlunoDb.USER_CPF, AlunoDb.USER_PASSWORD, AlunoDb.USER_TIPO},
                null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                usuarios.add(new Usuario(
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.USER_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.USER_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.USER_TIPO))
                ));
            }
        }
        return usuarios;
    }

    public boolean verificaUsuarioPorEmail(String email) {
        boolean exists = false;
        try (Cursor cursor = db.query(AlunoDb.TABLE_USER, new String[]{AlunoDb.USER_EMAIL},
                AlunoDb.USER_EMAIL + " = ?", new String[]{email}, null, null, null)) {
            exists = cursor.moveToFirst();
        }
        return exists;
    }

    public boolean redefinirSenha(String email, String novaSenha) {
        ContentValues values = new ContentValues();
        values.put(AlunoDb.USER_PASSWORD, SenhaUtil.hashPassword(novaSenha));
        int rows = update(AlunoDb.TABLE_USER, values, AlunoDb.USER_EMAIL + " = ?", new String[]{email});
        return rows > 0;
    }

    // ---------------- Professor ----------------
    public long insertProfessor(Professor professor) {
        ContentValues values = new ContentValues();
        values.put(AlunoDb.PROFESSOR_NOME, professor.getNome());
        values.put(AlunoDb.PROFESSOR_CPF, professor.getCpf());
        values.put(AlunoDb.PROFESSOR_EMAIL, professor.getEmail());
        return insert(AlunoDb.TABLE_PROFESSOR, values);
    }

    public List<Professor> getAllProfessores() {
        List<Professor> professores = new ArrayList<>();
        try (Cursor cursor = db.query(AlunoDb.TABLE_PROFESSOR,
                new String[]{AlunoDb.ID, AlunoDb.PROFESSOR_NOME, AlunoDb.PROFESSOR_CPF, AlunoDb.PROFESSOR_EMAIL},
                null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                professores.add(new Professor(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AlunoDb.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.PROFESSOR_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.PROFESSOR_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AlunoDb.PROFESSOR_EMAIL))
                ));
            }
        }
        return professores;
    }

    // ---------------- Close ----------------
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        alunoDb.close();
    }
}