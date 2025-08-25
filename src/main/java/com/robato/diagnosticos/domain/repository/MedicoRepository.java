package com.robato.diagnosticos.domain.repository;

import java.util.List;
import java.util.Optional;

import com.robato.diagnosticos.domain.model.Medico;

public interface MedicoRepository {
    Medico salvar(Medico medico);
    Optional<Medico> buscarPorId(Long id);
    List<Medico> listarTodos();
    boolean existePorCrm(String crm); 
}
