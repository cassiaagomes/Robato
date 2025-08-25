package com.robato.diagnosticos.notificacao;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class NotificadorTelegram implements ObservadorNotificacao {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public void atualizar(String mensagem, String destino) {
        
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.telegram.org/bot" + botToken + "/sendMessage").newBuilder();
        urlBuilder.addQueryParameter("chat_id", chatId);
        urlBuilder.addQueryParameter("text", mensagem);
        
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("[TELEGRAM] Notificação enviada com sucesso para o Chat ID: " + chatId);
            } else {
                System.err.println("[TELEGRAM] Falha ao enviar notificação: " + response.body().string());
            }
        } catch (IOException e) {
            System.err.println("[TELEGRAM] Erro de IO ao enviar notificação: " + e.getMessage());
        }
    }
}