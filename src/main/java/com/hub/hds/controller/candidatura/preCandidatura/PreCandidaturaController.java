package com.hub.hds.controller.candidatura.preCandidatura;

import com.hub.hds.models.candidatura.preCandidatura.PreCandidatura;
import com.hub.hds.service.candidatura.preCandidatura.PreCandidaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pre-candidaturas")
public class PreCandidaturaController {

    private final PreCandidaturaService preCandidaturaService;

    public PreCandidaturaController(PreCandidaturaService preCandidaturaService) {
        this.preCandidaturaService = preCandidaturaService;
    }

    // =========================
    // 1️⃣ INICIAR PRÉ-CANDIDATURA
    // =========================
    @PostMapping("/{idVaga}")
    public ResponseEntity<?> iniciar(
            @PathVariable Long idVaga,
            @RequestParam String email
    ) {
        try {
            PreCandidatura pre = preCandidaturaService.iniciar(idVaga, email);

            // Retorna o token para o frontend
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "token", pre.getTokenConfirmacao(),
                            "message", "Token gerado e enviado com sucesso"
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // =========================
    // 2️⃣ VALIDAR TOKEN (FRONT)
    // =========================
    @GetMapping("/validar")
    public ResponseEntity<?> validarToken(
            @RequestParam String token
    ) {
        try {
            PreCandidatura pre = preCandidaturaService.validarToken(token);

            return ResponseEntity.ok(
                    Map.of(
                            "email", pre.getEmail(),
                            "vagaId", pre.getVaga().getIdVaga(),
                            "status", pre.getStatusPreCandidatura()
                    )
            );
        } catch (Exception e) {
            // Retorna um erro 400 com mensagem de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Token inválido ou expirado"));
        }
    }

    // =========================
    // 3️⃣ CONFIRMAR NA TELA
    // =========================
    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmar(@RequestParam String token) {
        try {
            preCandidaturaService.confirmarEConverter(token);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}



