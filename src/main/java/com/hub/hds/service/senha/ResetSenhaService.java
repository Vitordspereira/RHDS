package com.hub.hds.service.senha;


import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.senha.ResetSenha;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.senha.ResetSenhaRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import com.hub.hds.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final ResetSenhaRepository resetSenhaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

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

    public void solicitarReset(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        String token = UUID.randomUUID().toString();

        ResetSenha reset = new ResetSenha();
        reset.setUsuario(usuario);
        reset.setToken(token);
        reset.setExpiraEm(LocalDateTime.now().plusMinutes(30));
        reset.setUtilizado(false);

        resetSenhaRepository.save(reset);

        String link = "http://localhost:3000/resetar-senha?token=" + token;

        emailService.enviarEmail(
                usuario.getEmail(),
                "Reset de senha",
                "Clique no link para redefinir sua senha:\n" + link
        );
    }

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




