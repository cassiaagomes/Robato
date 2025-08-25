package com.robato.diagnosticos.web.controller;

import com.robato.diagnosticos.application.facade.AtendimentoFacade;
import com.robato.diagnosticos.domain.factory.ExameFactory;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.PacienteRepository;
import com.robato.diagnosticos.web.dto.PedidoFilaRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoController {

    private final AtendimentoFacade atendimentoFacade;
    private final PacienteRepository pacienteRepository;

    public AtendimentoController(AtendimentoFacade atendimentoFacade, PacienteRepository pacienteRepository) {
        this.atendimentoFacade = atendimentoFacade;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/fila")
    public String verFila(Model model) {
        model.addAttribute("itensFila", atendimentoFacade.getFila().verFila());
        return "fila"; // Aponta para seu templates/fila.html
    }

    @PostMapping("/fila/adicionar")
    public String adicionarExameNaFila(@ModelAttribute PedidoFilaRequest request, RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = pacienteRepository.buscarPorId(request.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
            
            // A Factory cria o "molde" do exame
            var exame = ExameFactory.criar(request.getNomeExame());

            // A fachada orquestra a submissão para a fila
            atendimentoFacade.submeterPedidoParaFila(exame, paciente, request.getPrioridade());

            redirectAttributes.addFlashAttribute("sucesso", "Exame '" + request.getNomeExame() + "' adicionado à fila para o paciente " + paciente.getNome());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao adicionar exame: " + e.getMessage());
        }
        return "redirect:/pacientes"; // Volta para a tela de pacientes
    }
}
