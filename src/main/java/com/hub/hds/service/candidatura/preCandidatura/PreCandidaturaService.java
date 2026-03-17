package com.hub.hds.service.candidatura.preCandidatura;

import com.hub.hds.models.candidatura.Candidatura;
import com.hub.hds.models.candidatura.StatusCandidatura;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.candidatura.preCandidatura.PreCandidatura;
import com.hub.hds.models.candidatura.preCandidatura.StatusPreCandidatura;
import com.hub.hds.models.vaga.Vaga;
import com.hub.hds.repository.candidatura.CandidaturaRepository;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.candidatura.PreCandidatura.PreCandidaturaRepository;
import com.hub.hds.repository.vaga.VagaRepository;
import com.hub.hds.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PreCandidaturaService {

    private final PreCandidaturaRepository preCandidaturaRepository;
    private final CandidatoRepository candidatoRepository;
    private final CandidaturaRepository candidaturaRepository;
    private final VagaRepository vagaRepository;
    private final EmailService emailService;

    public PreCandidaturaService(
            PreCandidaturaRepository preCandidaturaRepository,
            CandidatoRepository candidatoRepository,
            CandidaturaRepository candidaturaRepository,
            VagaRepository vagaRepository,
            EmailService emailService
    ) {
        this.preCandidaturaRepository = preCandidaturaRepository;
        this.candidatoRepository = candidatoRepository;
        this.candidaturaRepository = candidaturaRepository;
        this.vagaRepository = vagaRepository;
        this.emailService = emailService;
    }

    // =========================
    // 1️⃣ INICIAR PRÉ-CANDIDATURA
    // =========================
    @Transactional
    public PreCandidatura iniciar(Long vagaId, String email) {

        if (preCandidaturaRepository.existsByEmailAndVaga_IdVagaAndStatusPreCandidatura(
                email,
                vagaId,
                StatusPreCandidatura.INICIADA
        )) {
            throw new RuntimeException("Já existe uma candidatura em andamento para esta vaga.");
        }

        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada."));

        PreCandidatura pre = PreCandidatura.builder()
                .email(email)
                .vaga(vaga)
                .tokenConfirmacao(UUID.randomUUID().toString())
                .statusPreCandidatura(StatusPreCandidatura.INICIADA)
                .tokenEnviado(false)
                .emailLembreteEnviado(false)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        pre = preCandidaturaRepository.save(pre);

        if (!pre.isTokenEnviado()) {
            emailService.enviarTokenPorEmail(email, pre.getTokenConfirmacao());
            pre.setTokenEnviado(true);
            preCandidaturaRepository.save(pre);
        }

        return pre;
    }

    // =========================
    // 2️⃣ VALIDAR TOKEN (FRONT)
    // =========================
    public PreCandidatura validarToken(String token) {

        PreCandidatura pre = preCandidaturaRepository
                .findByTokenConfirmacao(token)
                .orElseThrow(() -> new RuntimeException("Token inválido."));

        if (pre.getStatusPreCandidatura() == StatusPreCandidatura.EXPIRADA ||
                pre.getExpiresAt().isBefore(LocalDateTime.now())) {

            pre.setStatusPreCandidatura(StatusPreCandidatura.EXPIRADA);
            preCandidaturaRepository.save(pre);

            throw new RuntimeException("Pré-candidatura expirada.");
        }

        return pre;
    }

    // =========================
    // 3️⃣ CONFIRMAR E CONVERTER
    // =========================
    @Transactional
    public void confirmarEConverter(String token) {

        PreCandidatura pre = validarToken(token);

        if (pre.getStatusPreCandidatura() == StatusPreCandidatura.CONVERTIDA) {
            return;
        }

        Candidato candidato = candidatoRepository
                .findByUsuario_Email(pre.getEmail())
                .orElseThrow(() -> new RuntimeException("Nenhum candidato encontrado para este e-mail."));

        if (candidaturaRepository.existsByCandidato_IdCandidatoAndVaga_IdVaga(
                candidato.getIdCandidato(),
                pre.getVaga().getIdVaga()
        )) {
            throw new RuntimeException("Você já se candidatou a esta vaga.");
        }

        Candidatura candidatura = Candidatura.builder()
                .candidato(candidato)
                .vaga(pre.getVaga())
                .statusCandidatura(StatusCandidatura.ENTREVISTA)
                .build();

        candidaturaRepository.save(candidatura);

        pre.setStatusPreCandidatura(StatusPreCandidatura.CONVERTIDA);
        preCandidaturaRepository.save(pre);

        emailService.enviarConfirmacaoCandidatura(pre.getEmail(), pre.getVaga());
    }
}

