package com.robato.diagnosticos.infrastructure.strategy;

import com.robato.diagnosticos.application.dto.LaudoDTO;
import java.nio.charset.StandardCharsets;

public class GeradorHTML implements GeradorFormato {

    @Override
    public byte[] gerar(LaudoDTO laudo) {
        String corpoHtml = laudo.corpo().replace("\n", "<br/>");

        String html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <title>Laudo Médico</title>
                <style>
                    body { font-family: sans-serif; margin: 2em; color: #333; }
                    .laudo { max-width: 800px; margin: auto; border: 1px solid #eee; padding: 2em; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                    .cabecalho, .rodape { font-style: italic; color: #555; }
                    .corpo { margin-top: 2em; margin-bottom: 2em; line-height: 1.6; }
                </style>
            </head>
            <body>
                <div class="laudo">
                    <div class="cabecalho">
                        <h2>%s</h2>
                    </div>
                    <hr/>
                    <div class="corpo">
                        <p>%s</p>
                    </div>
                    <hr/>
                    <div class="rodape">
                        <p>%s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(laudo.cabecalho(), corpoHtml, laudo.rodape());

        return html.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }
}
