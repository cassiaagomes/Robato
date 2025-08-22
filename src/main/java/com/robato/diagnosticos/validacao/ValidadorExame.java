package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public interface ValidadorExame {
    ValidadorExame setProximo(ValidadorExame proximo);

    void validar(ExameComponent exame, ValidacaoContexto contexto);
}
