package com.robato.diagnosticos.domain.repository;

import java.util.List;
import java.util.Optional;

import com.robato.diagnosticos.domain.model.Pagamento;

public interface PagamentoRepository {
    Pagamento salvar(Pagamento pagamento);
    Optional<Pagamento> buscarPorId(Long id);
    List<Pagamento> listarTodos();
}