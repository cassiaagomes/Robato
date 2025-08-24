package com.robato.diagnosticos.laudo;

import com.robato.diagnosticos.web.dto.ResultadoExameItem;

public class TextoFormato implements FormatoLaudo {

    @Override
    public String gerar(LaudoCompleto l) {
        // Usamos um StringBuilder para construir o texto de forma eficiente
        StringBuilder sb = new StringBuilder();

        sb.append("========================================\n");
        sb.append("===      ROBATO DIAGNÓSTICOS       ===\n");
        sb.append("========================================\n\n");
        sb.append("Exame Nº: ").append(l.getNumeroExame()).append("\n");
        sb.append("Paciente: ").append(l.getPaciente()).append("\n");
        sb.append("Convênio: ").append(l.getConvenio() == null ? "-" : l.getConvenio()).append("\n");
        sb.append("Médico Solicitante: ").append(l.getMedicoSolicitante()).append("\n");
        sb.append("Data: ").append(l.getData()).append("\n");
        
        sb.append("\n----------------------------------------\n");
        sb.append("             RESULTADOS\n");
        sb.append("----------------------------------------\n");

        // Verificamos se a lista de resultados não está vazia
        if (l.getResultados() != null && !l.getResultados().isEmpty()) {
            // Percorremos cada item da lista e o adicionamos ao texto
            for (ResultadoExameItem item : l.getResultados()) {
                sb.append(String.format("%-20s: %-10s %-15s (Ref: %s)\n",
                    item.getIndicador(),
                    item.getResultado(),
                    item.getUnidade(),
                    item.getValorReferencia()
                ));
            }
        } else {
            sb.append("Nenhum resultado detalhado fornecido.\n");
        }

        sb.append("\n----------------------------------------\n");
        sb.append("             OBSERVAÇÕES\n");
        sb.append("----------------------------------------\n");
        sb.append(l.getObservacoes()).append("\n\n");
        
        sb.append("_______________________________________\n");
        sb.append("Médico Responsável: ").append(l.getMedicoResponsavel()).append("\n");
        sb.append("CRM: ").append(l.getCrmResponsavel()).append("\n");

        return sb.toString();
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }
}