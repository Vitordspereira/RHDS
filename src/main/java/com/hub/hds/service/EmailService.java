package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void enviarEmail(String para, String assunto, String html) {
        if (para == null || para.isBlank()) {
            throw new IllegalArgumentException("E-mail de destino não informado.");
        }

        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            throw new IllegalStateException("Chave da Brevo não configurada.");
        }

        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException("E-mail do remetente não configurado.");
        }

        try {
            String json = """
                    {
                      "sender": {
                        "name": "%s",
                        "email": "%s"
                      },
                      "to": [
                        {
                          "email": "%s"
                        }
                      ],
                      "subject": "%s",
                      "htmlContent": %s
                    }
                    """.formatted(
                    escapeJson(senderName),
                    escapeJson(senderEmail),
                    escapeJson(para),
                    escapeJson(assunto),
                    toJsonString(html)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", brevoApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Brevo status: {}", response.statusCode());
            log.info("Brevo body: {}", response.body());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Falha ao enviar e-mail via Brevo: " + response.body());
            }

            log.info("E-mail enviado com sucesso para {}", para);

        } catch (IOException e) {
            log.error("Erro de IO ao enviar e-mail para {}", para, e);
            throw new RuntimeException("Falha ao enviar e-mail.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrompida ao enviar e-mail para {}", para, e);
            throw new RuntimeException("Falha ao enviar e-mail.", e);
        }
    }

    public void enviarTokenPorEmail(String email, String token) {
        String assunto = "Token de confirmação para a sua candidatura";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>Para confirmar sua candidatura, use o seguinte token:</p>
                        <p style="font-size:18px; font-weight:bold;">%s</p>
                    </body>
                </html>
                """.formatted(escapeHtml(token));

        enviarEmail(email, assunto, html);
    }

    public void enviarConfirmacaoCandidatura(String email, Vaga vaga) {
        String tituloVaga = (vaga != null && vaga.getTituloFinal() != null && !vaga.getTituloFinal().isBlank())
                ? vaga.getTituloFinal()
                : "vaga informada";

        String assunto = "Candidatura realizada com sucesso!";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>Sua candidatura para a vaga <strong>%s</strong> foi realizada com sucesso.</p>
                        <p>Acompanhe as próximas etapas pelo seu perfil.</p>
                    </body>
                </html>
                """.formatted(escapeHtml(tituloVaga));

        enviarEmail(email, assunto, html);
    }

    public void notificarVagaEncerrada(Vaga vaga, Set<String> emails) {
        String cargo = (vaga != null && vaga.getCargo() != null && !vaga.getCargo().isBlank())
                ? vaga.getCargo()
                : "vaga informada";

        String assunto = "Atualização vaga encerrada";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>A vaga <strong>%s</strong> foi encerrada pela empresa.</p>
                        <p>Agradecemos sua candidatura e desejamos sucesso nos próximos processos seletivos.</p>
                        <p>Atenciosamente,<br>RHDS</p>
                    </body>
                </html>
                """.formatted(escapeHtml(cargo));

        for (String email : emails) {
            try {
                enviarEmail(email, assunto, html);
            } catch (Exception e) {
                log.error("Falha ao notificar encerramento para {}", email, e);
            }
        }
    }

    private String toJsonString(String value) {
        return "\"" + escapeJson(value) + "\"";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}