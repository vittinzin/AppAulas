package com.vitor.aulas.model;

public class Aluno {
    private int id;
    private String nome;
    private String cpf;
    private int turmaId;

    public Aluno(int id, String nome, String cpf, int turmaId) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.turmaId = turmaId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public int getTurmaId() { return turmaId; }
    public void setTurmaId(int turmaId) { this.turmaId = turmaId; }
}

