package com.hub.hds.service.login;

import com.hub.hds.dto.login.LoginRequest;
import com.hub.hds.dto.login.LoginResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class LoginService {

    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(CandidatoRepository candidatoRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.candidatoRepository = candidatoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    public LoginResponse autenticar(LoginRequest request) {

        Candidato candidato = candidatoRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválido"));

        if (!passwordEncoder.matches(request.senha(), candidato.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválido");
        }

        String token = jwtService.gerarToken(
                candidato.getId_candidato(),
                candidato.getEmail(),
                "CANDIDATO",
                "ROLE_CANDIDATO"
        );

        return new LoginResponse(
                candidato.getId_candidato(),
                candidato.getNome_completo(),
                candidato.getEmail(),
                token
        );
    }
}
