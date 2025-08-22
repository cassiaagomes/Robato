package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public abstract class ValidadorBase implements ValidadorExame {
    private ValidadorExame proximo;

    public ValidadorExame setProximo(ValidadorExame p) {
        this.proximo = p;
        return p;
    }

    public void validar(ExameComponent exame, ValidacaoContexto contexto) {
        executar(exame, contexto);
        if (proximo != null)
            proximo.validar(exame, contexto);
    }

    protected abstract void executar(ExameComponent exame, ValidacaoContexto contexto);
}
