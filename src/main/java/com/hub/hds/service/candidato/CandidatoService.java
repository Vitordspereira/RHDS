package com.hub.hds.service.candidato;

import com.hub.hds.dto.candidato.CandidatoRequest;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;

    public CandidatoService(
            CandidatoRepository candidatoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.candidatoRepository = candidatoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //CRIAR CANDIDATO
    public CandidatoResponse criar(CandidatoRequest request) {

        Candidato candidato = Candidato.builder()
                .nomeCompleto(request.nomeCompleto())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .telefone(request.telefone())
                .cpf(request.cpf())
                .genero(request.genero())
                .dataNascimento(request.dataNascimento())
                .cidade(request.cidade())
                .estado(request.estado())
                .build();

        candidatoRepository.save(candidato);

        return mapToResponse(candidato);
    }

    //ATUALIZAR CANDIDATO
    public CandidatoResponse atualizar(Long id, CandidatoRequest request) {

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        candidato.setNomeCompleto(request.nomeCompleto());
        candidato.setTelefone(request.telefone());
        candidato.setCidade(request.cidade());
        candidato.setEstado(request.estado());
        candidato.setGenero(request.genero());

        return mapToResponse(candidato);
    }

    // DELETAR
    public void deletar(Long id) {
        candidatoRepository.deleteById(id);
    }

    //BUSCAR POR ID
    public CandidatoResponse buscarCompleto(Long id) {

        Candidato candidato = candidatoRepository.findComExperienciasById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        return mapToResponse(candidato);
    }
    //LISTAR TODOS OS CANDIDATOS
    public List<CandidatoResponse> listarTodos() {

        return candidatoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private CandidatoResponse mapToResponse(Candidato candidato) {
        return new CandidatoResponse(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getEmail(),
                candidato.getTelefone(),
                candidato.getCpf(),
                candidato.getGenero(),
                candidato.getDataNascimento(),
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getExperiencias()
                        .stream()
                        .map(e -> new ExperienciaResponse(
                                e.getIdExperiencia(),
                                e.getNomeEmpresa(),
                                e.getFuncao(),
                                e.getDescricao(),
                                e.getOutrasExperiencias(),
                                e.getHabilidades(),
                                e.getPeriodoInicio(),
                                e.getPeriodoFim()
                        ))
                        .toList()
        );
    }
}

