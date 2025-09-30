package com.vitor.aulas.model;

public class Usuario {
    private int id;         // ID autoincrement do banco
    private String email;   // e-mail
    private String cpf;     // CPF único
    private String senha;   // senha do usuário
    private String tipo;    // "ALUNO" ou "DOCENTE"

    // 🔹 Construtor completo (com ID - usado ao buscar no banco)
    public Usuario(int id, String email, String cpf, String senha, String tipo) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.tipo = tipo;
    }

    // 🔹 Construtor sem ID (usado ao cadastrar)
    public Usuario(String email, String cpf, String senha, String tipo) {
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
