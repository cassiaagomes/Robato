package com.robato.diagnosticos.laudo;

import com.robato.diagnosticos.web.dto.ResultadoExameItem;

public class PdfFormato implements FormatoLaudo {

    @Override
    public String gerar(LaudoCompleto l) {
        // Usamos um StringBuilder para construir o texto de forma eficiente
        StringBuilder sb = new StringBuilder();

        sb.append("--- LAUDO MÉDICO (Simulação de PDF) ---\n\n");
        sb.append("Exame Nº: ").append(l.getNumeroExame()).append("\n");
        sb.append("Paciente: ").append(l.getPaciente()).append("\n");
        sb.append("Convênio: ").append(l.getConvenio()).append("\n");
        sb.append("Médico Solicitante: ").append(l.getMedicoSolicitante()).append("\n");
        sb.append("Data: ").append(l.getData()).append("\n");
        
        sb.append("\n--- RESULTADOS DO EXAME ---\n");
        if (l.getResultados() != null && !l.getResultados().isEmpty()) {
            for (ResultadoExameItem item : l.getResultados()) {
                sb.append(String.format("%-25s %10s %-15s (Ref: %s)\n",
                    item.getIndicador() + ":",
                    item.getResultado(),
                    item.getUnidade(),
                    item.getValorReferencia()
                ));
            }
        } else {
            sb.append("Nenhum resultado detalhado fornecido.\n");
        }

        sb.append("\n--- OBSERVAÇÕES ---\n");
        sb.append(l.getObservacoes()).append("\n\n");
        
        sb.append("________________________________________\n");
        sb.append("Médico Responsável: ").append(l.getMedicoResponsavel()).append("\n");
        sb.append("CRM: ").append(l.getCrmResponsavel()).append("\n");

        return sb.toString();
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }
}