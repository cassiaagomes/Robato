package com.robato.diagnosticos.web.dto;

public class ResultadoExameItem {
    private String nomeExame;
    private String valor;
    private String unidade;
    private String valorReferencia;
    private String indicador;

    public ResultadoExameItem() {
    }

    public ResultadoExameItem(String nomeExame, String valor, String unidade, String valorReferencia,
            String indicador) {
        this.nomeExame = nomeExame;
        this.valor = valor;
        this.unidade = unidade;
        this.valorReferencia = valorReferencia;
        this.indicador = indicador;
    }

    // Getters e Setters
    public String getNomeExame() {
        return nomeExame;
    }

    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getValorReferencia() {
        return valorReferencia;
    }

    public void setValorReferencia(String valorReferencia) {
        this.valorReferencia = valorReferencia;
    }

    @Override
    public String toString() {
        return "ResultadoExameItem{" +
                "nomeExame='" + nomeExame + '\'' +
                ", valor='" + valor + '\'' +
                ", unidade='" + unidade + '\'' +
                ", valorReferencia='" + valorReferencia + '\'' +
                '}';
    }

    public String getIndicador() {
        return indicador;
    }

    public String getResultado() {
        return valor;
    }
}