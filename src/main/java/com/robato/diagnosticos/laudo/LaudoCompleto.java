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
    private List<ResultadoExameItem> resultados;
    private String observacoes;
    private String imagemBase64;

    public int getNumeroExame() {
        return numeroExame;
    }

    public void setNumeroExame(int numeroExame) {
        this.numeroExame = numeroExame;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public void setMedicoSolicitante(String medicoSolicitante) {
        this.medicoSolicitante = medicoSolicitante;
    }

    public String getMedicoResponsavel() {
        return medicoResponsavel;
    }

    public void setMedicoResponsavel(String medicoResponsavel) {
        this.medicoResponsavel = medicoResponsavel;
    }

    public String getCrmResponsavel() {
        return crmResponsavel;
    }

    public void setCrmResponsavel(String crmResponsavel) {
        this.crmResponsavel = crmResponsavel;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }

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