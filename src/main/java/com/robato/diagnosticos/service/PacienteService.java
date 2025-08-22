package com.robato.diagnosticos.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.robato.diagnosticos.domain.Paciente;

@Service
public class PacienteService {

    private final Map<Long, Paciente> pacientes = new LinkedHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    public Collection<Paciente> listarPacientes() {
        return pacientes.values();
    }

    public Paciente adicionarPaciente(Paciente paciente) {
        long id = contador.getAndIncrement();
        paciente.setId(id);
        pacientes.put(id, paciente);
        return paciente;
    }

    public Paciente buscarPorId(Long id) {
        return pacientes.get(id);
    }   

    public Paciente atualizarPaciente(Paciente pacienteAtualizado) {
        if (pacienteAtualizado.getId() == null || !pacientes.containsKey(pacienteAtualizado.getId())) {
            throw new IllegalArgumentException("Paciente com ID inválido ou não encontrado.");
        }
        pacientes.put(pacienteAtualizado.getId(), pacienteAtualizado);
        return pacienteAtualizado;
    }

    public Paciente excluirPaciente(Long id) {
        return pacientes.remove(id);
    }
}
