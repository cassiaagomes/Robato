package com.robato.diagnosticos.validacao;

import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

public class ValidadorCreatinina extends ValidadorBase {
    protected void executar(ExameComponent exame, ValidacaoContexto ctx) {
        if (!"Sorologia".equalsIgnoreCase(exame.obterDescricao()))
            return;
        Double v = ctx.getValor();
        if (v == null)
            return;
        String sexo = ctx.getSexo();
        Integer idade = ctx.getIdade();
        String diag = "Referência: Homens 0,50-1,30 | Mulheres 0,40-1,10 mg/dL";
        if (idade != null && idade >= 60 && v > 1.5)
            diag += " | Alerta: Idoso > 1,5";
        if ("M".equalsIgnoreCase(sexo) && v > 1.2)
            diag += " | Alerta: Homem > 1,2";
        if ("F".equalsIgnoreCase(sexo) && v > 1.1)
            diag += " | Alerta: Mulher > 1,1";
        ctx.setDiagnostico(diag);
        ctx.setResultadoFormatado("CREATININA " + v + " mg/dL");
    }
}
