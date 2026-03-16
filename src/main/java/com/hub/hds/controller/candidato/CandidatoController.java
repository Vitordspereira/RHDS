package com.hub.hds.controller.candidato;

import com.hub.hds.dto.candidato.CandidatoCadastroRequest;
import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoPerfilDTO;
import com.hub.hds.service.candidato.CandidatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/candidatos", "/candidato"})
public class CandidatoController {

    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody CandidatoCadastroRequest request) {
        Long id = candidatoService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Candidato cadastrado com sucesso",
                        "idCandidato", id
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<CandidatoPerfilDTO> buscarMe(@AuthenticationPrincipal String emailUsuario) {
        return ResponseEntity.ok(candidatoService.buscarPorEmail(emailUsuario));
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<CandidatoPerfilDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(candidatoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CandidatoCadastroRequest request,
            @AuthenticationPrincipal String emailUsuario
    ) {
        candidatoService.atualizar(id, request, emailUsuario);
        return ResponseEntity.ok(Map.of("message", "Candidato atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal String emailUsuario
    ) {
        candidatoService.deletar(id, emailUsuario);
        return ResponseEntity.ok(Map.of("message", "Candidato removido com sucesso"));
    }
}
