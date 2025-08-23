package com.robato.diagnosticos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.robato.diagnosticos.prioridade.EstrategiaPrioridade;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeRotina;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeUrgente;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadePoucoUrgente;
import com.robato.diagnosticos.prioridade.FilaDePrioridade;

@Service
public class FilaService {

    private final FilaDePrioridade fila;

    public FilaService() {
        this.fila = new FilaDePrioridade(new EstrategiaPrioridadeRotina());
    }

    public void adicionarExame(Long pacienteId, String pacienteNome, String tipoExame, String prioridadeStr) {
        EstrategiaPrioridade estrategia;

        switch (prioridadeStr.toUpperCase()) {
            case "URGENTE" -> estrategia = new EstrategiaPrioridadeUrgente();
            case "POUCO_URGENTE" -> estrategia = new EstrategiaPrioridadePoucoUrgente();
            default -> estrategia = new EstrategiaPrioridadeRotina();
        }
        fila.add(tipoExame, pacienteNome, estrategia);
    }

    public List<FilaDePrioridade.Item> listarExames() {
        return fila.listarExames();
    }

    public int tamanhoFila() {
        return fila.tamanho();
    }

    public FilaDePrioridade.Item proximoExame() {
        return fila.proximo();
    }
}
