package com.hub.hds.service.senha;

import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.models.senha.ResetSenha;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.senha.ResetSenhaRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import com.hub.hds.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final ResetSenhaRepository resetSenhaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.frontend.reset-senha-url}")
    private String resetSenhaUrl;

    public ResetSenhaService(
            UsuarioRepository usuarioRepository,
            ResetSenhaRepository resetSenhaRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.resetSenhaRepository = resetSenhaRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1️⃣ Solicitar reset de senha
    public void solicitarReset(String email) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return; // segurança: não revelar se existe ou não
        }

        Usuario usuario = usuarioOpt.get();

        String token = UUID.randomUUID().toString();

        ResetSenha reset = ResetSenha.builder()
                .usuario(usuario)
                .token(token)
                .expiraEm(LocalDateTime.now().plusMinutes(30))
                .utilizado(false)
                .build();

        resetSenhaRepository.save(reset);

        String link = resetSenhaUrl + "?token=" +token;

        emailService.enviarEmail(
                usuario.getEmail(),
                "Reset de senha",
                "Clique no link para redefinir sua senha:\n" + link
        );
    }

    // 2️⃣ Redefinir senha
    @Transactional
    public void redefinirSenha(ResetSenhaResponse dto) {

        ResetSenha reset = resetSenhaRepository.findByToken(dto.token())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (reset.isUtilizado()) {
            throw new RuntimeException("Token já utilizado");
        }

        if (reset.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        Usuario usuario = reset.getUsuario();
        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);

        reset.setUtilizado(true);
        resetSenhaRepository.save(reset);
    }
}

