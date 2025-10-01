package com.vitor.aulas.model;

public class Professor {
    private int id;
    private String nome;
    private String cpf;
    private String email;

    public Professor(int id, String nome, String cpf, String email){
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
}

