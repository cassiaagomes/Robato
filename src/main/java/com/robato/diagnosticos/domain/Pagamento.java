package com.robato.diagnosticos.domain;

import java.time.LocalDateTime;


public class Pagamento {
    private Long id;

    private Paciente paciente;
    
    private String examesRealizados;
    private Double valorTotal;
    private String metodoPagamento;
    private LocalDateTime dataHora;

    public Pagamento() {}

    public Long getId() {
        return id;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public String getExamesRealizados() {
        return examesRealizados;
    }
    public Double getValorTotal() {
        return valorTotal;
    }
    public String getMetodoPagamento() {
        return metodoPagamento;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    public void setExamesRealizados(String examesRealizados) {
        this.examesRealizados = examesRealizados;
    }
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
}