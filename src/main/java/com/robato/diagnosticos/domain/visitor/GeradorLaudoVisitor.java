package com.robato.diagnosticos.domain.visitor;

import java.util.Map;

import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.PacoteExames;

public class GeradorLaudoVisitor implements ExameVisitor {
    private final StringBuilder corpoLaudo = new StringBuilder();

    public String getCorpoDoLaudo() {
        return corpoLaudo.toString();
    }

    @Override
    public void visit(ExameSimples exame) {
        Map<String, Object> attrs = exame.getAtributos();
        String nomeExame = exame.getNome().toUpperCase();
        Object resultado = attrs.getOrDefault("resultado", "N/A");
        String unidade = (String) attrs.getOrDefault("unidade", "");

        corpoLaudo.append(String.format("%-25s: %s %s\n", nomeExame, resultado.toString(), unidade));

        if ("GLICOSE".equals(nomeExame)) {
            corpoLaudo.append("  Valores de Referência:\n")
                      .append("  - Normal: 60 a 99 mg/DL\n")
                      .append("  - Intolerância: 100 a 125 mg/DL\n")
                      .append("  - Diabetes: Acima de 125 mg/DL\n");
        }
    }

    @Override
    public void visit(PacoteExames pacote) {
        corpoLaudo.append("----------------------------------------\n");
        corpoLaudo.append("PACOTE DE EXAMES: ").append(pacote.getNome().toUpperCase()).append("\n");
        corpoLaudo.append("----------------------------------------\n\n");
        
        pacote.getItens().forEach(item -> {
            item.accept(this);
            corpoLaudo.append("\n");
        });
    }
}