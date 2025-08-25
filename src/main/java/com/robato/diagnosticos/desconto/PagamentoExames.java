package com.robato.diagnosticos.desconto;

/**
 * Componente Concreto que implementa a operação base sem decorações.
 */
public class PagamentoExames implements CalculoPreco {

    private final double valorOriginal;

    public PagamentoExames(double valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    @Override
    public double calcularPrecoFinal() {
        return this.valorOriginal;
    }
}
