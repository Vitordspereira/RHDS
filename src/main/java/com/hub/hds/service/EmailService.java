package com.hub.hds.service;

import com.hub.hds.models.vaga.Vaga;
import org.springframework.beans.factory.annotation.Autowired;
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

    // =========================
    // METODO BASE PARA E-MAIL
    // =========================
    public void enviarEmail(String para, String assunto, String texto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // 'true' permite anexos e HTML

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(texto, true); // 'true' indica que o conteúdo é em HTML
            helper.setFrom("vitorpj1602@gmail.com");

            mailSender.send(message);
            System.out.println("EMAIL ENVIADO COM SUCESSO");

        } catch (Exception e) {
            System.err.println("ERRO AO ENVIAR EMAIL PARA: " + para);
            e.printStackTrace();
        }
    }

    // =========================
    // EMAIL: ENVIAR TOKEN PARA CONFIRMAÇÃO DE CANDIDATURA
    // =========================
    public void enviarTokenPorEmail(String email, String token) {
        String assunto = "Token de confirmação para sua candidatura";
        String texto =
                "Olá!\n\n" +
                        "Para confirmar sua candidatura, use o seguinte token: " + token + "\n\n";
        enviarEmail(email, assunto, texto);  // Envia o e-mail com o token
    }

    // =========================
    // EMAIL: CONFIRMAÇÃO DE CANDIDATURA REALIZADA COM SUCESSO
    // =========================
    public void enviarConfirmacaoCandidatura(String email, Vaga vaga) {
        String assunto = "Candidatura realizada com sucesso!";
        String texto =
                "Sua candidatura para a vaga \"" + vaga.getTituloFinal() + "\" foi realizada com sucesso.\n\n" +
                        "Acompanhe as próximas etapas pelo seu perfil.";
        enviarEmail(email, assunto, texto);  // Envia o e-mail de confirmação de candidatura
    }

    // =========================
    // 3️⃣ LEMBRETE DE INTERESSE (ESTAVA FALTANDO)
    // =========================
    public void enviarLembreteInteresse(String email, Vaga vaga) {
        String assunto = "Você ainda tem interesse nesta vaga?";
        String texto =
                "<p>Olá!</p>" +
                        "<p>Notamos que você iniciou uma candidatura para a vaga:</p>" +
                        "<p><strong>" + vaga.getTituloFinal() + "</strong></p>" +
                        "<p>Caso ainda tenha interesse, acesse seu perfil e finalize a candidatura.</p>";

        enviarEmail(email, assunto, texto);
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

            mailSender.send(msg);
        }
    }
}
