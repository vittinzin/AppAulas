package com.vitor.aulas.model;

public class Usuario {
    private String email;
    private String cpf;
    private String senha;
    private String tipo;

    public Usuario(String email, String cpf, String senha, String tipo) {
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getEmail() { return email; }

    public String getCpf() { return cpf; }

    public String getSenha() { return senha; }

    public String getTipo() { return tipo; }
}
