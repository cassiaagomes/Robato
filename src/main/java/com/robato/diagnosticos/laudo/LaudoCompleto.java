package com.robato.diagnosticos.laudo;

import java.time.LocalDate; // Import necessário
import java.util.List;

import com.robato.diagnosticos.web.dto.ResultadoExameItem; // Import necessário

public class LaudoCompleto {

    private int numeroExame;
    private String paciente;
    private String convenio;
    private String medicoSolicitante;
    private String medicoResponsavel;
    private String crmResponsavel;
    private LocalDate data;

    // =======================================================
    // ===== CAMPOS ANTIGOS REMOVIDOS =====
    // private String resultado;
    // private String diagnostico;
    // =======================================================

    // =======================================================
    // ===== NOVOS CAMPOS ADICIONADOS =====
    // =======================================================
    private List<ResultadoExameItem> resultados; // Para a tabela de resultados
    private String observacoes; // Para o texto de diagnóstico/observações

    
    // --- Getters e Setters ---

    public int getNumeroExame() { return numeroExame; }
    public void setNumeroExame(int numeroExame) { this.numeroExame = numeroExame; }

    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }

    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }

    public String getMedicoSolicitante() { return medicoSolicitante; }
    public void setMedicoSolicitante(String medicoSolicitante) { this.medicoSolicitante = medicoSolicitante; }

    public String getMedicoResponsavel() { return medicoResponsavel; }
    public void setMedicoResponsavel(String medicoResponsavel) { this.medicoResponsavel = medicoResponsavel; }

    public String getCrmResponsavel() { return crmResponsavel; }
    public void setCrmResponsavel(String crmResponsavel) { this.crmResponsavel = crmResponsavel; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    // =======================================================
    // ===== NOVOS GETTERS E SETTERS ADICIONADOS =====
    // =======================================================
    public List<ResultadoExameItem> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoExameItem> resultados) {
        this.resultados = resultados;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}