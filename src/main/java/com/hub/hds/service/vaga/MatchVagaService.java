package com.hub.hds.service.vaga;

import com.hub.hds.dto.vaga.match.MatchVagaDTO;
import com.hub.hds.dto.vaga.match.MatchVagaRecomendadoDTO;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.vaga.StatusVaga;
import com.hub.hds.models.vaga.Vaga;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.vaga.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchVagaService {

    private final VagaRepository vagaRepository;
    private final CandidatoRepository candidatoRepository;

    // =========================
    // CALCULAR MATCH
    // =========================
    public MatchVagaDTO calcular(Long idVaga, Long idCandidato) {

        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada"));

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        int score = 0;

        score += matchCargo(candidato, vaga);        // até 30
        score += matchExperiencia(candidato, vaga);  // até 25
        score += matchSenioridade(candidato, vaga);  // até 15
        score += matchLocal(candidato, vaga);        // até 15
        score += matchFormacao(candidato, vaga);     // até 10
        score += matchRequisitos(candidato, vaga);   // até 5

        double percentual = calcularPercentual(score, 100);

        return new MatchVagaDTO(
                vaga.getIdVaga(),
                candidato.getIdCandidato(),
                percentual,
                nivel(percentual)
        );
    }

    // =========================
    // REGRAS DE MATCH
    // =========================
    private int matchCargo(Candidato candidato, Vaga vaga) {
        if (vaga.getCargo() == null) return 0;

        String cargoVaga = vaga.getCargo().toLowerCase();

        return candidato.getExperiencias().stream()
                .anyMatch(exp ->
                        exp.getCargo() != null &&
                                exp.getCargo().toLowerCase().contains(cargoVaga)
                ) ? 30 : 0;
    }

    private int matchExperiencia(Candidato candidato, Vaga vaga) {
        if (vaga.getCargo() == null) return 0;
        return candidato.getExperiencias().isEmpty() ? 0 : 25;
    }

    private int matchSenioridade(Candidato candidato, Vaga vaga) {
        // ainda não existe senioridade no modelo
        if (vaga.getCargo() == null || candidato.getExperiencias().isEmpty()) {
            return 0;
        }
        return 5;
    }

    private int matchLocal(Candidato candidato, Vaga vaga) {
        if (vaga.getLocalizacao() == null) return 0;

        String cidadeVaga = vaga.getLocalizacao().getCidade();
        String cidadeCand = candidato.getCidade();

        if (cidadeVaga == null || cidadeCand == null) return 0;

        return cidadeVaga.equalsIgnoreCase(cidadeCand) ? 15 : 0;
    }

    private int matchFormacao(Candidato candidato, Vaga vaga) {
        if (vaga.getCargo() == null) return 0;
        return candidato.getFormacoes().isEmpty() ? 0 : 10;
    }

    private int matchRequisitos(Candidato candidato, Vaga vaga) {
        // ainda não existe requisitos/skills no modelo
        if (vaga.getCargo() == null || candidato.getExperiencias().isEmpty()) {
            return 0;
        }
        return 5;
    }

    // =========================
    // NORMALIZAÇÃO PROFISSIONAL
    // =========================
    private double calcularPercentual(int score, int scoreMaximo) {
        return Math.round((score * 100.0 / scoreMaximo) * 100.0) / 100.0;
    }

    private String nivel(double percentual) {
        if (percentual >= 75) return "ALTO";
        if (percentual >= 50) return "MEDIO";
        return "BAIXO";
    }

    // =========================
    // VAGAS RECOMENDADAS
    // =========================
    public List<MatchVagaRecomendadoDTO> vagasRecomendadas(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        return vagaRepository.buscarVagasParaMatch(
                        StatusVaga.ABERTA,
                        candidato.getCidade()
                ).stream()
                .map(vaga -> {
                    MatchVagaDTO match =
                            calcular(vaga.getIdVaga(), candidato.getIdCandidato());

                    return new MatchVagaRecomendadoDTO(
                            vaga.getIdVaga(),
                            vaga.getTituloFinal(),
                            match.percentual(),
                            match.nivel()
                    );
                })
                .sorted((a, b) ->
                        Double.compare(b.percentual(), a.percentual()))
                .toList();
    }
}
