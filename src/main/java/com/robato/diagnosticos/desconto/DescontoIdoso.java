package com.robato.diagnosticos.desconto;

/**
 * Decorator Concreto que adiciona a responsabilidade de aplicar o desconto para idosos.
 */
public class DescontoIdoso extends DescontoDecorator {

    public DescontoIdoso(CalculoPreco calculoPreco) {
        super(calculoPreco);
    }

    @Override
    public double calcularPrecoFinal() {
        double precoCalculado = super.calcularPrecoFinal();
        // Aplica 8% de desconto
        return precoCalculado * 0.92;
    }
}
