package com.robato.diagnosticos.web.dto;

import java.util.List; // Import necessário para a lista

public class ValidacaoContexto {

    // --- Atributos existentes ---
    private Boolean pacienteComImplantesMetalicos;
    private Double valor;
    private String unidade;
    private Integer idade;
    private String sexo;
    private Boolean hasImagem;
    private String radiologistaAssinatura;
    private String protocolo;
    private Boolean usouContraste;
    private String contraste;
    private String doseContraste;
    private String resultadoFormatado;
    private String diagnostico;

    // --- Novo atributo para a lista de resultados (ex: hemograma) ---
    private List<ResultadoExameItem> resultados;


    // --- Getters e Setters ---

    public Boolean getPacienteComImplantesMetalicos() {
        return pacienteComImplantesMetalicos;
    }
    public void setPacienteComImplantesMetalicos(Boolean pacienteComImplantesMetalicos) {
        this.pacienteComImplantesMetalicos = pacienteComImplantesMetalicos;
    }

    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Integer getIdade() {
        return idade;
    }
    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Boolean getHasImagem() {
        return hasImagem;
    }
    public void setHasImagem(Boolean hasImagem) {
        this.hasImagem = hasImagem;
    }

    public String getRadiologistaAssinatura() {
        return radiologistaAssinatura;
    }
    public void setRadiologistaAssinatura(String radiologistaAssinatura) {
        this.radiologistaAssinatura = radiologistaAssinatura;
    }

    public String getProtocolo() {
        return protocolo;
    }
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public Boolean getUsouContraste() {
        return usouContraste;
    }
    public void setUsouContraste(Boolean usouContraste) {
        this.usouContraste = usouContraste;
    }

    public String getContraste() {
        return contraste;
    }
    public void setContraste(String contraste) {
        this.contraste = contraste;
    }

    public String getDoseContraste() {
        return doseContraste;
    }
    public void setDoseContraste(String doseContraste) {
        this.doseContraste = doseContraste;
    }

    public String getResultadoFormatado() {
        return resultadoFormatado;
    }
    public void setResultadoFormatado(String resultadoFormatado) {
        this.resultadoFormatado = resultadoFormatado;
    }

    public String getDiagnostico() {
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    public List<ResultadoExameItem> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoExameItem> resultados) {
        this.resultados = resultados;
    }
}