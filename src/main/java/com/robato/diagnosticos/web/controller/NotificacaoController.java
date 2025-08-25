package com.robato.diagnosticos.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.robato.diagnosticos.infrastructure.observer.AssuntoNotificacao;
import com.robato.diagnosticos.infrastructure.observer.NotificadorEmail;
import com.robato.diagnosticos.infrastructure.observer.NotificadorTelegram;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final AssuntoNotificacao assuntoNotificacao;
    private final NotificadorEmail notificadorEmail;
    private final NotificadorTelegram notificadorTelegram;

    public NotificacaoController(AssuntoNotificacao assunto, NotificadorEmail email, NotificadorTelegram telegram) {
        this.assuntoNotificacao = assunto;
        this.notificadorEmail = email;
        this.notificadorTelegram = telegram;
        
        // Configura os observadores
        this.assuntoNotificacao.adicionar(this.notificadorEmail);
        this.assuntoNotificacao.adicionar(this.notificadorTelegram);
    }

    @GetMapping
    public String telaSimulacao(Model model) {
        model.addAttribute("notificacaoRequest", new NotificacaoRequest());
        // Em um sistema real, os históricos seriam persistidos. Aqui simulamos buscando dos próprios notificadores.
        // model.addAttribute("emails", notificadorEmail.getHistorico());
        // model.addAttribute("telegrams", notificadorTelegram.getHistorico());
        return "notificacoes";
    }

    @PostMapping("/enviar")
    public String enviarParaTodos(@ModelAttribute NotificacaoRequest request) {
        assuntoNotificacao.notificarObservadores(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    @PostMapping("/enviar-email")
    public String enviarApenasEmail(@ModelAttribute NotificacaoRequest request) {
        notificadorEmail.enviar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    @PostMapping("/enviar-telegram")
    public String enviarApenasTelegram(@ModelAttribute NotificacaoRequest request) {
        notificadorTelegram.enviar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes";
    }

    // DTO aninhado para o formulário de notificação
    public static class NotificacaoRequest {
        private String destino;
        private String mensagem;

        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    }
}
