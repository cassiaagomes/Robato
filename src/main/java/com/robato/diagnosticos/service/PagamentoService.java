package com.robato.diagnosticos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.robato.diagnosticos.domain.Paciente;
import com.robato.diagnosticos.domain.Pagamento;

@Service
public class PagamentoService {

    private final Map<Long, Pagamento> pagamentos = new ConcurrentHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    
    private static final Map<String, Double> PRECOS_EXAMES = Map.ofEntries(
        Map.entry("Hemograma", 30.0),
        Map.entry("Hemograma Completo", 70.0),
        Map.entry("Glicemia", 25.0),
        Map.entry("Ureia", 50.0),
        Map.entry("Creatinina", 70.0),
        Map.entry("TGO (AST)", 50.0),
        Map.entry("TGP (ALT)", 50.0),
        Map.entry("HDL", 50.0),
        Map.entry("LDL", 50.0),
        Map.entry("Triglicerídeos", 50.0),
        Map.entry("Potássio", 50.0),
        Map.entry("Sorologia", 35.0),
        Map.entry("RaioX", 90.0),
        Map.entry("Ressonancia Magnetica", 450.0)
    );

    public PagamentoService() {
    }

    public double calcularValorTotal(List<String> nomesExames) {
        return nomesExames.stream()
                .mapToDouble(exame -> PRECOS_EXAMES.getOrDefault(exame, 0.0))
                .sum();
    }

    public Pagamento registrarPagamento(Paciente paciente, List<String> exames, double valorTotal, String metodoPagamento) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(contadorId.getAndIncrement());
        pagamento.setPaciente(paciente);
        pagamento.setExamesRealizados(String.join(", ", exames));
        pagamento.setValorTotal(valorTotal);
        pagamento.setMetodoPagamento(metodoPagamento);
        pagamento.setDataHora(LocalDateTime.now());

        pagamentos.put(pagamento.getId(), pagamento);

        return pagamento;
    }

    public List<Pagamento> listarTodosPagamentos() {
        return new ArrayList<>(pagamentos.values());
    }

    public Pagamento buscarPorId(Long id) {
        return pagamentos.get(id);
    }

    public void excluirPagamento(Long id) {
        pagamentos.remove(id);
    }

    public double getPrecoExame(String nomeExame) {
        return PRECOS_EXAMES.getOrDefault(nomeExame, 0.0);
    }

    
}