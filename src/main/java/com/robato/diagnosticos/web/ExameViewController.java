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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExameViewController {

    private final SistemaDiagnosticoFacade facade;

    public ExameViewController(SistemaDiagnosticoFacade facade) {
        this.facade = facade;
    }

    @GetMapping({ "/", "/dashboard" })
    public String dashboard(Model model, HttpServletRequest request) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("laudosNoMes", 78);
        stats.put("pacientesEmFila", facade.tamanhoFila());
        stats.put("totalPacientes", facade.listarPacientes().size());
        stats.put("medicosAtivos", facade.listarMedicos().size());
        model.addAttribute("stats", stats);
        model.addAttribute("tipos", TipoExame.values());
        model.addAttribute("currentUri", request.getRequestURI()); // Adicionado
        return "dashboard";
    }

    @GetMapping("/exames/novo")
    public String novo(Model model, @ModelAttribute("laudoRequest") LaudoRequest laudoRequest, HttpSession session,
            HttpServletRequest request) {
        LaudoRequest laudoNaSessao = (LaudoRequest) session.getAttribute("laudoRequestAtual");
        if (laudoNaSessao != null) {
            model.addAttribute("laudoRequest", laudoNaSessao);
        } else if (laudoRequest == null || laudoRequest.getTipoExame() == null) {
            model.addAttribute("laudoRequest", new LaudoRequest());
        }
        model.addAttribute("tipos", TipoExame.values());
        model.addAttribute("currentUri", request.getRequestURI()); // Adicionado
        return "exames";
    }

    @GetMapping("/exames/novo/preencher")
    public String preencherNovoLaudo(@RequestParam String pacienteNome,
            @RequestParam String tipoExame,
            @RequestParam String convenio, // Parâmetro adicionado
            HttpSession session, RedirectAttributes redirectAttributes) {
        LaudoRequest laudoRequest = new LaudoRequest();
        laudoRequest.setPacienteNome(pacienteNome);
        laudoRequest.setTipoExame(tipoExame);
        laudoRequest.setConvenio(convenio); // Campo adicionado

        session.setAttribute("laudoRequestAtual", laudoRequest);

        redirectAttributes.addFlashAttribute("laudoRequest", laudoRequest);
        return "redirect:/exames/novo";
    }

    @GetMapping("/exames/importar-pagina")
    public String paginaImportar(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
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
            // Usar o método parseResultadosCsv em vez de parseCsvParaLaudoRequest
            List<ResultadoExameItem> resultadosDoCsv = facade.parseResultadosCsv(file.getInputStream());

            System.out.println(">>> CSV parseado com " + resultadosDoCsv.size() + " resultados");

            // Salvar os resultados na sessão
            session.setAttribute("resultadosDoCsv", resultadosDoCsv);

            redirectAttributes.addFlashAttribute("sucesso",
                    "Resultados do CSV importados! Preencha os dados do paciente e gere o laudo.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o CSV: " + e.getMessage());
        }
        return "redirect:/exames/novo";
    }

    @PostMapping("/exames/gerar")
    public String gerarLaudo(@ModelAttribute LaudoRequest req, Model model, HttpSession session,
            HttpServletRequest request) {
        try {
            // Recuperar resultados do CSV da sessão
            @SuppressWarnings("unchecked")
            List<ResultadoExameItem> resultadosSalvos = (List<ResultadoExameItem>) session
                    .getAttribute("resultadosDoCsv");

            // Se não há resultados na sessão, verificar se há no request
            if (resultadosSalvos == null && req.getContexto() != null && req.getContexto().getResultados() != null) {
                resultadosSalvos = req.getContexto().getResultados();
            }

            // Garantir que o contexto existe
            if (req.getContexto() == null) {
                req.setContexto(new ValidacaoContexto());
            }

            // Se temos resultados, colocá-los no contexto
            if (resultadosSalvos != null && !resultadosSalvos.isEmpty()) {
                req.getContexto().setResultados(resultadosSalvos);
                System.out.println(">>> Resultados a serem processados: " + resultadosSalvos.size());
            }

            // Debug: verificar o que está no request
            System.out.println(">>> TipoExame: " + req.getTipoExame());
            System.out.println(">>> Resultados no contexto: " +
                    (req.getContexto().getResultados() != null ? req.getContexto().getResultados().size() : 0));

            // Gera o objeto do laudo
            LaudoCompleto laudoObjeto = facade.construirLaudo(req);

            // Debug: verificar o que foi construído
            System.out.println(">>> Resultados no laudoObjeto: " +
                    (laudoObjeto.getResultados() != null ? laudoObjeto.getResultados().size() : 0));
            model.addAttribute("laudo", laudoObjeto); // ← Objeto LaudoCompleto
            model.addAttribute("tipoExame", req.getTipoExame());

            // Mantém os atributos individuais para compatibilidade com o template
            model.addAttribute("pacienteNome", laudoObjeto.getPaciente());
            model.addAttribute("convenio", laudoObjeto.getConvenio());
            model.addAttribute("medicoSolicitante", laudoObjeto.getMedicoSolicitante());
            model.addAttribute("medicoResponsavel", laudoObjeto.getMedicoResponsavel());
            model.addAttribute("crmResponsavel", laudoObjeto.getCrmResponsavel());

            // Remove resultados temporários da sessão
            session.removeAttribute("resultadosDoCsv");
            model.addAttribute("currentUri", request.getRequestURI());

            return "laudo";
        } catch (Exception e) {
            e.printStackTrace(); // Para ver o stack trace completo
            model.addAttribute("erro", "Erro ao gerar laudo: " + e.getMessage());
            model.addAttribute("laudoRequest", req);
            model.addAttribute("tipos", TipoExame.values());
            model.addAttribute("currentUri", request.getRequestURI());
            return "exames";
        }
    }

    

   
}