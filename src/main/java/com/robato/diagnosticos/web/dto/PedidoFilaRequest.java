package com.robato.diagnosticos.web.dto;

import com.robato.diagnosticos.domain.service.FilaDePrioridadeExames.Prioridade;

public class PedidoFilaRequest {
    private Long pacienteId;
    private String nomeExame;
    private Prioridade prioridade;

    // Getters e Setters
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public String getNomeExame() { return nomeExame; }
    public void setNomeExame(String nomeExame) { this.nomeExame = nomeExame; }
    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
}
