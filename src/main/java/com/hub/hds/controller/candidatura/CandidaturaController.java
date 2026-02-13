package com.hub.hds.controller.candidatura;

import com.hub.hds.dto.candidatura.CandidaturaRequest;
import com.hub.hds.dto.candidatura.CandidaturaResponse;
import com.hub.hds.dto.candidatura.CandidaturaStatus;
import com.hub.hds.dto.candidatura.Mensagem;
import com.hub.hds.service.candidatura.CandidaturaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    public CandidaturaController(CandidaturaService candidaturaService) {
        this.candidaturaService = candidaturaService;
    }

    // =========================
    // 1️⃣ CANDIDATO – CRIAR CANDIDATURA
    // =========================
    @PostMapping
    public ResponseEntity<Mensagem<CandidaturaResponse>> criarCandidatura(
            @Valid @RequestBody CandidaturaRequest request
    ) {

        CandidaturaResponse response =
                candidaturaService.criarCandidatura(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new Mensagem<>(
                                "Candidatura realizada com sucesso",
                                response
                        )
                );
    }

    // =========================
    // 2️⃣ ADMIN – ALTERAR STATUS DA CANDIDATURA
    // =========================
    @PatchMapping("/{id}/status")
    public ResponseEntity<Mensagem<CandidaturaResponse>> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody CandidaturaStatus status
    ) {

        // enquanto não houver autenticação
        Long idUsuarioAvaliador = 1L;

        CandidaturaResponse response =
                candidaturaService.alterarStatus(
                        id,
                        status,
                        idUsuarioAvaliador
                );

        return ResponseEntity.ok(
                new Mensagem<>(
                        "Status da candidatura atualizado com sucesso",
                        response
                )
        );
    }
}
