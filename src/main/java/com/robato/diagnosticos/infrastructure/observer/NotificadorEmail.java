package com.robato.diagnosticos.infrastructure.observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificadorEmail implements Notificador {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void enviar(String mensagem, String destinatario) {
        if (destinatario == null || destinatario.isBlank()) {
            System.err.println("EMAIL: Destinatário não informado, notificação cancelada.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("Seu Laudo de Exame está Pronto!");
            message.setText(mensagem);
            
            emailSender.send(message);
            
            System.out.println("--- EMAIL REAL ENVIADO ---");
            System.out.println("Para: " + destinatario);
            System.out.println("--------------------------\n");

        } catch (Exception e) {
            System.err.println("Falha ao enviar email real para " + destinatario + ": " + e.getMessage());
        }
    }
}
