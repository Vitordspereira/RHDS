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

    // ✅ POST — cria experiência para um candidato
    public ExperienciaResponse criar(ExperienciaRequest request) {

        Candidato candidato = candidatoRepository.findById(request.idCandidato())
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

        candidatoRepository.save(candidato);

        return mapToResponse(experiencia);
    }

    // GET — lista experiências de um candidato
    public List<ExperienciaResponse> listarPorCandidato(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        return candidato.getExperiencias()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // PUT — atualiza experiência do candidato
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

    // DELETE — remove experiência do candidato
    public void deletar(Long idCandidato, Long idExperiencia) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        candidato.getExperiencias()
                .removeIf(e -> e.getIdExperiencia().equals(idExperiencia));

        candidatoRepository.save(candidato);
    }

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

