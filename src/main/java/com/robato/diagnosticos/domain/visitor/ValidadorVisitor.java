package com.robato.diagnosticos.domain.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.PacoteExames;

public class ValidadorVisitor implements ExameVisitor {
    private final List<String> erros = new ArrayList<>();

    public boolean isValido() {
        return erros.isEmpty();
    }

    public List<String> getErros() {
        return new ArrayList<>(erros);
    }

    @Override
    public void visit(ExameSimples exame) {
        switch (exame.getNome().toUpperCase()) {
            case "GLICOSE":
                validarGlicose(exame.getAtributos());
                break;
            case "RAIO-X":
                validarRaioX(exame.getAtributos());
                break;
        }
    }

    @Override
    public void visit(PacoteExames pacote) {
        pacote.getItens().forEach(item -> item.accept(this));
    }

    private void validarGlicose(Map<String, Object> atributos) {
        Object valorObj = atributos.get("resultado");
        if (!(valorObj instanceof Number)) {
            erros.add("GLICOSE: O resultado do exame deve ser um valor numérico.");
            return;
        }
        double valor = ((Number) valorObj).doubleValue();
        if (valor > 125) {
            erros.add("GLICOSE: Valor crítico detectado (" + valor + " mg/DL), indicativo de Diabetes.");
        }
    }

    private void validarRaioX(Map<String, Object> atributos) {
        Object assinaturaObj = atributos.get("assinatura");
        if (!(assinaturaObj instanceof String) || ((String) assinaturaObj).isBlank()) {
            erros.add("RAIO-X: A assinatura do médico radiologista é obrigatória para a emissão do laudo.");
        }
    }
}