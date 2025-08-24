package com.robato.diagnosticos.desconto;

public class DescontoOutubroRosa implements Desconto {
    public double aplicar(double v) {
        return v * 0.95;
    }
}
