package com.robato.diagnosticos.infrastructure.strategy;


import com.robato.diagnosticos.application.dto.LaudoDTO;
import java.nio.charset.StandardCharsets;

public class GeradorTXT implements GeradorFormato {

    @Override
    public byte[] gerar(LaudoDTO laudo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("*********************************************\n");
        sb.append("********** LAUDO MÉDICO     **********\n");
        sb.append("*********************************************\n\n");
        
        sb.append(laudo.cabecalho()).append("\n\n");
        
        sb.append("--- CORPO DO LAUDO ---\n");
        sb.append(laudo.corpo()).append("\n");
        
        sb.append("--- RODAPÉ ---\n");
        sb.append(laudo.rodape()).append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }
}