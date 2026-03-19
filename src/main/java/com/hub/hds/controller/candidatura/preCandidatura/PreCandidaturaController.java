package com.hub.hds.controller.candidatura.preCandidatura;

import com.hub.hds.service.candidatura.preCandidatura.PreCandidaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pre-candidaturas")
public class PreCandidaturaController {

    private final PreCandidaturaService preCandidaturaService;

    public PreCandidaturaController(PreCandidaturaService preCandidaturaService) {
        this.preCandidaturaService = preCandidaturaService;
    }

    @PostMapping("/{idVaga}")
    public ResponseEntity<?> iniciar(
            @PathVariable Long idVaga,
            @RequestParam String email
    ) {
        try {
            preCandidaturaService.iniciar(idVaga, email);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "success", true,
                            "message", "Pré-candidatura iniciada com sucesso."
                    )
            );
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao iniciar pré-candidatura. vagaId={}, email={}", idVaga, email, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        } catch (Exception e) {
            log.error("Erro interno ao iniciar pré-candidatura. vagaId={}, email={}", idVaga, email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "success", false,
                            "error", "Erro interno ao processar a pré-candidatura."
                    )
            );
        }
    }

    @GetMapping("/validar")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        try {
            var pre = preCandidaturaService.validarToken(token);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "email", pre.getEmail(),
                            "vagaId", pre.getVaga().getIdVaga(),
                            "status", pre.getStatusPreCandidatura().name()
                    )
            );
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao validar token. token={}", token, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        } catch (Exception e) {
            log.error("Erro interno ao validar token. token={}", token, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "success", false,
                            "error", "Erro interno ao validar token."
                    )
            );
        }
    }

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
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao confirmar pré-candidatura. token={}", token, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        } catch (Exception e) {
            log.error("Erro interno ao confirmar pré-candidatura. token={}", token, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "success", false,
                            "error", "Erro interno ao confirmar pré-candidatura."
                    )
            );
        }
    }
}


