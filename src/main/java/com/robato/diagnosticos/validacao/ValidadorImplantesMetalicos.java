package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.exame.RessonanciaMagnetica;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorImplantesMetalicos extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto ctx) {
        if (exame instanceof RessonanciaMagnetica && Boolean.TRUE.equals(ctx.getPacienteComImplantesMetalicos()))
            throw new IllegalStateException("Paciente com implantes não pode realizar ressonância.");
    }
}
