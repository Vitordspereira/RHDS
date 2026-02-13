package com.hub.hds.service.alerta;

import com.hub.hds.AlertaPalavras.KeywordExtractor;
import com.hub.hds.AlertaPalavras.MatchScorer;
import com.hub.hds.dto.alerta.AlertaDTO;
import com.hub.hds.models.alerta.Alerta;
import com.hub.hds.models.vaga.Vaga;
import com.hub.hds.repository.alerta.AlertaRepository;
import com.hub.hds.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final EmailService emailService;

    public AlertaService(
            AlertaRepository alertaRepository,
            EmailService emailService
    ) {
        this.alertaRepository = alertaRepository;
        this.emailService = emailService;
    }

    // =========================
    // CRIAR ALERTA
    // =========================
    public void criarAlerta(AlertaDTO alertaDTO) {

        boolean jaExiste = alertaRepository.existsByEmailAndCargoAndCidade(
                alertaDTO.email(),
                alertaDTO.cargo(),
                alertaDTO.cidade()
        );

        if (jaExiste) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Você já possui um alerta para esse cargo"
            );
        }

        Alerta alerta = new Alerta();
        alerta.setEmail(alertaDTO.email());
        alerta.setCargo(alertaDTO.cargo());
        alerta.setCidade(alertaDTO.cidade());

        alerta.setAtivo(true);
        alerta.setTokenCancelamento(UUID.randomUUID().toString());

        alertaRepository.save(alerta);

        enviarEmailConfirmacao(alerta);
    }

    // =========================
    // E-MAIL: CONFIRMAÇÃO DE ALERTA
    // =========================
    private void enviarEmailConfirmacao(Alerta alerta) {

        String assunto = "Alerta de vagas criado com sucesso";

        String mensagem = """
            <p>Olá!</p>

            <p>Seu alerta de vagas foi criado com sucesso.</p>

            <p>
                <strong>Cargo:</strong> %s<br>
                <strong>Cidade:</strong> %s
            </p>

            <p>
                Assim que surgirem novas vagas compatíveis,
                você será avisado por e-mail.
            </p>
            """.formatted(
                alerta.getCargo(),
                alerta.getCidade()
        );

        emailService.enviarEmail(alerta.getEmail(), assunto, mensagem);
    }

    // =========================
    // AVISAR CANDIDATOS (ALERTAS)
    // =========================
    @Async
    public void avisarCandidatos(Vaga vaga, Set<String> vagaKeys) {

        if (vaga.getLocalizacao() == null ||
                vaga.getLocalizacao().getCidade() == null) {
            return;
        }

        List<Alerta> alertas =
                alertaRepository.findByCargoAndCidadeAndAtivoTrue(
                        vaga.getCargo(),
                        vaga.getLocalizacao().getCidade()
                );

        for (Alerta alerta : alertas) {

            Set<String> alertKeys =
                    KeywordExtractor.extract(alerta.getCargo());

            Set<String> intersecao = new HashSet<>(alertKeys);
            intersecao.retainAll(vagaKeys);

            if (intersecao.isEmpty()) {
                continue;
            }

            int score = MatchScorer.score(alertKeys, vagaKeys);

            double similaridade =
                    (double) score / Math.min(alertKeys.size(), vagaKeys.size());

            if (similaridade < 0.4) {
                continue;
            }

            // =========================
            // E-MAIL INFORMATIVO (SEM BOTÃO E SEM DESCADASTRO)
            // =========================
            String assunto = "Nova vaga compatível com seu alerta de emprego";

            String mensagem = """
                <p>Olá!</p>

                <p>Encontramos uma nova vaga compatível com o alerta que você cadastrou:</p>

                <p>
                    <strong>Cargo:</strong> %s<br>
                    <strong>Cidade:</strong> %s<br>
                    <strong>Modalidade:</strong> %s<br>
                    <strong>Tipo de contrato:</strong> %s
                </p>

                <p>
                    Acesse a plataforma para visualizar mais detalhes.
                </p>
                """.formatted(
                    vaga.getCargo(),
                    vaga.getLocalizacao().getCidade(),
                    vaga.getModalidadeVaga(),
                    vaga.getTipoContrato()
            );

            emailService.enviarEmail(
                    alerta.getEmail(),
                    assunto,
                    mensagem
            );
        }
    }
}
