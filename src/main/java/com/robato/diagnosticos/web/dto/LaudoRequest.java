package com.robato.diagnosticos.web.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class LaudoRequest {

    @NotBlank
    private Long pacienteId;
    private String tipoExame;
    private String formato = "txt";
    private String pacienteNome;
    private String convenio;
    private String medicoSolicitante;
    private String medicoResponsavel;
    private String crmResponsavel;
    private String emailPaciente;
    private String telefonePaciente;
    private ValidacaoContexto contexto;

    private List<String> subExamesSelecionados;

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getTipoExame() {
        return tipoExame;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getTelefonePaciente() {
        return telefonePaciente;
    }

    public void setTelefonePaciente(String telefonePaciente) {
        this.telefonePaciente = telefonePaciente;
    }

    public void setTipoExame(String t) {
        this.tipoExame = t;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String f) {
        this.formato = f;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String p) {
        this.pacienteNome = p;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String c) {
        this.convenio = c;
    }

    public String getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public void setMedicoSolicitante(String m) {
        this.medicoSolicitante = m;
    }

    public String getMedicoResponsavel() {
        return medicoResponsavel;
    }

    public void setMedicoResponsavel(String m) {
        this.medicoResponsavel = m;
    }

    public String getCrmResponsavel() {
        return crmResponsavel;
    }

    public void setCrmResponsavel(String c) {
        this.crmResponsavel = c;
    }

    public ValidacaoContexto getContexto() {
        return contexto;
    }

    public void setContexto(ValidacaoContexto ctx) {
        this.contexto = ctx;
    }

    public List<String> getSubExamesSelecionados() {
        return subExamesSelecionados;
    }

    public void setSubExamesSelecionados(List<String> subExamesSelecionados) {
        this.subExamesSelecionados = subExamesSelecionados;
    }
}