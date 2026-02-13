package com.hub.hds.service.candidatura;

import com.hub.hds.dto.candidatura.CandidaturaRequest;
import com.hub.hds.dto.candidatura.CandidaturaResponse;
import com.hub.hds.dto.candidatura.CandidaturaStatus;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.candidatura.Candidatura;
import com.hub.hds.models.candidatura.StatusCandidatura;
import com.hub.hds.models.processoSeletivo.EtapaProcesso;
import com.hub.hds.models.processoSeletivo.ProcessoSeletivo;
import com.hub.hds.models.vaga.StatusVaga;
import com.hub.hds.models.vaga.Vaga;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.candidatura.CandidaturaRepository;
import com.hub.hds.repository.processoSeletivo.ProcessoSeletivoRepository;
import com.hub.hds.repository.vaga.VagaRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final CandidatoRepository candidatoRepository;
    private final VagaRepository vagaRepository;
    private final ProcessoSeletivoRepository processoSeletivoRepository;

    public CandidaturaService(
            CandidaturaRepository candidaturaRepository,
            CandidatoRepository candidatoRepository,
            VagaRepository vagaRepository,
            ProcessoSeletivoRepository processoSeletivoRepository
    ) {
        this.candidaturaRepository = candidaturaRepository;
        this.candidatoRepository = candidatoRepository;
        this.vagaRepository = vagaRepository;
        this.processoSeletivoRepository = processoSeletivoRepository;
    }

    /* =====================================================
       1️⃣ CRIAR CANDIDATURA
       ===================================================== */
    @Transactional
    public CandidaturaResponse criarCandidatura(CandidaturaRequest request) {

        // 🔒 evita candidatura duplicada
        if (candidaturaRepository.existsByCandidato_IdCandidatoAndVaga_IdVaga(
                request.candidatoId(),
                request.vagaId()
        )) {
            throw new IllegalStateException("Você já se candidatou para esta vaga.");
        }

        Candidato candidato = candidatoRepository.findById(request.candidatoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Candidato não encontrado")
                );

        Vaga vaga = vagaRepository.findById(request.vagaId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Vaga não encontrada")
                );

        // 🔒 não permite candidatura em vaga fechada
        if (vaga.getStatusVaga() == StatusVaga.FECHADA) {
            throw new IllegalStateException("Esta vaga já foi encerrada");
        }

        ProcessoSeletivo processoSeletivo = processoSeletivoRepository
                .findByVaga_IdVaga(vaga.getIdVaga())
                .orElseThrow(() ->
                        new RuntimeException("Processo seletivo não encontrado")
                );

        EtapaProcesso etapaInicial = processoSeletivo.getEtapaProcessos().get(0);

        Candidatura candidatura = Candidatura.builder()
                .candidato(candidato)
                .vaga(vaga)
                .etapaAtual(etapaInicial)
                .statusCandidatura(StatusCandidatura.ENVIADA)
                .build();

        candidaturaRepository.save(candidatura);

        return new CandidaturaResponse(
                candidatura.getIdCandidatura(),
                candidatura.getStatusCandidatura()
        );
    }

    /* =====================================================
       2️⃣ ALTERAR STATUS DA CANDIDATURA (EMPRESA / ADMIN)
       ===================================================== */
    @Transactional
    public CandidaturaResponse alterarStatus(
            Long idCandidatura,
            CandidaturaStatus status,
            Long idUsuarioAvaliador
    ) {

        Candidatura candidatura = candidaturaRepository.findById(idCandidatura)
                .orElseThrow(() ->
                        new IllegalArgumentException("Candidatura não encontrada")
                );

        StatusCandidatura statusAtual = candidatura.getStatusCandidatura();
        StatusCandidatura novoStatus = status.novoStatus();

        // 🔒 valida transição com o enum
        if (!statusAtual.podeIrPara(novoStatus)) {
            throw new IllegalStateException(
                    "Transição inválida: " + statusAtual + " → " + novoStatus
            );
        }

        candidatura.setStatusCandidatura(novoStatus);
        candidatura.setObservacoes(status.observacoes());
        candidatura.setAvaliadoPor(idUsuarioAvaliador);

        if (novoStatus.isFinal()) {
            candidatura.setDataDecisao(LocalDateTime.now());
        }

        candidaturaRepository.save(candidatura);

        return new CandidaturaResponse(
                candidatura.getIdCandidatura(),
                candidatura.getStatusCandidatura()
        );
    }

    /* =====================================================
       3️⃣ FECHAR VAGA (EMPRESA)
       ===================================================== */
    @Transactional
    public void fecharVaga(Long idVaga) {

        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() ->
                        new RuntimeException("Vaga não encontrada")
                );

        if (vaga.getStatusVaga() == StatusVaga.FECHADA) {
            return; // já fechada
        }

        vaga.setStatusVaga(StatusVaga.FECHADA);

        List<Candidatura> candidaturas =
                candidaturaRepository.findByVaga_IdVaga(idVaga);

        for (Candidatura c : candidaturas) {
            if (!c.getStatusCandidatura().isFinal()) {
                c.setStatusCandidatura(StatusCandidatura.CANCELADA);
                c.setObservacoes("Vaga encerrada pela empresa");
            }
        }
    }
}
