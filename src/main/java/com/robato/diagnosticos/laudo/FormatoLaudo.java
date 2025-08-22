package com.robato.diagnosticos.laudo;

public interface FormatoLaudo {
    String gerar(LaudoCompleto laudo);

    String getMimeType();
}
