package com.robato.diagnosticos.application.facade;


import org.springframework.stereotype.Service;

import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.service.FilaDePrioridadeExames;
import com.robato.diagnosticos.domain.service.FilaDePrioridadeExames.Prioridade;

/**
 * Fachada para o contexto de Atendimento.
 * Responsável por orquestrar os casos de uso de cadastro e enfileiramento de exames.
 */
@Service
public class AtendimentoFacade {

    private final FilaDePrioridadeExames fila;

    public AtendimentoFacade(FilaDePrioridadeExames fila) {
        this.fila = fila;
    }

    public void submeterPedidoParaFila(ItemExame pedido, Paciente paciente, Prioridade prioridade) {
        // A lógica de criação do ItemExame já foi feita pelo Controller usando a Factory.
        // A responsabilidade desta fachada é apenas orquestrar a adição à fila.
        fila.adicionar(pedido, prioridade);
    }
    
    public FilaDePrioridadeExames getFila() {
        return this.fila;
    }
}