package com.hub.hds.service.usuario;

import com.hub.hds.dto.usuario.UsuarioRequest;
import com.hub.hds.dto.usuario.UsuarioResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.usuario.Role;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import com.hub.hds.service.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            CandidatoRepository candidatoRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.candidatoRepository = candidatoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // MÉTODO BASE DE AUTENTICAÇÃO
    // =========================
    private Usuario autenticarBase(UsuarioRequest req) {

        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Email ou senha inválidos"
                        )
                );

        if (!passwordEncoder.matches(req.senha(), usuario.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha inválidos"
            );
        }

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário inativo"
            );
        }

        return usuario;
    }

    // =========================
    // LOGIN CANDIDATO
    // =========================
    public UsuarioResponse loginCandidato(UsuarioRequest req) {

        Usuario usuario = autenticarBase(req);

        if (usuario.getRole() != Role.CANDIDATO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário não é candidato"
            );
        }

        Candidato candidato = candidatoRepository.findByUsuario(usuario)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Candidato não encontrado"
                        )
                );

        String token = gerarToken(usuario, candidato.getIdCandidato());

        return new UsuarioResponse(token, usuario.getRole().name());
    }

    // =========================
    // LOGIN EMPRESA
    // =========================
    public UsuarioResponse loginEmpresa(UsuarioRequest req) {

        Usuario usuario = autenticarBase(req);

        if (usuario.getRole() != Role.RECRUTADOR) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário não é empresa"
            );
        }

        String token = gerarToken(usuario, null);

        return new UsuarioResponse(token, usuario.getRole().name());
    }

    // =========================
    // TOKEN CENTRALIZADO
    // =========================
    private String gerarToken(Usuario usuario, Long idCandidato) {
        return jwtService.gerarToken(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                idCandidato,
                usuario.getRole().name()
        );
    }
}
