package com.robato.diagnosticos.web.dto;

import java.util.List;

public class PagamentoRequest {
    private Long pacienteId;
    private List<String> nomesExames;
    private boolean aplicaDescontoConvenio;
    private boolean aplicaDescontoIdoso;

    // Getters e Setters
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public List<String> getNomesExames() { return nomesExames; }
    public void setNomesExames(List<String> nomesExames) { this.nomesExames = nomesExames; }
    public boolean isAplicaDescontoConvenio() { return aplicaDescontoConvenio; }
    public void setAplicaDescontoConvenio(boolean aplicaDescontoConvenio) { this.aplicaDescontoConvenio = aplicaDescontoConvenio; }
    public boolean isAplicaDescontoIdoso() { return aplicaDescontoIdoso; }
    public void setAplicaDescontoIdoso(boolean aplicaDescontoIdoso) { this.aplicaDescontoIdoso = aplicaDescontoIdoso; }
}