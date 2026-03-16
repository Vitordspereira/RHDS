package com.hub.hds.controller.candidatura;

import com.hub.hds.dto.candidatura.CandidaturaRequest;
import com.hub.hds.dto.candidatura.CandidaturaResponse;
import com.hub.hds.dto.candidatura.CandidaturaStatus;
import com.hub.hds.dto.candidatura.Mensagem;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.candidatura.CandidaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;
    private final CandidatoRepository candidatoRepository;

    public CandidaturaController(CandidaturaService candidaturaService, CandidatoRepository candidatoRepository) {
        this.candidaturaService = candidaturaService;
        this.candidatoRepository = candidatoRepository;
    }

    @PostMapping
    public ResponseEntity<Mensagem<CandidaturaResponse>> criarCandidatura(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal String email
    ) {
        Long vagaId = asLong(payload.get("vagaId"));
        if (vagaId == null) vagaId = asLong(payload.get("idVaga"));
        if (vagaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vagaId é obrigatório");
        }

        Long candidatoId = asLong(payload.get("candidatoId"));
        if (candidatoId == null) candidatoId = asLong(payload.get("idCandidato"));
        if (candidatoId == null && email != null && !email.isBlank()) {
            candidatoId = candidatoRepository.findByUsuario_Email(email)
                    .map(c -> c.getIdCandidato())
                    .orElse(null);
        }
        if (candidatoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "candidatoId é obrigatório");
        }

        CandidaturaResponse response = candidaturaService.criarCandidatura(new CandidaturaRequest(candidatoId, vagaId));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Mensagem<>("Candidatura realizada com sucesso", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Mensagem<CandidaturaResponse>> alterarStatus(
            @PathVariable Long id,
            @RequestBody CandidaturaStatus status
    ) {
        Long idUsuarioAvaliador = 1L;
        CandidaturaResponse response = candidaturaService.alterarStatus(id, status, idUsuarioAvaliador);
        return ResponseEntity.ok(new Mensagem<>("Status da candidatura atualizado com sucesso", response));
    }

    private Long asLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.longValue();
        try {
            String s = String.valueOf(value).trim();
            if (s.isEmpty()) return null;
            return Long.parseLong(s);
        } catch (Exception ignored) {
            return null;
        }
    }
}
