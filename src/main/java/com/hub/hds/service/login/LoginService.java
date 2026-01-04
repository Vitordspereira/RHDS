package com.hub.hds.service.login;

import com.hub.hds.dto.login.LoginRequest;
import com.hub.hds.dto.login.LoginResponse;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.usuario.UsuarioRepository;
import com.hub.hds.service.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse autenticar(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválido"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválido");
        }

        String role = usuario.getRole().name();
        String tipo = role;

        String token = jwtService.gerarToken(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                tipo,          // tipo
                role           // role (SEM "ROLE_")
        );

        return new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getEmail(),
                token
        );
    }
}

