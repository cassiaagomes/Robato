package com.robato.diagnosticos.prioridade;

public class EstrategiaPrioridadePoucoUrgente implements EstrategiaPrioridade {

    @Override
    public int determinarPrioridade(String tipoExame) {
        return 2; 
    }
}
