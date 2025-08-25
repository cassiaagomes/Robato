package com.robato.diagnosticos.desconto;

/**
 * Decorator Concreto que adiciona a responsabilidade de aplicar o desconto de convênio.
 */
public class DescontoConvenio extends DescontoDecorator {
    
    private final double percentualDesconto = 0.15; // 15%

    public DescontoConvenio(CalculoPreco calculoPreco) {
        super(calculoPreco);
    }

    @Override
    public double calcularPrecoFinal() {
    
        double precoCalculado = super.calcularPrecoFinal(); 
        return precoCalculado * (1 - percentualDesconto);
    }
}