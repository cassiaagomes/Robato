package com.robato.diagnosticos.laudo;

import com.robato.diagnosticos.web.dto.ResultadoExameItem;

public class HtmlFormato implements FormatoLaudo {

    @Override
    public String gerar(LaudoCompleto l) {
        StringBuilder sb = new StringBuilder();

        // Início do HTML com CSS embutido para uma boa aparência
        sb.append("<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"UTF-8\"><title>Laudo Médico</title><style>");
        sb.append("body { font-family: sans-serif; background-color: #f4f4f9; margin: 0; padding: 20px; }");
        sb.append(".container { max-width: 800px; margin: auto; background: white; padding: 40px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        sb.append("header { text-align: center; border-bottom: 2px solid #0A2342; padding-bottom: 15px; margin-bottom: 20px; }");
        sb.append("header h1 { color: #0A2342; margin: 0; }");
        sb.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        sb.append("th, td { text-align: left; padding: 8px; border-bottom: 1px solid #ddd; }");
        sb.append("th { color: #555; }");
        sb.append("footer { text-align: center; margin-top: 40px; padding-top: 20px; border-top: 1px solid #eee; }");
        sb.append("</style></head><body>");

        sb.append("<div class='container'>");
        
        // Cabeçalho
        sb.append("<header><h1>Robato Diagnósticos</h1></header>");
        sb.append("<p><b>Exame Nº:</b> ").append(l.getNumeroExame()).append("</p>");
        sb.append("<p><b>Paciente:</b> ").append(l.getPaciente()).append(" | <b>Convênio:</b> ").append(l.getConvenio() == null ? "-" : l.getConvenio()).append("</p>");
        sb.append("<p><b>Médico Solicitante:</b> ").append(l.getMedicoSolicitante()).append("</p>");
        sb.append("<p><b>Data:</b> ").append(l.getData()).append("</p><hr/>");

        // Tabela de Resultados
        sb.append("<h2>Resultados do Exame</h2>");
        sb.append("<table><thead><tr><th>Exame</th><th>Resultado</th><th>Unidades</th><th>Valores de Referência</th></tr></thead><tbody>");
        
        if (l.getResultados() != null && !l.getResultados().isEmpty()) {
            for (ResultadoExameItem item : l.getResultados()) {
                sb.append("<tr>");
                sb.append("<td>").append(item.getIndicador()).append("</td>");
                sb.append("<td><b>").append(item.getResultado()).append("</b></td>");
                sb.append("<td>").append(item.getUnidade()).append("</td>");
                sb.append("<td>").append(item.getReferencia()).append("</td>");
                sb.append("</tr>");
            }
        } else {
            sb.append("<tr><td colspan='4'>Nenhum resultado detalhado fornecido.</td></tr>");
        }
        
        sb.append("</tbody></table>");

        // Observações
        sb.append("<h2>Observações</h2>");
        sb.append("<p>").append(l.getObservacoes()).append("</p><hr/>");
        
        // Rodapé
        sb.append("<footer>");
        sb.append("<p><b>Responsável:</b> ").append(l.getMedicoResponsavel()).append(" (").append(l.getCrmResponsavel()).append(")</p>");
        sb.append("</footer>");

        sb.append("</div></body></html>");

        return sb.toString();
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }
}