package com.robato.diagnosticos.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.exame.TipoExame;
import com.robato.diagnosticos.facade.SistemaDiagnosticoFacade;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PagamentoController {

    private final SistemaDiagnosticoFacade facade;

    public PagamentoController(SistemaDiagnosticoFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/pagamentos")
    public String listarPagamentos(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "pagamentos"; 
    }

    @GetMapping("/pagamentos/novo")
    public String novaTelaPagamento(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("pacientes", facade.listarPacientes().values());
        model.addAttribute("tiposExame", TipoExame.values());
        return "pagamento";
    }

    @PostMapping("/pagamentos/confirmar")
    public String confirmarPagamento(@RequestParam String pacienteNome,
            @RequestParam List<String> nomesExames,
            @RequestParam(required = false) boolean comConvenio,
            @RequestParam(required = false) boolean ehIdoso,
            RedirectAttributes redirectAttributes) {
        List<TipoExame> tiposExames = nomesExames.stream()
                .map(TipoExame::valueOf)
                .collect(Collectors.toList());
        double valorFinal = facade.calcularCustoTotal(tiposExames, comConvenio, ehIdoso);
        facade.registrarPagamento(pacienteNome, valorFinal);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento de R$ " + String.format("%.2f", valorFinal)
                + " para " + pacienteNome + " registrado com sucesso!");
        return "redirect:/dashboard"; 
    }
}