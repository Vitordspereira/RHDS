package com.hub.hds.controller.empresa.empresaCandidato;

import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoEmpresaDTO;
import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoPerfilDTO;
import com.hub.hds.service.candidato.CandidatoService;
import com.hub.hds.service.empresa.empresaCandidato.EmpresaCandidatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaCandidatoController {

    private final EmpresaCandidatoService empresaCandidatoService;
    private final CandidatoService candidatoService;

    //LISTAR TODOS OS CANDIDATOS
    @GetMapping("/{empresaId}/candidatos")
    public List<CandidatoEmpresaDTO> listarCandidatos(
            @PathVariable Long empresaId
    ) {
        return empresaCandidatoService.listarParaEmpresa(empresaId);
    }

    @GetMapping("/{empresaId}/candidatos/{candidatoId}")
    public ResponseEntity<CandidatoEmpresaDTO> BuscarPorId(@PathVariable Long empresaId, @PathVariable Long candidatoId) {
        return ResponseEntity.ok(empresaCandidatoService.BuscarPorId(empresaId, candidatoId));
    }


    /* ============================
       ❤️ FAVORITAR CANDIDATO
       ============================ */
    @PostMapping("/{empresaId}/candidatos/{candidatoId}/favoritar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favoritarCandidato(
            @PathVariable Long empresaId,
            @PathVariable Long candidatoId
    ) {
        empresaCandidatoService.favoritar(empresaId, candidatoId);
    }

    /* ============================
       💔 DESFAVORITAR CANDIDATO
       ============================ */
    @PostMapping("/{empresaId}/candidatos/{candidatoId}/desfavoritar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desfavoritarCandidato(
            @PathVariable Long empresaId,
            @PathVariable Long candidatoId
    ) {
        empresaCandidatoService.desfavoritar(empresaId, candidatoId);
    }

    @GetMapping("/{idCandidato}/perfil")
    public ResponseEntity<CandidatoPerfilDTO> buscarPerfilProfissional(
            @PathVariable Long idCandidato
    ) {
        return ResponseEntity.ok(
                candidatoService.buscarPerfilProfissional(idCandidato)
        );
    }
}
