package com.hub.hds.service.dashboard;

import com.hub.hds.dto.dashboardCandidato.CandidatoDashboardDTO;
import com.hub.hds.dto.dashboardCandidato.ExperienciaDTO;
import com.hub.hds.dto.dashboardCandidato.FormacaoDTO;
import com.hub.hds.dto.dashboardCandidato.ListarVagas.VagaCandidaturaDTO;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.candidatura.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoDashboardService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    private final CandidaturaRepository candidaturaRepository;

    public CandidatoDashboardService(
            CandidaturaRepository candidaturaRepository
    ){
        this.candidaturaRepository = candidaturaRepository;
    }

    public CandidatoDashboardDTO montarDashboard(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        List<ExperienciaDTO> experiencias = candidato.getExperiencias()
                .stream()
                .map(exp -> new ExperienciaDTO(
                        exp.getIdExperiencia(),
                        exp.getCargo(),
                        exp.getEmpresa(),
                        exp.getDescricao(),
                        exp.getDataInicio(),
                        exp.getDataFim()
                ))
                .toList();

        List<FormacaoDTO> formacoes = candidato.getFormacoes()
                .stream()
                .map(f -> new FormacaoDTO(
                        f.getIdFormacao(),
                        f.getCurso(),
                        f.getInstituicao(),
                        f.getDataInicio(),
                        f.getDataFim()
                ))
                .toList();

        return new CandidatoDashboardDTO(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getCpf(),
                candidato.getTelefone(),
                candidato.getGenero(),
                candidato.getDataNascimento(),
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getVideoApresentacao(),
                experiencias,
                formacoes
        );
    }

    public List<VagaCandidaturaDTO> listarVagasCandidatadas (Long idCandidato){
        return candidaturaRepository
                .findByCandidato_IdCandidato(idCandidato)
                .stream()
                .map(VagaCandidaturaDTO::fromEntity)
                .toList();
    }
}

