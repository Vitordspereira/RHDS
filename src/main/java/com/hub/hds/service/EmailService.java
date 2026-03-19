package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String from;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmail(String para, String assunto, String html) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(para);
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);
            System.out.println("EMAIL ENVIADO COM SUCESSO PARA: " + para);
        } catch (MessagingException | MailException mailException) {
            System.err.println("ERRO AO ENVIAR UM EMAIL PARA: " + para);
            mailException.printStackTrace();
            throw new RuntimeException("Falha ao enviar e-mail.", mailException);
        }
    }

    public void enviarTokenPorEmail(String email, String token) {
        String assunto = "Token de confirmação para a sua candidatura";

        String hmtl = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>Para confirmar sua candidatura, use o seguinte token:</p>
                        <p style="font-size:18px; font-weight:bold;">%s</p>
                    </body>
                </html>
                """.formatted(token);

        enviarEmail(email, assunto, hmtl);
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
                """.formatted(vaga.getCargo());

        for (String email : emails) {
            enviarEmail(email, assunto, html);
        }
    }
}