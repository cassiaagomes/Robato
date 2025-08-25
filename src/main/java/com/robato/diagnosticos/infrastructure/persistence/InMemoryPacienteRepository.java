package com.robato.diagnosticos.infrastructure.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.PacienteRepository;

@Repository
public class InMemoryPacienteRepository implements PacienteRepository {
    private final Map<Long, Paciente> pacientes = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    public InMemoryPacienteRepository() {

        salvar(new Paciente(null, "Pedro Henrique", "vhlr.ics@gmail.com", "83986301780", "Unimed", LocalDate.of(2002, 1, 18)));
        salvar(new Paciente(null, "Maria Santos (Idosa)", "maria@email.com", "11888888888", null, LocalDate.of(1950, 8, 20)));
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        if (paciente.getId() == null) {
            paciente.setId(contador.getAndIncrement());
        }
        pacientes.put(paciente.getId(), paciente);
        return paciente;
    }

    @Override
    public Optional<Paciente> buscarPorId(Long id) {
        return Optional.ofNullable(pacientes.get(id));
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

       @Override
    public Optional<Paciente> buscarPorNome(String nome) {
        return pacientes.values().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    @Override
    public boolean existePorNome(String nome) { 
        return pacientes.values().stream()
                .anyMatch(paciente -> paciente.getNome().equalsIgnoreCase(nome));
    }
}
