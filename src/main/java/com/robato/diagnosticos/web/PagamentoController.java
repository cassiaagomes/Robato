package com.robato.diagnosticos.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robato.diagnosticos.domain.Paciente;
import com.robato.diagnosticos.domain.Pagamento;
import com.robato.diagnosticos.service.PacienteService;
import com.robato.diagnosticos.service.PagamentoService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PagamentoController {

    private final PacienteService pacienteService;
    private final PagamentoService pagamentoService;

    public PagamentoController(PacienteService pacienteService, PagamentoService pagamentoService) {
        this.pacienteService = pacienteService;
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/pagamentos")
    public String mostrarFormularioPagamento(Model model, HttpServletRequest request) {
        List<Paciente> pacientes = pacienteService.listarPacientes();

        // Lista hardcoded (alternativa)
        List<String> tiposExame = Arrays.asList(
                "Hemograma", "Hemograma Completo", "Glicemia", "Ureia",
                "Creatinina", "TGO (AST)", "TGP (ALT)", "HDL", "LDL",
                "Triglicerídeos", "Potássio", "Sorologia", "RaioX", "Ressonancia Magnetica");

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("tiposExame", tiposExame);
        model.addAttribute("currentUri", request.getRequestURI());

        return "pagamento";
    }

    @PostMapping("/pagamentos/processar")
    public String processarPagamento(
            @RequestParam String pacienteNome,
            @RequestParam List<String> nomesExames,
            @RequestParam(required = false) Boolean comConvenio,
            @RequestParam(required = false) Boolean ehIdoso,
            Model model, HttpServletRequest request) {

        Paciente paciente = pacienteService.buscarPorNome(pacienteNome);
        if (paciente == null) {
            return "redirect:/pagamentos?error=Paciente não encontrado";
        }

        // Calcular valor total com descontos
        double valorTotal = pagamentoService.calcularValorTotal(nomesExames);
        double valorOriginal = valorTotal;

        // Aplicar descontos
        if (comConvenio != null && comConvenio) {
            valorTotal *= 0.85; // 15% desconto
        }
        if (ehIdoso != null && ehIdoso) {
            valorTotal *= 0.92; // 8% desconto
        }

        model.addAttribute("paciente", paciente);
        model.addAttribute("exames", nomesExames);
        model.addAttribute("valorOriginal", valorOriginal);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("comConvenio", comConvenio != null ? comConvenio : false);
        model.addAttribute("ehIdoso", ehIdoso != null ? ehIdoso : false);
        model.addAttribute("descontoAplicado", valorOriginal - valorTotal);
        model.addAttribute("currentUri", request.getRequestURI());

        return "selecionar-metodo-pagamento";
    }

    @PostMapping("/pagamentos/finalizar")
    public String finalizarPagamento(
            @RequestParam String pacienteNome,
            @RequestParam List<String> exames,
            @RequestParam Double valorTotal,
            @RequestParam String metodoPagamento,
            Model model, HttpServletRequest request) {

        Paciente paciente = pacienteService.buscarPorNome(pacienteNome);
        Pagamento pagamento = pagamentoService.registrarPagamento(paciente, exames, valorTotal, metodoPagamento);

        model.addAttribute("pagamento", pagamento);
        model.addAttribute("paciente", paciente);
        model.addAttribute("exames", exames);
        model.addAttribute("metodoPagamento", metodoPagamento);
        model.addAttribute("pagamentoService", pagamentoService);
        model.addAttribute("currentUri", request.getRequestURI());

        return "nota-fiscal";
    }
}