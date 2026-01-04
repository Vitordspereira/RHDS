package com.hub.hds.service.experiencia;

import com.hub.hds.dto.experiencia.ExperienciaRequest;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.experiencia.Experiencia;
import com.hub.hds.repository.candidato.CandidatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ExperienciaService {

    private final CandidatoRepository candidatoRepository;

    public ExperienciaService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    // CRIAR EXPERIÊNCIA
    public ExperienciaResponse criar(Long idCandidato, ExperienciaRequest request) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Experiencia experiencia = Experiencia.builder()
                .nomeEmpresa(request.nomeEmpresa())
                .funcao(request.funcao())
                .descricao(request.descricao())
                .outrasExperiencias(request.outrasExperiencias())
                .habilidades(request.habilidades())
                .periodoInicio(request.periodoInicio())
                .periodoFim(request.periodoFim())
                .build();

        candidato.adicionarExperiencia(experiencia);

        return mapToResponse(experiencia);
    }

    // LISTAR EXPERIÊNCIAS DO CANDIDATO
    public List<ExperienciaResponse> listarPorCandidato(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        return candidato.getExperiencias()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ATUALIZAR EXPERIÊNCIA
    public ExperienciaResponse atualizar(
            Long idCandidato,
            Long idExperiencia,
            ExperienciaRequest request
    ) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Experiencia experiencia = candidato.getExperiencias()
                .stream()
                .filter(e -> e.getIdExperiencia().equals(idExperiencia))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada"));

        experiencia.setNomeEmpresa(request.nomeEmpresa());
        experiencia.setFuncao(request.funcao());
        experiencia.setDescricao(request.descricao());
        experiencia.setOutrasExperiencias(request.outrasExperiencias());
        experiencia.setHabilidades(request.habilidades());
        experiencia.setPeriodoInicio(request.periodoInicio());
        experiencia.setPeriodoFim(request.periodoFim());

        return mapToResponse(experiencia);
    }


    // DELETAR EXPERIÊNCIA
    public void deletar(Long idCandidato, Long idExperiencia) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        boolean removido = candidato.getExperiencias()
                .removeIf(e -> e.getIdExperiencia().equals(idExperiencia));

        if (!removido) {
            throw new RuntimeException("Experiência não encontrada");
        }
    }

    // MapToResponse
    private ExperienciaResponse mapToResponse(Experiencia experiencia) {
        return new ExperienciaResponse(
                experiencia.getIdExperiencia(),
                experiencia.getNomeEmpresa(),
                experiencia.getFuncao(),
                experiencia.getDescricao(),
                experiencia.getOutrasExperiencias(),
                experiencia.getHabilidades(),
                experiencia.getPeriodoInicio(),
                experiencia.getPeriodoFim()
        );
    }
}
