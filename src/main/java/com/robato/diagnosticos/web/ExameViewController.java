package com.robato.diagnosticos.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.exame.TipoExame;
import com.robato.diagnosticos.facade.SistemaDiagnosticoFacade;
import com.robato.diagnosticos.laudo.LaudoCompleto;
import com.robato.diagnosticos.web.dto.LaudoRequest;
import com.robato.diagnosticos.web.dto.ResultadoExameItem;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExameViewController {

    private final SistemaDiagnosticoFacade facade;

    public ExameViewController(SistemaDiagnosticoFacade facade) {
        this.facade = facade;
    }

    @GetMapping({ "/", "/dashboard" })
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("laudosNoMes", 78);
        stats.put("pacientesEmFila", facade.tamanhoFila());
        stats.put("totalPacientes", facade.listarPacientes().size());
        stats.put("medicosAtivos", facade.listarMedicos().size());
        model.addAttribute("stats", stats);
        model.addAttribute("tipos", TipoExame.values());
        return "dashboard";
    }

    @GetMapping("/exames/novo")
    public String novo(Model model, @ModelAttribute("laudoRequest") LaudoRequest laudoRequest) {
        if (laudoRequest == null || laudoRequest.getTipoExame() == null) {
            model.addAttribute("laudoRequest", new LaudoRequest());
        }
        model.addAttribute("tipos", TipoExame.values());
        return "exames";
    }

    @GetMapping("/exames/importar-pagina")
    public String paginaImportar() {
        return "importar";
    }

    @PostMapping("/exames/importar")
    public String processarCsv(@RequestParam("csvFile") MultipartFile file, RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Por favor, selecione um arquivo CSV.");
            return "redirect:/exames/importar-pagina";
        }
        try {
            LaudoRequest dadosDoCsv = facade.parseCsvParaLaudoRequest(file.getInputStream());
            session.setAttribute("resultadosDoCsv", dadosDoCsv.getContexto().getResultados());
            redirectAttributes.addFlashAttribute("sucesso",
                    "Resultados do CSV importados! Preencha os dados do paciente e gere o laudo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o CSV: " + e.getMessage());
        }
        return "redirect:/exames/novo";
    }

    @PostMapping("/exames/gerar")
    public String gerarLaudo(@ModelAttribute LaudoRequest req, Model model, HttpSession session) {
        try {
            @SuppressWarnings("unchecked")
            List<ResultadoExameItem> resultadosSalvos = (List<ResultadoExameItem>) session
                    .getAttribute("resultadosDoCsv");

            if (req.getContexto() == null) {
                req.setContexto(new ValidacaoContexto());
            }
            if (resultadosSalvos != null) {
                req.getContexto().setResultados(resultadosSalvos);
            }

            // Gera o objeto do laudo
            LaudoCompleto laudoObjeto = facade.construirLaudo(req);
            // Formata o laudo para HTML, PDF ou texto
            String laudoFormatado = facade.formatarLaudo(laudoObjeto, req.getFormato());

            // Passa para a view
            model.addAttribute("laudo", laudoFormatado);
            model.addAttribute("tipoExame", req.getTipoExame());
            model.addAttribute("pacienteNome", req.getPacienteNome());
            model.addAttribute("convenio", req.getConvenio());
            model.addAttribute("medicoSolicitante", req.getMedicoSolicitante());
            model.addAttribute("medicoResponsavel", req.getMedicoResponsavel());
            model.addAttribute("crmResponsavel", req.getCrmResponsavel());

            // Remove resultados temporários da sessão
            session.removeAttribute("resultadosDoCsv");

            return "laudo";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao gerar laudo: " + e.getMessage());
            model.addAttribute("laudoRequest", req);
            model.addAttribute("tipos", TipoExame.values());
            return "exames";
        }
    }

    @GetMapping("/fila")
    public String fila(Model model) {
        model.addAttribute("tamanho", facade.tamanhoFila());
        return "fila";
    }
}