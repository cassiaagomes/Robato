package com.robato.diagnosticos.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.repository.MedicoRepository;

@Repository
public class InMemoryMedicoRepository implements MedicoRepository {

    private final Map<Long, Medico> medicos = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    public InMemoryMedicoRepository() {
        // Dados de exemplo para inicialização
        salvar(new Medico(null, "Dra. Ana Paula", "CRM-12345"));
        salvar(new Medico(null, "Dr. Carlos Henrique", "CRM-98765"));
    }

    @Override
    public Medico salvar(Medico medico) {
        if (medico.getId() == null) {
            medico.setId(contador.getAndIncrement());
        }
        medicos.put(medico.getId(), medico);
        return medico;
    }

    @Override
    public Optional<Medico> buscarPorId(Long id) {
        return Optional.ofNullable(medicos.get(id));
    }

    @Override
    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos.values());
    }

    @Override
    public boolean existePorCrm(String crm) { 
        return medicos.values().stream()
                .anyMatch(medico -> medico.getCrm().equalsIgnoreCase(crm));
    }

}
