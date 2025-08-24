package com.robato.diagnosticos.domain;

public class Medico {
    private Long id;
    private String nome;
    private String crm;
    private String especialidade;
    private String email;
    private String telefone;
    
    // Construtores
    public Medico() {}
    
    public Medico(Long id, String nome, String crm, String especialidade, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.email = email;
        this.telefone = telefone;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
