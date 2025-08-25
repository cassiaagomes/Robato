package com.robato.diagnosticos.domain.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.robato.diagnosticos.domain.composite.ItemExame;

@Component
public class FilaDePrioridadeExames {

    public enum Prioridade {
        URGENTE,
        POUCO_URGENTE,
        ROTINA
    }

    public record ItemFila(long id, ItemExame exame, Prioridade prioridade) {}

    private final LinkedList<ItemFila> fila = new LinkedList<>();

    public void adicionar(ItemExame exame, Prioridade prioridade) {
        long id = GeradorIDSequencial.getInstance().proximoId();
        ItemFila item = new ItemFila(id, exame, prioridade);

        switch (prioridade) {
            case URGENTE:
                fila.addFirst(item);
                break;
            case POUCO_URGENTE:
                int indiceUltimoUrgente = -1;
                for (int i = 0; i < fila.size(); i++) {
                    if (fila.get(i).prioridade() == Prioridade.URGENTE) {
                        indiceUltimoUrgente = i;
                    }
                }
                fila.add(indiceUltimoUrgente + 1, item);
                break;
            case ROTINA:
                fila.addLast(item);
                break;
        }
    }

    public ItemFila proximo() {
        return fila.poll();
    }

    public List<ItemFila> verFila() {
        return List.copyOf(fila);
    }

    public int tamanho() {
        return fila.size();
    }
}
