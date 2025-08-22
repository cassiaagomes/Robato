package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.exame.RaioX;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorRaioX extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto ctx) {
        if (!(exame instanceof RaioX))
            return;
        if (ctx.getRadiologistaAssinatura() == null || ctx.getRadiologistaAssinatura().isBlank())
            throw new IllegalStateException("Raio-X requer assinatura do radiologista.");
        if (ctx.getHasImagem() == null || !ctx.getHasImagem())
            throw new IllegalStateException("Raio-X requer imagem associada.");
        ctx.setResultadoFormatado("Raio-X: imagem avaliada");
        ctx.setDiagnostico("Laudo assinado por " + ctx.getRadiologistaAssinatura());
    }
}
