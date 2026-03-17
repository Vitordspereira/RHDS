package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.util.Set;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public void enviarEmail(String para, String assunto, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(html, true);
            helper.setFrom(mailFrom);

            mailSender.send(message);
            System.out.println("EMAIL ENVIADO COM SUCESSO PARA: " + para);

        } catch (Exception e) {
            System.err.println("ERRO AO ENVIAR EMAIL PARA: " + para);
            e.printStackTrace();
            throw new RuntimeException("Falha ao enviar e-mail.", e);
        }
    }

    public void enviarTokenPorEmail(String email, String token) {
        String assunto = "Token de confirmação para sua candidatura";

        String html = """
                <html>
                    <body>
                        <p>Olá!</p>
                        <p>Para confirmar sua candidatura, use o seguinte token:</p>
                        <p style="font-size:18px; font-weight:bold;">%s</p>
                        <p>Esse token expira em 24 horas.</p>
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

    public void notificarVagaEncerrada(Vaga vaga, Set<String> emails) {
        String assunto = "Atualização: vaga encerrada";

        String corpo = """
                Olá!

                A vaga "%s" foi encerrada pela empresa.

                Agradecemos sua candidatura e desejamos sucesso nos próximos processos seletivos.

                Atenciosamente,
                RHDS
                """.formatted(vaga.getCargo());

        for (String email : emails) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject(assunto);
            msg.setText(corpo);
            msg.setFrom(mailFrom);

            mailSender.send(msg);
        }
    }
}