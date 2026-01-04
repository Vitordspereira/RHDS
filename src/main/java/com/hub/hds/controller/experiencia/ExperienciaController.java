package com.hub.hds.controller.experiencia;

import com.hub.hds.dto.experiencia.ExperienciaRequest;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.service.experiencia.ExperienciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/candidatos/{idCandidato}/experiencias")
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    public ExperienciaController(ExperienciaService experienciaService) {
        this.experienciaService = experienciaService;
    }

    // CRIAR EXPERIÊNCIA
    @PostMapping
    public ResponseEntity<ExperienciaResponse> criar(
            @PathVariable Long idCandidato,
            @RequestBody @Valid ExperienciaRequest request
    ) {
        ExperienciaResponse response =
                experienciaService.criar(idCandidato, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // LISTAR EXPERIÊNCIAS DO CANDIDATO
    @GetMapping
    public ResponseEntity<List<ExperienciaResponse>> listarPorCandidato(
            @PathVariable Long idCandidato
    ) {
        List<ExperienciaResponse> lista =
                experienciaService.listarPorCandidato(idCandidato);

        return ResponseEntity.ok(lista);
    }

    // ATUALIZAR EXPERIÊNCIA
    @PutMapping("/{idExperiencia}")
    public ResponseEntity<ExperienciaResponse> atualizar(
            @PathVariable Long idCandidato,
            @PathVariable Long idExperiencia,
            @RequestBody @Valid ExperienciaRequest request
    ) {
        ExperienciaResponse response =
                experienciaService.atualizar(idCandidato, idExperiencia, request);

        return ResponseEntity.ok(response);
    }

    // DELETAR EXPERIÊNCIA
    @DeleteMapping("/{idExperiencia}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long idCandidato,
            @PathVariable Long idExperiencia
    ) {
        experienciaService.deletar(idCandidato, idExperiencia);
    }
}
