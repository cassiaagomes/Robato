package com.robato.diagnosticos.domain.model;

public class Medico {
    private Long id;
    private String nome;
    private String crm;
    
    public Medico(Long id, String nome, String crm) { this.id = id; this.nome = nome; this.crm = crm; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
}
