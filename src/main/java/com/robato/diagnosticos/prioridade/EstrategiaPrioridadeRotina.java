package com.robato.diagnosticos.prioridade;

public class EstrategiaPrioridadeRotina implements EstrategiaPrioridade {
    public int determinarPrioridade(String tipoExame) {
        return switch (tipoExame.toUpperCase()) {
            case "RESSONANCIA" -> 1;
            case "RAIOX" -> 2;
            default -> 3;
        };
    }
}
