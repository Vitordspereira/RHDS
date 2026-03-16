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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Long cadastrar(CandidatoCadastroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.CANDIDATO);
        usuarioRepository.save(usuario);

        Candidato candidato = new Candidato();
        candidato.setNomeCompleto(request.nomeCompleto());
        candidato.setCpf(request.cpf());
        candidato.setTelefone(request.telefone());
        candidato.setGenero(request.genero());
        candidato.setDataNascimento(request.dataNascimento());
        candidato.setCidade(request.cidade());
        candidato.setEstado(request.estado());
        candidato.setUsuario(usuario);

        applyExperiencias(candidato, request.experiencias());
        applyFormacoes(candidato, request.formacoes());

        candidatoRepository.save(candidato);
        return candidato.getIdCandidato();
    }

    @Transactional(readOnly = true)
    public CandidatoPerfilDTO buscarPorEmail(String email) {
        Candidato candidato = candidatoRepository.findByUsuario_Email(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));
        return toPerfilDTO(candidato);
    }

    @Transactional(readOnly = true)
    public CandidatoPerfilDTO buscarPorId(Long id) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));
        return toPerfilDTO(candidato);
    }

    @Transactional
    public void atualizar(Long id, CandidatoCadastroRequest request, String emailUsuario) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        if (emailUsuario != null && !emailUsuario.isBlank()) {
            String emailDono = candidato.getUsuario() != null ? candidato.getUsuario().getEmail() : null;
            if (emailDono != null && !emailDono.equalsIgnoreCase(emailUsuario)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Candidato não pertence ao usuário autenticado");
            }
        }

        candidato.setNomeCompleto(defaultIfNull(request.nomeCompleto(), candidato.getNomeCompleto()));
        candidato.setCpf(defaultIfNull(request.cpf(), candidato.getCpf()));
        candidato.setTelefone(defaultIfNull(request.telefone(), candidato.getTelefone()));
        candidato.setGenero(request.genero() != null ? request.genero() : candidato.getGenero());
        candidato.setDataNascimento(request.dataNascimento() != null ? request.dataNascimento() : candidato.getDataNascimento());
        candidato.setCidade(defaultIfNull(request.cidade(), candidato.getCidade()));
        candidato.setEstado(defaultIfNull(request.estado(), candidato.getEstado()));

        if (request.experiencias() != null) {
            applyExperiencias(candidato, request.experiencias());
        }
        if (request.formacoes() != null) {
            applyFormacoes(candidato, request.formacoes());
        }

        if (request.email() != null && !request.email().isBlank()) {
            Usuario usuario = candidato.getUsuario();
            if (usuario != null && !request.email().equalsIgnoreCase(usuario.getEmail())) {
                if (usuarioRepository.existsByEmail(request.email())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
                }
                usuario.setEmail(request.email());
            }
        }

        if (request.senha() != null && !request.senha().isBlank()) {
            Usuario usuario = candidato.getUsuario();
            if (usuario != null) {
                usuario.setSenha(passwordEncoder.encode(request.senha()));
            }
        }

        candidatoRepository.save(candidato);
    }

    @Transactional
    public void deletar(Long id, String emailUsuario) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        if (emailUsuario != null && !emailUsuario.isBlank()) {
            String emailDono = candidato.getUsuario() != null ? candidato.getUsuario().getEmail() : null;
            if (emailDono != null && !emailDono.equalsIgnoreCase(emailUsuario)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Candidato não pertence ao usuário autenticado");
            }
        }

        candidatoRepository.delete(candidato);
    }

    public List<CandidatoCadastroResponse> listar() {
        return candidatoRepository.findAll().stream().map(CandidatoCadastroResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public CandidatoPerfilDTO buscarPerfilProfissional(Long idCandidato) {
        return buscarPorId(idCandidato);
    }

    private void applyExperiencias(Candidato candidato, List<ExperienciaRequest> experiencias) {
        candidato.getExperiencias().clear();
        if (experiencias == null) return;
        for (ExperienciaRequest expReq : experiencias) {
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

    private void applyFormacoes(Candidato candidato, List<FormacaoRequest> formacoes) {
        candidato.getFormacoes().clear();
        if (formacoes == null) return;
        for (FormacaoRequest formReq : formacoes) {
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

    private CandidatoPerfilDTO toPerfilDTO(Candidato candidato) {
        return new CandidatoPerfilDTO(
                candidato.getIdCandidato(),
                candidato.getNomeCompleto(),
                candidato.getTelefone(),
                candidato.getGenero(),
                candidato.getDataNascimento() != null ? candidato.getDataNascimento().toString() : null,
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getUsuario() != null ? candidato.getUsuario().getEmail() : null,
                candidato.getExperiencias().stream().map(ExperienciaDTO::fromEntity).toList(),
                candidato.getFormacoes().stream().map(FormacaoDTO::fromEntity).toList(),
                candidato.getVideoApresentacao()
        );
    }

    private String defaultIfNull(String novo, String atual) {
        return novo != null ? novo : atual;
    }
}
