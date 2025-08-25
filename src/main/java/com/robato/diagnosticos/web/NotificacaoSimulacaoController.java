package com.robato.diagnosticos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.robato.diagnosticos.notificacao.AssuntoNotificacao;
import com.robato.diagnosticos.notificacao.NotificadorEmail;
import com.robato.diagnosticos.notificacao.NotificadorTelegram; // Importa a classe correta

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoSimulacaoController {

    private final AssuntoNotificacao assuntoNotificacao;
    private final NotificadorEmail notificadorEmail;
    // --- CORREÇÃO AQUI ---
    private final NotificadorTelegram notificadorTelegram; // Usa a classe correta

    // --- CONSTRUTOR CORRIGIDO ---
    public NotificacaoSimulacaoController(NotificadorEmail notificadorEmail,
            NotificadorTelegram notificadorTelegram) { // Pede a classe correta
        this.notificadorEmail = notificadorEmail;
        this.notificadorTelegram = notificadorTelegram;

        this.assuntoNotificacao = new AssuntoNotificacao();
        assuntoNotificacao.adicionar(notificadorEmail);
        assuntoNotificacao.adicionar(notificadorTelegram); // Adiciona o observador correto
    }

    @GetMapping
    public String telaSimulacao(Model model, HttpServletRequest request) {
        model.addAttribute("notificacaoRequest", new NotificacaoRequest());
        // model.addAttribute("emails", notificadorEmail.getHistorico());
        // A lógica para exibir o histórico do Telegram pode ser adicionada aqui se necessário
        // model.addAttribute("telegrams", notificadorTelegram.getHistorico()); 
        model.addAttribute("currentUri", request.getRequestURI());
        return "notificacoes";
    }

    @PostMapping("/enviar")
    public String enviarNotificacao(@ModelAttribute NotificacaoRequest request) {
        assuntoNotificacao.notificarTodos(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    @PostMapping("/enviar-email")
    public String enviarApenasEmail(@ModelAttribute NotificacaoRequest request) {
        notificadorEmail.atualizar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    // Endpoint atualizado para o Telegram
    @PostMapping("/enviar-telegram")
    public String enviarApenasTelegram(@ModelAttribute NotificacaoRequest request) {
        notificadorTelegram.atualizar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    @GetMapping("/limpar") // MUDADO para GetMapping para ser mais simples de chamar
    public String limparHistorico() {
        // A lógica de limpar o histórico precisaria ser implementada no NotificadorEmail/Telegram se desejado
        // notificadorEmail.limparHistorico(); 
        return "redirect:/notificacoes";
    }

    // Classe interna estática para o formulário
    public static class NotificacaoRequest {
        private String destino;
        private String mensagem;

        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    }
}