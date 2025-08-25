package com.robato.diagnosticos.desconto;

/**
 * Decorator Abstrato que mantém uma referência para o objeto Component
 * e encaminha as requisições para ele.
 */
public abstract class DescontoDecorator implements CalculoPreco {
    
    protected CalculoPreco calculoPreco;

    public DescontoDecorator(CalculoPreco calculoPreco) {
        this.calculoPreco = calculoPreco;
    }

    @Override
    public double calcularPrecoFinal() {
        return calculoPreco.calcularPrecoFinal();
    }
}
