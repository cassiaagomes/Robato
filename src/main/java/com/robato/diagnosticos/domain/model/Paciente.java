package com.robato.diagnosticos.domain.model;

import java.time.LocalDate;
import java.time.Period;

public class Paciente {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String convenio;
    private LocalDate dataNascimento;

    // Construtores, Getters e Setters
    public Paciente(Long id, String nome, String email, String telefone, String convenio, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.convenio = convenio;
        this.dataNascimento = dataNascimento;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public boolean isIdoso() {
        if (this.dataNascimento == null) return false;
        return Period.between(this.dataNascimento, LocalDate.now()).getYears() >= 60;
    }
}