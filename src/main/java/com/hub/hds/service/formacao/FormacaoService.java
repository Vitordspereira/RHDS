package com.hub.hds.service.formacao;

import com.hub.hds.dto.formacao.FormacaoRequest;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.formacao.Formacao;
import com.hub.hds.repository.candidato.CandidatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FormacaoService {

    private final CandidatoRepository candidatoRepository;

    public FormacaoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    // =========================
    // CRIAR FORMAÇÃO
    // =========================
    public FormacaoResponse criar(Long idCandidato, FormacaoRequest request) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Formacao formacao = Formacao.builder()
                .nomeCurso(request.nomeCurso())
                .instituicao(request.instituicao())
                .status(request.status())
                .periodoInicio(request.periodoInicio())
                .periodoFim(request.periodoFim())
                .build();

        // 🔴 MESMO PADRÃO DA EXPERIÊNCIA
        candidato.adicionarFormacao(formacao);

        return mapToResponse(formacao);
    }

    // =========================
    // LISTAR FORMAÇÕES DO CANDIDATO
    // =========================
    public List<FormacaoResponse> listarPorCandidato(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        return candidato.getFormacoes()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ATUALIZAR FORMAÇÃO
    // =========================
    public FormacaoResponse atualizar(
            Long idCandidato,
            Long idFormacao,
            FormacaoRequest request
    ) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Formacao formacao = candidato.getFormacoes()
                .stream()
                .filter(f -> f.getIdFormacao().equals(idFormacao))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Formação não encontrada"));

        formacao.setNomeCurso(request.nomeCurso());
        formacao.setInstituicao(request.instituicao());
        formacao.setStatus(request.status());
        formacao.setPeriodoInicio(request.periodoInicio());
        formacao.setPeriodoFim(request.periodoFim());

        return mapToResponse(formacao);
    }

    // =========================
    // DELETAR FORMAÇÃO
    // =========================
    public void deletar(Long idCandidato, Long idFormacao) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        boolean removido = candidato.getFormacoes()
                .removeIf(f -> f.getIdFormacao().equals(idFormacao));

        if (!removido) {
            throw new RuntimeException("Formação não encontrada");
        }
    }

    // =========================
    // MAPPER
    // =========================
    private FormacaoResponse mapToResponse(Formacao formacao) {
        return new FormacaoResponse(
                formacao.getIdFormacao(),
                formacao.getNomeCurso(),
                formacao.getInstituicao(),
                formacao.getStatus(),
                formacao.getPeriodoInicio(),
                formacao.getPeriodoFim()
        );
    }
}
