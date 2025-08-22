package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorPrecoPositivo extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto contexto) {
        if (exame.calcularPreco() <= 0)
            throw new IllegalStateException("Preço do exame inválido.");
    }
}
