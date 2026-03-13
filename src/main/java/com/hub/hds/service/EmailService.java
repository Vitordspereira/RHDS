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
            throw new RuntimeException("Falha ao enviar e-mail.", e);
        }
    }

    // =========================
    // 1️⃣ ENVIAR TOKEN PARA CONFIRMAÇÃO DE CANDIDATURA
    // =========================
    public void enviarTokenPorEmail(String email, String token) {
        String assunto = "Token de confirmação para sua candidatura";

        String texto = """
                <p>Olá!</p>
                <p>Para confirmar sua candidatura, use o seguinte token:</p>
                <p><strong>%s</strong></p>
                <p>Esse token expira em 24 horas.</p>
                """.formatted(token);

        enviarEmail(email, assunto, texto);
    }

    // =========================
    // 2️⃣ CONFIRMAÇÃO DE CANDIDATURA
    // =========================
    public void enviarConfirmacaoCandidatura(String email, Vaga vaga) {
        String assunto = "Candidatura realizada com sucesso!";

        String texto = """
                <p>Olá!</p>
                <p>Sua candidatura para a vaga <strong>%s</strong> foi realizada com sucesso.</p>
                <p>Acompanhe as próximas etapas pelo seu perfil.</p>
                """.formatted(vaga.getTituloFinal());

        enviarEmail(email, assunto, texto);
    }

    // =========================
    // 3️⃣ LEMBRETE DE INTERESSE
    // =========================
    public void enviarLembreteInteresse(String email, Vaga vaga) {
        String assunto = "Você ainda tem interesse nesta vaga?";

        String texto = """
                <p>Olá!</p>
                <p>Notamos que você iniciou uma candidatura para a vaga:</p>
                <p><strong>%s</strong></p>
                <p>Caso ainda tenha interesse, acesse seu perfil e finalize a candidatura.</p>
                """.formatted(vaga.getTituloFinal());

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
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(email);
                msg.setSubject(assunto);
                msg.setText(corpo);

                mailSender.send(msg);
            } catch (Exception e) {
                System.err.println("Erro ao notificar vaga encerrada para: " + email);
                e.printStackTrace();
            }
        }
    }
}
