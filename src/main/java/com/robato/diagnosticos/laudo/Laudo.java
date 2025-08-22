package com.robato.diagnosticos.laudo;

public class Laudo {
    private final FormatoLaudo formato;

    public Laudo(FormatoLaudo formato) {
        this.formato = formato;
    }

    public String gerarLaudo(LaudoCompleto d) {
        return formato.gerar(d);
    }

    public String getMimeType() {
        return formato.getMimeType();
    }
}
