package com.robato.diagnosticos.infrastructure.observer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

@Component
public class NotificadorTelegram implements Notificador {

    
    private static final String BOT_TOKEN = "8482754570:AAHG53omeInuID0ERPSl0KeEAbTZd3H9XI4"; 
    private static final String CHAT_ID = "1166361596";  

    @Override
    public void enviar(String mensagem, String destinatario) {
        if (BOT_TOKEN.equals("8482754570:AAHG53omeInuID0ERPSl0KeEAbTZd3H9XI4") || CHAT_ID.equals("1166361596")) {
            System.err.println("TELEGRAM: Bot Token ou Chat ID não configurados. Envio real cancelado.");
            return;
        }

        try {
            String textoFormatado = "Notificação de Laudo para " + destinatario + ":\n\n" + mensagem;
            
            String urlString = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                BOT_TOKEN,
                CHAT_ID,
                URLEncoder.encode(textoFormatado, StandardCharsets.UTF_8)
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("--- TELEGRAM REAL ENVIADO ---");
                System.out.println("Para Chat ID: " + CHAT_ID);
                System.out.println("-------------------------------\n");
            } else {
                System.err.println("Falha ao enviar notificação via Telegram. Código de resposta: " + responseCode);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.err.println("Falha !! ao enviar notificação real via Telegram: " + e.getMessage());
        }
    }
}