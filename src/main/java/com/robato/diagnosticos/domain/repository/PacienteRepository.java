package com.robato.diagnosticos.domain.repository;


import java.util.List;
import java.util.Optional;

import com.robato.diagnosticos.domain.model.Paciente;

public interface PacienteRepository {
    Paciente salvar(Paciente paciente);
    Optional<Paciente> buscarPorId(Long id);
    Optional<Paciente> buscarPorNome(String nome);
    boolean existePorNome(String nome); 
    List<Paciente> listarTodos();

}