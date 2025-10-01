package com.vitor.aulas.model;

public class Turma {
    private int id;
    private String nome;
    private String ano;

    public Turma(int id, String nome, String ano) {
        this.id = id;
        this.nome = nome;
        this.ano = ano;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAno() { return ano; }
    public void setAno(String ano) { this.ano = ano; }
}
