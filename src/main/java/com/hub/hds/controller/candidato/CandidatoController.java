package com.hub.hds.controller.candidato;

import com.hub.hds.dto.candidato.CandidatoRequest;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.service.candidato.CandidatoService;
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

    // ✅ POST — CRIAR CANDIDATO (com senha)
    @PostMapping
    public CandidatoResponse criar(@RequestBody CandidatoRequest request) {
        return candidatoService.criar(request);
    }

    // ✅ PUT — ATUALIZAR CANDIDATO
    @PutMapping("/{id}")
    public CandidatoResponse atualizar(
            @PathVariable Long id,
            @RequestBody CandidatoRequest request
    ) {
        return candidatoService.atualizar(id, request);
    }

    // ✅ DELETE — DELETAR CANDIDATO
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        candidatoService.deletar(id);
    }

    // ✅ GET — BUSCAR CANDIDATO COMPLETO (COM EXPERIÊNCIAS)
    @GetMapping("/{id}")
    public CandidatoResponse buscarPorId(@PathVariable Long id) {
        return candidatoService.buscarCompleto(id);
    }

    // ✅ GET — LISTAR TODOS OS CANDIDATOS
    @GetMapping
    public List<CandidatoResponse> listarTodos() {
        return candidatoService.listarTodos();
    }
}

