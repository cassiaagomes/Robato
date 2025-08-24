package com.robato.diagnosticos.web;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.domain.Paciente;
import com.robato.diagnosticos.exame.TipoExame;
import com.robato.diagnosticos.facade.SistemaDiagnosticoFacade;
import com.robato.diagnosticos.laudo.LaudoCompleto;
import com.robato.diagnosticos.notificacao.NotificadorEmail;
import com.robato.diagnosticos.notificacao.NotificadorWhatsapp;
import com.robato.diagnosticos.service.PacienteService;
import com.robato.diagnosticos.web.dto.LaudoRequest;
import com.robato.diagnosticos.web.dto.ResultadoExameItem;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExameViewController {

    private final SistemaDiagnosticoFacade facade;
    private final PacienteService pacienteService;
    private final NotificadorEmail notificadorEmail;
    private final NotificadorWhatsapp notificadorWhatsapp;

    public ExameViewController(SistemaDiagnosticoFacade facade, PacienteService pacienteService,
            NotificadorEmail notificadorEmail, NotificadorWhatsapp notificadorWhatsapp) {
        this.facade = facade;
        this.pacienteService = pacienteService;
        this.notificadorEmail = notificadorEmail;
        this.notificadorWhatsapp = notificadorWhatsapp;
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

    @GetMapping("/pacientes/dados/{id}")
    @ResponseBody
    public Paciente getDadosPaciente(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
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
        List<Paciente> pacientes = pacienteService.listarPacientes();
        model.addAttribute("pacientes", pacientes);

        model.addAttribute("currentUri", request.getRequestURI());
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
            List<ResultadoExameItem> resultadosDoCsv = facade.parseResultadosCsv(file.getInputStream());

            System.out.println(">>> CSV parseado com " + resultadosDoCsv.size() + " resultados");
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
    public String gerarLaudo(@ModelAttribute LaudoRequest req,
            @RequestParam(value = "imagemExame", required = false) MultipartFile imagemFile,
            Model model, HttpSession session, HttpServletRequest request) {
        try {
            if (req.getPacienteId() != null) {
                Paciente paciente = pacienteService.buscarPorId(req.getPacienteId());
                if (paciente != null) {
                    req.setPacienteNome(paciente.getNome());
                    req.setEmailPaciente(paciente.getEmail());
                    req.setTelefonePaciente(paciente.getTelefone());
                    req.setConvenio(paciente.getConvenio());

                    System.out.println(">>> Dados do paciente carregados: " + paciente.getNome());
                    System.out.println(">>> Email: " + paciente.getEmail());
                    System.out.println(">>> Telefone: " + paciente.getTelefone());
                }
            }

            @SuppressWarnings("unchecked")
            List<ResultadoExameItem> resultadosSalvos = (List<ResultadoExameItem>) session
                    .getAttribute("resultadosDoCsv");

            if (resultadosSalvos == null && req.getContexto() != null && req.getContexto().getResultados() != null) {
                resultadosSalvos = req.getContexto().getResultados();
            }

            if (req.getContexto() == null) {
                req.setContexto(new ValidacaoContexto());
            }

            if (resultadosSalvos != null && !resultadosSalvos.isEmpty()) {
                req.getContexto().setResultados(resultadosSalvos);
                System.out.println(">>> Resultados a serem processados: " + resultadosSalvos.size());
            }

            if (imagemFile != null && !imagemFile.isEmpty() &&
                    (req.getTipoExame().equals("RAIO_X") || req.getTipoExame().equals("RESSONANCIA"))) {

                if (imagemFile.getContentType() != null &&
                        (imagemFile.getContentType().startsWith("image/") ||
                                imagemFile.getContentType().equals("application/pdf") ||
                                imagemFile.getContentType().equals("application/dicom"))) {

                    String imagemBase64 = Base64.getEncoder().encodeToString(imagemFile.getBytes());
                    req.getContexto().setImagemBase64(imagemBase64);

                    System.out.println(">>> Imagem recebida: " + imagemFile.getOriginalFilename() +
                            " (" + imagemFile.getSize() + " bytes, tipo: " + imagemFile.getContentType() + ")");
                } else {
                    System.out.println(">>> Arquivo não é uma imagem válida: " + imagemFile.getContentType());
                }
            }

            System.out.println(">>> TipoExame: " + req.getTipoExame());
            System.out.println(">>> Paciente: " + req.getPacienteNome());
            System.out.println(">>> Email: " + req.getEmailPaciente());
            System.out.println(">>> Telefone: " + req.getTelefonePaciente());
            System.out.println(">>> Resultados no contexto: " +
                    (req.getContexto().getResultados() != null ? req.getContexto().getResultados().size() : 0));
            System.out.println(">>> Possui imagem: " + (req.getContexto().getImagemBase64() != null));

            String laudoFormatado = facade.gerarLaudo(req);

            LaudoCompleto laudoObjeto = facade.construirLaudo(req);

            if (req.getContexto().getImagemBase64() != null) {
                laudoObjeto.setImagemBase64(req.getContexto().getImagemBase64());
            }

            System.out.println(">>> Resultados no laudoObjeto: " +
                    (laudoObjeto.getResultados() != null ? laudoObjeto.getResultados().size() : 0));
            System.out.println(">>> Possui imagem no laudo: " + (laudoObjeto.getImagemBase64() != null));

            System.out.println("=== NOTIFICAÇÃO ENVIADA ===");
            System.out.println("Paciente: " + req.getPacienteNome());
            System.out.println("Email: " + req.getEmailPaciente());
            System.out.println("Telefone: " + req.getTelefonePaciente());

            model.addAttribute("laudo", laudoObjeto); 
            model.addAttribute("laudoFormatado", laudoFormatado);
            model.addAttribute("tipoExame", req.getTipoExame());

            model.addAttribute("pacienteNome", laudoObjeto.getPaciente());
            model.addAttribute("convenio", laudoObjeto.getConvenio());
            model.addAttribute("medicoSolicitante", laudoObjeto.getMedicoSolicitante());
            model.addAttribute("medicoResponsavel", laudoObjeto.getMedicoResponsavel());
            model.addAttribute("crmResponsavel", laudoObjeto.getCrmResponsavel());

            model.addAttribute("emailPaciente", req.getEmailPaciente());
            model.addAttribute("telefonePaciente", req.getTelefonePaciente());

            session.removeAttribute("resultadosDoCsv");
            model.addAttribute("currentUri", request.getRequestURI());

            notificadorEmail.atualizar("Seu exame está pronto! Acesse o sistema para visualizar.",
                    req.getEmailPaciente());
            notificadorWhatsapp.atualizar("Seu exame está pronto! Acesse o sistema para visualizar.",
                    req.getTelefonePaciente());

            return "laudo";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao gerar laudo: " + e.getMessage());
            model.addAttribute("laudoRequest", req);
            model.addAttribute("tipos", TipoExame.values());
            model.addAttribute("currentUri", request.getRequestURI());
            return "exames";
        }
    }
}
