package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorGlicose extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto ctx) {
        if (!"Glicemia".equalsIgnoreCase(exame.obterDescricao()))
            return;
        Double v = ctx.getValor();
        if (v == null)
            throw new IllegalArgumentException("Valor de glicose não informado.");
        String diag;
        if (v < 60)
            diag = "Hipoglicemia (<60 mg/dL)";
        else if (v >= 100 && v <= 125)
            diag = "Intolerância à glicose (100-125 mg/dL)";
        else if (v > 125)
            diag = "Diabetes (>125 mg/dL)";
        else
            diag = "Normal (60-99 mg/dL)";
        ctx.setDiagnostico(diag);
        ctx.setResultadoFormatado("GLICOSE " + v + " " + (ctx.getUnidade() != null ? ctx.getUnidade() : "mg/dL"));
    }
}
