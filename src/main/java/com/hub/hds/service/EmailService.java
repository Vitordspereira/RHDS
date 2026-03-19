package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from:${spring.mail.username}}")
    private String from;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmail(String para, String assunto, String html) {
        if (para == null || para.isBlank()) {
            throw new IllegalArgumentException("E-mail de destino não informado.");
        }

        if (from == null || from.isBlank()) {
            throw new IllegalStateException("Remetente de e-mail não configurado.");
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
            log.info("E-mail enviado com sucesso para {}", para);

        } catch (MessagingException | MailException e) {
            log.error("Erro ao enviar e-mail para {} com assunto {}", para, assunto, e);
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
                """.formatted(token);

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
                """.formatted(tituloVaga);

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
                """.formatted(cargo);

        for (String email : emails) {
            try {
                enviarEmail(email, assunto, html);
            } catch (Exception e) {
                log.error("Falha ao notificar encerramento para {}", email, e);
            }
        }
    }
}