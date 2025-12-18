package com.hub.hds.service.senha;


import com.hub.hds.dto.senha.ResetSenhaRequest;
import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.senha.ResetSenha;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.senha.ResetSenhaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetSenhaService {

    private final CandidatoRepository candidatoRepository;
    private final ResetSenhaRepository resetSenhaRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetSenhaService(
            CandidatoRepository candidatoRepository,
            ResetSenhaRepository resetSenhaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.candidatoRepository = candidatoRepository;
        this.resetSenhaRepository = resetSenhaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String solicitarReset(ResetSenhaRequest request) {

        Candidato candidato = candidatoRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

        String token = UUID.randomUUID().toString();

        ResetSenha reset = ResetSenha.builder()
                .candidato(candidato)
                .token(token)
                .expira_em(LocalDateTime.now().plusMinutes(10))
                .utilizado(false)
                .build();

        resetSenhaRepository.save(reset);

        return token;
    }

    public String confirmarReset(ResetSenhaResponse request) {

        ResetSenha reset = resetSenhaRepository.findByToken(request.token())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (reset.isUtilizado())
            throw new RuntimeException("Token já foi utilizado");

        if (reset.getExpira_em().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expirado");

        // Atualiza senha
        Candidato candidato = reset.getCandidato();
        candidato.setSenha(passwordEncoder.encode(request.novaSenha()));
        candidatoRepository.save(candidato);

        // Marca token como utilizado
        reset.setUtilizado(true);
        resetSenhaRepository.save(reset);

        return "Senha atualizada com sucesso";
    }
}

