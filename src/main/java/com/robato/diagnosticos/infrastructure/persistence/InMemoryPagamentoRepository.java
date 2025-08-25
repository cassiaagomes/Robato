package com.robato.diagnosticos.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.robato.diagnosticos.domain.model.Pagamento;
import com.robato.diagnosticos.domain.repository.PagamentoRepository;

@Repository
public class InMemoryPagamentoRepository implements PagamentoRepository {

    private final Map<Long, Pagamento> pagamentos = new ConcurrentHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        if (pagamento.getId() == null) {
            pagamento.setId(contadorId.getAndIncrement());
        }
        pagamentos.put(pagamento.getId(), pagamento);
        return pagamento;
    }

    @Override
    public Optional<Pagamento> buscarPorId(Long id) {
        return Optional.ofNullable(pagamentos.get(id));
    }

    @Override
    public List<Pagamento> listarTodos() {
        return new ArrayList<>(pagamentos.values());
    }

}
