package com.robato.diagnosticos.web.dto;

public class ResultadoExameItem {

    private String indicador;
    private String resultado;
    private String unidade;
    private String referencia;

    public ResultadoExameItem() {}

    public ResultadoExameItem(String indicador, String resultado, String unidade, String referencia) {
        this.indicador = indicador;
        this.resultado = resultado;
        this.unidade = unidade;
        this.referencia = referencia;
    }

    public String getIndicador() { return indicador; }
    public void setIndicador(String indicador) { this.indicador = indicador; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}