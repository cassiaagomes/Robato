package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.exame.RessonanciaMagnetica;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorRessonancia extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto ctx) {
        if (!(exame instanceof RessonanciaMagnetica))
            return;
        if (Boolean.TRUE.equals(ctx.getPacienteComImplantesMetalicos()))
            throw new IllegalStateException("Paciente com implantes não pode realizar ressonância.");
        if (ctx.getProtocolo() == null || ctx.getProtocolo().isBlank())
            throw new IllegalArgumentException("Ressonância requer protocolo.");
        if (ctx.getRadiologistaAssinatura() == null || ctx.getRadiologistaAssinatura().isBlank())
            throw new IllegalArgumentException("Ressonância requer assinatura do radiologista.");
        String diag = "Protocolo: " + ctx.getProtocolo();
        if (Boolean.TRUE.equals(ctx.getUsouContraste())) {
            if (ctx.getContraste() == null || ctx.getDoseContraste() == null)
                throw new IllegalArgumentException("Informe contraste e dosagem.");
            diag += " | Contraste: " + ctx.getContraste() + " (" + ctx.getDoseContraste() + ")";
        }
        diag += " | Assinado por " + ctx.getRadiologistaAssinatura();
        ctx.setResultadoFormatado("Ressonância Magnética – " + ctx.getProtocolo());
        ctx.setDiagnostico(diag);
    }
}
