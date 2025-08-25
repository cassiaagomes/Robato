package com.robato.diagnosticos.infrastructure.strategy;

import java.nio.charset.StandardCharsets;

import com.robato.diagnosticos.application.dto.LaudoDTO;

public class GeradorPDF implements GeradorFormato {

    @Override
    public byte[] gerar(LaudoDTO laudo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("--- SIMULAÇÃO DE ARQUIVO PDF ---\n\n");
        sb.append("Este é um arquivo de texto que representa o conteúdo que seria\n");
        sb.append("renderizado em um PDF por uma biblioteca como iText ou OpenPDF.\n\n");
        sb.append("==================================================\n\n");
        
        sb.append("CABEÇALHO:\n").append(laudo.cabecalho()).append("\n\n");
        sb.append("CORPO DO LAUDO:\n").append(laudo.corpo()).append("\n");
        sb.append("RODAPÉ:\n").append(laudo.rodape()).append("\n");
        
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }
}
