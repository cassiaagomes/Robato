package com.robato.diagnosticos.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.robato.diagnosticos.application.facade.PagamentoFacade;
import com.robato.diagnosticos.domain.composite.PacoteExames;
import com.robato.diagnosticos.domain.factory.ExameFactory;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.PacienteRepository;
import com.robato.diagnosticos.web.dto.PagamentoRequest;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoFacade pagamentoFacade;
    private final PacienteRepository pacienteRepository;

    public PagamentoController(PagamentoFacade pagamentoFacade, PacienteRepository pacienteRepository) {
        this.pagamentoFacade = pagamentoFacade;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public String formularioPagamento(Model model) {
        model.addAttribute("faturamentoRequest", new PagamentoRequest());
        model.addAttribute("pacientes", pacienteRepository.listarTodos());
        return "pagamento";
    }

    @PostMapping("/processar")
    public String processarPagamento(@ModelAttribute PagamentoRequest request, Model model) {
        Paciente paciente = pacienteRepository.buscarPorId(request.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));

        PacoteExames pedido = new PacoteExames("Pedido de Faturamento");
        request.getNomesExames().forEach(nomeExame -> {
            pedido.adicionar(ExameFactory.criar(nomeExame));
        });

        var comprovante = pagamentoFacade.gerarComprovante(
            paciente,
            pedido,
            request.isAplicaDescontoConvenio(),
            request.isAplicaDescontoIdoso()
        );

        model.addAttribute("comprovante", comprovante);
        return "nota-fiscal";
    }
}
