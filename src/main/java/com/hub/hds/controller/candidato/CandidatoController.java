package com.hub.hds.controller.candidato;

import com.hub.hds.dto.candidato.CandidatoCadastroRequest;
import com.hub.hds.dto.candidato.CandidatoCadastroResponse;
import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoPerfilDTO;
import com.hub.hds.service.candidato.CandidatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(
            @Valid @RequestBody CandidatoCadastroRequest request
    ) {
        Long candidatoId = candidatoService.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Candidato cadastrado com sucesso",
                        "candidatoId", candidatoId
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<CandidatoCadastroResponse>> listar() {
        return ResponseEntity.ok(candidatoService.listar());
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<CandidatoPerfilDTO> buscarPorId(
            @PathVariable Long id
    ) {
        CandidatoPerfilDTO candidato = candidatoService.buscarPorId(id);
        return ResponseEntity.ok(candidato);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CandidatoCadastroRequest request
    ) {
        candidatoService.atualizar(id, request);

        return ResponseEntity.ok(
                Map.of("message", "Candidato atualizado com sucesso")
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {

        candidatoService.deletar(id);

        return ResponseEntity.ok(
                Map.of("message", "Candidato removido com sucesso")
        );
    }
}


