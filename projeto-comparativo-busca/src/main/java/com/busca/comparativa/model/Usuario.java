package com.busca.comparativa.model;

public class Usuario {
    private final int id;
    private final String cpf;
    private final String nome;
    private final String email;

    public Usuario(int id, String cpf, String nome, String email) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
}