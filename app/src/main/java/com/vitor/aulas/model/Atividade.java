package com.vitor.aulas.model;

public class Atividade {
    private int id;
    private String titulo;
    private String descricao;
    private String alunoCpf;


    public Atividade() {}


    public Atividade(int id, String titulo, String descricao, String alunoCpf) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.alunoCpf = alunoCpf;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getAlunoCpf() { return alunoCpf; }
    public void setAlunoCpf(String alunoCpf) { this.alunoCpf = alunoCpf; }
}
