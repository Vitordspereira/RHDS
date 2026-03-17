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
            preCandidaturaService.iniciar(idVaga, email);

            return ResponseEntity.status(HttpStatus.CREATED).body(
              Map.of(
                      "sucess", true,
                      "message", "Pré-candidatura iniciada com sucesso."
              )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "sucess", false,
                            "error", e.getMessage()
                    )
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
                            "status", pre.getStatusPreCandidatura().name()
                    )
            );
        } catch (Exception e) {
            // Retorna um erro 400 com mensagem de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("sucess", false, "error", e.getMessage()));
        }
    }

    // =========================
    // 3️⃣ CONFIRMAR NA TELA
    // =========================
    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestParam String token) {
        try {
            preCandidaturaService.confirmarEConverter(token);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Candidatura confirmada com sucesso."
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        }
    }
}



