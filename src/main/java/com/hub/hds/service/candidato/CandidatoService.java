package com.hub.hds.service.candidato;

import com.hub.hds.dto.candidato.CandidatoCadastroRequest;
import com.hub.hds.dto.candidato.CandidatoCadastroResponse;
import com.hub.hds.dto.candidato.ExperienciaRequest;
import com.hub.hds.dto.candidato.FormacaoRequest;
import com.hub.hds.dto.dashboardCandidato.ExperienciaDTO;
import com.hub.hds.dto.dashboardCandidato.FormacaoDTO;
import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoPerfilDTO;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.candidato.Experiencia;
import com.hub.hds.models.candidato.Formacao;
import com.hub.hds.models.usuario.Role;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CandidatoService {

    private final UsuarioRepository usuarioRepository;
    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;

    public CandidatoService(
            UsuarioRepository usuarioRepository,
            CandidatoRepository candidatoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.candidatoRepository = candidatoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Transactional
    public Long cadastrar(CandidatoCadastroRequest request) {

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.CANDIDATO);
        usuarioRepository.save(usuario);

        Candidato candidato = Candidato.builder()
                .nomeCompleto(request.nomeCompleto())
                .cpf(request.cpf())
                .telefone(request.telefone())
                .genero(request.genero())
                .dataNascimento(request.dataNascimento())
                .cidade(request.cidade())
                .estado(request.estado())
                .usuario(usuario)
                .build();

        if (request.experiencias() != null) {
            for (ExperienciaRequest expReq : request.experiencias()) {
                Experiencia exp = Experiencia.builder()
                        .empresa(expReq.empresa())
                        .cargo(expReq.cargo())
                        .descricao(expReq.descricao())
                        .dataInicio(expReq.dataInicio())
                        .dataFim(expReq.dataFim())
                        .atual(expReq.atual())
                        .candidato(candidato)
                        .build();
                candidato.getExperiencias().add(exp);
            }
        }

        if (request.formacoes() != null) {
            for (FormacaoRequest formReq : request.formacoes()) {
                Formacao form = Formacao.builder()
                        .instituicao(formReq.instituicao())
                        .curso(formReq.curso())
                        .nivelFormacao(formReq.nivelFormacao())
                        .statusFormacao(formReq.statusFormacao())
                        .dataInicio(formReq.dataInicio())
                        .dataFim(formReq.dataFim())
                        .candidato(candidato)
                        .build();
                candidato.getFormacoes().add(form);
            }
        }

        candidatoRepository.save(candidato);
        return candidato.getIdCandidato();
    }

    //LISTAR POR ID
    public CandidatoPerfilDTO buscarPorId(Long id) {

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidato não encontrado"
                ));
        return new CandidatoPerfilDTO(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getTelefone(),
                candidato.getGenero(),
                candidato.getDataNascimento() != null
                        ? candidato.getDataNascimento().toString()
                        : null,
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getVideoApresentacao(),
                candidato.getExperiencias()
                        .stream()
                        .map(ExperienciaDTO::fromEntity)
                        .toList(),
                candidato.getFormacoes()
                        .stream()
                        .map(FormacaoDTO::fromEntity)
                        .toList(),
                candidato.getVideoApresentacao()
        );

    }
    //UPDATE
    @Transactional
    public void atualizar(Long id, CandidatoCadastroRequest request) {

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        candidato.setNomeCompleto(request.nomeCompleto());
        candidato.setCpf(request.cpf());
        candidato.setTelefone(request.telefone());
        candidato.setGenero(request.genero());
        candidato.setDataNascimento(request.dataNascimento());
        candidato.setCidade(request.cidade());
        candidato.setEstado(request.estado());

        candidato.getExperiencias().clear();
        if (request.experiencias() != null) {
            for (ExperienciaRequest expReq : request.experiencias()) {
                Experiencia exp = Experiencia.builder()
                        .empresa(expReq.empresa())
                        .cargo(expReq.cargo())
                        .descricao(expReq.descricao())
                        .dataInicio(expReq.dataInicio())
                        .dataFim(expReq.dataFim())
                        .atual(expReq.atual())
                        .candidato(candidato)
                        .build();
                candidato.getExperiencias().add(exp);
            }
        }

        candidato.getFormacoes().clear();
        if (request.formacoes() != null) {
            for (FormacaoRequest formReq : request.formacoes()) {
                Formacao form = Formacao.builder()
                        .instituicao(formReq.instituicao())
                        .curso(formReq.curso())
                        .nivelFormacao(formReq.nivelFormacao())
                        .statusFormacao(formReq.statusFormacao())
                        .dataInicio(formReq.dataInicio())
                        .dataFim(formReq.dataFim())
                        .candidato(candidato)
                        .build();
                candidato.getFormacoes().add(form);
            }
        }

        candidatoRepository.save(candidato);
    }

    //DELETE
    @Transactional
    public void deletar(Long id) {

        if (!candidatoRepository.existsById(id)) {
            throw new RuntimeException("Candidato não encontrado");
        }

        candidatoRepository.deleteById(id);
    }

    // =========================
    // READ - LISTAR
    // =========================
    public List<CandidatoCadastroResponse> listar() {
        return candidatoRepository.findAll()
                .stream()
                .map(CandidatoCadastroResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CandidatoPerfilDTO buscarPerfilProfissional(Long idCandidato) {

        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() ->
                        new RuntimeException("Candidato não encontrado")
                );

        return new CandidatoPerfilDTO(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getTelefone(),
                candidato.getGenero(),
                candidato.getDataNascimento() != null
                        ? candidato.getDataNascimento().toString()
                        : null,
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getVideoApresentacao(),
                candidato.getExperiencias()
                        .stream()
                        .map(ExperienciaDTO::fromEntity)
                        .toList(),
                candidato.getFormacoes()
                        .stream()
                        .map(FormacaoDTO::fromEntity)
                        .toList(),
                candidato.getVideoApresentacao()
        );
    }
}
