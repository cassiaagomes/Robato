package com.robato.diagnosticos.exame;

import java.util.ArrayList;
import java.util.List;

public class HemogramaAgrupado implements ExameComponent {
    private final List<ExameComponent> itens = new ArrayList<>();

    public void adicionar(ExameComponent c) {
        itens.add(c);
    }

    public void remover(ExameComponent c) {
        itens.remove(c);
    }

    @Override
    public double calcularPreco() {
        return itens.stream().mapToDouble(ExameComponent::calcularPreco).sum();
    }

    @Override
    public String obterDescricao() {
        return "Hemograma Agrupado: " +
                itens.stream().map(ExameComponent::obterDescricao).toList();
    }
}
