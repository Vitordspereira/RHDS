package com.hub.hds.controller.candidato;

import com.hub.hds.dto.candidato.CandidatoCadastroDTO;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.dto.candidato.CandidatoCompletoResponse;
import com.hub.hds.dto.candidato.CandidatoUpdateDTO;
import com.hub.hds.service.candidato.CandidatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }


    // CADASTRO INICIAL (COM LOGIN)
    @PostMapping("/cadastro")
    public ResponseEntity<CandidatoResponse> cadastrar(
            @RequestBody @Valid CandidatoCadastroDTO request
    ) {
        CandidatoResponse response = candidatoService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LISTAR TODOS (BÁSICO)
    @GetMapping
    public ResponseEntity<List<CandidatoResponse>> listarTodos() {
        List<CandidatoResponse> lista = candidatoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // BUSCAR CANDIDATO COMPLETO
    @GetMapping("/{id}")
    public ResponseEntity<CandidatoCompletoResponse> buscarCompleto(
            @PathVariable Long id
    ) {
        CandidatoCompletoResponse response =
                candidatoService.buscarCompleto(id);
        return ResponseEntity.ok(response);
    }

    // ATUALIZAR PERFIL
    @PutMapping("/{id}")
    public ResponseEntity<CandidatoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CandidatoUpdateDTO request
    ) {
        CandidatoResponse response =
                candidatoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETAR
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        candidatoService.deletar(id);
    }
}
