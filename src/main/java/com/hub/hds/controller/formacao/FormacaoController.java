package com.hub.hds.controller.formacao;

import com.hub.hds.dto.formacao.FormacaoRequest;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.service.formacao.FormacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/candidatos/{idCandidato}/formacoes")
public class FormacaoController {

    private final FormacaoService formacaoService;

    public FormacaoController(FormacaoService formacaoService) {
        this.formacaoService = formacaoService;
    }

    // CRIAR FORMAÇÃO
    @PostMapping
    public ResponseEntity<FormacaoResponse> criar(
            @PathVariable Long idCandidato,
            @RequestBody @Valid FormacaoRequest request
    ) {
        FormacaoResponse response =
                formacaoService.criar(idCandidato, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // LISTAR FORMAÇÕES DO CANDIDATO
    @GetMapping
    public ResponseEntity<List<FormacaoResponse>> listarPorCandidato(
            @PathVariable Long idCandidato
    ) {
        List<FormacaoResponse> lista =
                formacaoService.listarPorCandidato(idCandidato);

        return ResponseEntity.ok(lista);
    }

    // ATUALIZAR FORMAÇÃO
    @PutMapping("/{idFormacao}")
    public ResponseEntity<FormacaoResponse> atualizar(
            @PathVariable Long idCandidato,
            @PathVariable Long idFormacao,
            @RequestBody @Valid FormacaoRequest request
    ) {
        FormacaoResponse response =
                formacaoService.atualizar(idCandidato, idFormacao, request);

        return ResponseEntity.ok(response);
    }

    // DELETAR FORMAÇÃO
    @DeleteMapping("/{idFormacao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long idCandidato,
            @PathVariable Long idFormacao
    ) {
        formacaoService.deletar(idCandidato, idFormacao);
    }
}

