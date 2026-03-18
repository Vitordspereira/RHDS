package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
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

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String resendFrom;

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public void enviarEmail(String para, String assunto, String html) {
        try{
            String json = """
                    {
                    "from": "%s",
                    "to": ["%s"],
                    "subject": "%s",
                    "html": %s
                    }
                    """.formatted(
                            escapeJson(resendFrom),
                    escapeJson(para),
                    escapeJson(assunto),
                    toJsonString(html)
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + resendApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );

            int status = httpResponse.statusCode();

            if (status < 200 || status >= 300) {
                System.err.println("ERRO AO ENVIAR EMAIL PARA: " + para);
                System.err.println("STATUS RESEND: " + status);
                System.err.println("RESPOSTA RESEND: " + httpResponse.body());
                throw new RuntimeException("Falha ao enviar e-mail.");
            }

            System.out.println("EMAIL ENVIADO COM SUCESSO PARA: " + para);
        } catch (InterruptedException interruptedException){
            Thread.currentThread().interrupt();
            System.err.println("ERRO AO ENVIAR EMAIL PARA: " + para);
            interruptedException.printStackTrace();
            throw new RuntimeException("Falha ao enviar e-mail. ", interruptedException);
        } catch (IOException ioException) {
            System.err.println("ERRO AO ENVIAR EMAIL PARA: " + para);
            ioException.printStackTrace();
            throw new RuntimeException("Falha ao enviar e-mail. ", ioException);
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
                """.formatted(token);

        enviarEmail(email, assunto, html);
    }

    public void enviarConfirmacaoCandidatura(String email, Vaga vaga) {
        String assunto = "Candidatura realizada com sucesso!";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>Sua candidatura para a vaga <strong>%s</strong> foi realizada com sucesso.</p>
                        <p>Acompanhe as próximas etapas pelo seu perfil.</p>
                    </body>
                </html>
                """.formatted(vaga.getTituloFinal());

        enviarEmail(email, assunto, html);
    }


    public void notificarVagaEncerrada(Vaga vaga, java.util.Set<String> emails) {
        String assunto = "Atualização: vaga encerrada";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>A vaga <strong>%s</strong> foi encerrada pela empresa.</p>
                        <p>Agradecemos sua candidatura e desejamos sucesso nos próximos processos seletivos.</p>
                        <p>Atenciosamente,<br>RHDS</p>
                    </body>
                </html>
                """.formatted(vaga.getCargo());

        for (String email : emails) {
            enviarEmail(email, assunto, html);
    }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private String toJsonString(String value) {
        if (value == null) return "\"\"";
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n") + "\"";
    }
}