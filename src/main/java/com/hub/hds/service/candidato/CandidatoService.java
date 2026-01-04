package com.hub.hds.service.candidato;

import com.hub.hds.dto.candidato.CandidatoCadastroDTO;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.dto.candidato.CandidatoCompletoResponse;
import com.hub.hds.dto.candidato.CandidatoUpdateDTO;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.usuario.Role;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.usuario.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final UsuarioService usuarioService;

    public CandidatoService(
            CandidatoRepository candidatoRepository,
            UsuarioService usuarioService
    ) {
        this.candidatoRepository = candidatoRepository;
        this.usuarioService = usuarioService;
    }

    // CADASTRO INICIAL (COM LOGIN)
    public CandidatoResponse cadastrar(CandidatoCadastroDTO request) {

        // 1️⃣ CRIA USUÁRIO (LOGIN)
        Usuario usuario = usuarioService.criarUsuario(
                request.email(),
                request.senha(),
                Role.CANDIDATO
        );

        String telefoneLimpo = request.telefone()
                .replaceAll("\\D", "");

    // CRIAR CANDIDATO

        Candidato candidato = Candidato.builder()
                .nomeCompleto(request.nomeCompleto())
                .telefone(request.telefone())
                .cpf(request.cpf())
                .genero(request.genero())
                .dataNascimento(request.dataNascimento())
                .cidade(request.cidade())
                .estado(request.estado())
                .usuario(usuario)
                .build();

        candidatoRepository.save(candidato);

        return mapBasico(candidato);
    }

    // ATUALIZAR CANDIDATO
    public CandidatoResponse atualizar(Long id, CandidatoUpdateDTO request) {

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));


        if (request.nomeCompleto() != null) {
            candidato.setNomeCompleto(request.nomeCompleto());
        }

        if (request.telefone() != null) {
            String telefoneLimpo = request.telefone()
                    .replaceAll("\\D", "");
            candidato.setTelefone(telefoneLimpo);
        }

        if (request.genero() != null) {
            candidato.setGenero(request.genero());
        }

        if (request.cidade() != null) {
            candidato.setCidade(request.cidade());
        }

        if (request.estado() != null) {
            candidato.setEstado(request.estado());
        }

        return mapBasico(candidato);
    }

    // DELETAR
    public void deletar(Long id) {
        candidatoRepository.deleteById(id);
    }

    // LISTAR TODOS
    public List<CandidatoResponse> listarTodos() {
        return candidatoRepository.findAll()
                .stream()
                .map(this::mapBasico)
                .toList();
    }

    // BUSCAR CANDIDATO COMPLETO
    public CandidatoCompletoResponse buscarCompleto(Long id) {

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        return mapCompleto(candidato);
    }

    // DTO BÁSICO (SEM EXPERIÊNCIAS E FORMACAO)
    private CandidatoResponse mapBasico(Candidato candidato) {
        return new CandidatoResponse(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getUsuario().getEmail(),
                candidato.getTelefone(),
                candidato.getCpf(),
                candidato.getGenero(),
                candidato.getDataNascimento(),
                candidato.getCidade(),
                candidato.getEstado()
        );
    }

    // DTO COMPLETO (COM EXPERIÊNCIAS E FORMAÇÃO)
    private CandidatoCompletoResponse mapCompleto(Candidato candidato) {
        return new CandidatoCompletoResponse(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getUsuario().getEmail(),
                candidato.getTelefone(),
                candidato.getCpf(),
                candidato.getGenero(),
                candidato.getDataNascimento(),
                candidato.getCidade(),
                candidato.getEstado(),

                //EXPERIENCIA
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
                        .toList(),

                //FORMAÇÃO
                candidato.getFormacoes()
                        .stream()
                        .map(f-> new FormacaoResponse(
                                f.getIdFormacao(),
                                f.getNomeCurso(),
                                f.getInstituicao(),
                                f.getStatus(),
                                f.getPeriodoInicio(),
                                f.getPeriodoFim()
                        ))
                        .toList()
        );
    }
}


