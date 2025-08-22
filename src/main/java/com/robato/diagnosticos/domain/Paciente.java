package com.robato.diagnosticos.domain;

public class Paciente {

    private Long id; 
    private String nome;
    private String email;
    private String telefone;
    private String convenio;
    private String status;

    public Paciente() {
    }

    public Paciente(Long id, String nome, String email, String telefone, String convenio, String status) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.convenio = convenio;
        this.status = status;
    }

    // Getters e Setters para todos os campos
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getConvenio() {
        return convenio;
    }
    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}