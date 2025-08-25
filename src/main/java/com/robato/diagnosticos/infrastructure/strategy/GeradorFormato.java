package com.robato.diagnosticos.infrastructure.strategy;

import com.robato.diagnosticos.application.dto.LaudoDTO;

public interface GeradorFormato {
    byte[] gerar(LaudoDTO laudo);
    String getMimeType();
}