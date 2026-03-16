package com.hub.hds.controller.vaga;

import com.hub.hds.dto.vaga.get.VagaListDTO;
import com.hub.hds.dto.vaga.post.VagaCreateDTO;
import com.hub.hds.dto.vaga.post.VagaCreateResponseDTO;
import com.hub.hds.dto.vaga.put.VagaUpdateDTO;
import com.hub.hds.models.vaga.CategoriaVaga;
import com.hub.hds.service.vaga.VagaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService vagaService;

    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping
    public ResponseEntity<VagaCreateResponseDTO> criar(
            @Valid @RequestBody VagaCreateDTO vagaCreateDTO,
            @AuthenticationPrincipal String emailUsuario
    ) {
        Long idVaga = vagaService.criar(vagaCreateDTO, emailUsuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new VagaCreateResponseDTO(idVaga, "Vaga criada com sucesso"));
    }

    @GetMapping("/me")
    public ResponseEntity<List<VagaListDTO>> buscarMinhas(@AuthenticationPrincipal String emailUsuario) {
        return ResponseEntity.ok(vagaService.listarDoRecrutador(emailUsuario));
    }

    @GetMapping("/me/{idVaga}")
    public ResponseEntity<VagaListDTO> buscarPorIdDoRecrutador(
            @PathVariable Long idVaga,
            @AuthenticationPrincipal String emailUsuario
    ) {
        return ResponseEntity.ok(vagaService.buscarPorIdDoRecrutador(idVaga, emailUsuario));
    }

    @GetMapping("/{idVaga}")
    public ResponseEntity<VagaListDTO> buscarPublicaPorId(@PathVariable Long idVaga) {
        return ResponseEntity.ok(vagaService.buscarPorId(idVaga));
    }

    @PutMapping("/{idVaga}")
    public ResponseEntity<VagaListDTO> atualizarVaga(
            @PathVariable Long idVaga,
            @RequestBody VagaUpdateDTO vagaUpdateDTO,
            @AuthenticationPrincipal String emailUsuario
    ) {
        return ResponseEntity.ok(vagaService.atualizar(idVaga, vagaUpdateDTO, emailUsuario));
    }

    @GetMapping("/list")
    public List<VagaListDTO> listar(@RequestParam(required = false) CategoriaVaga categoriaVaga) {
        if (categoriaVaga != null) {
            return vagaService.listarPorCategoria(categoriaVaga);
        }
        return vagaService.listar();
    }

    @DeleteMapping("/{idVaga}")
    public ResponseEntity<Void> deletarVaga(
            @PathVariable Long idVaga,
            @AuthenticationPrincipal String emailUsuario
    ) {
        vagaService.deletarVaga(idVaga, emailUsuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idVaga}/encerrar")
    public ResponseEntity<Void> encerrarVaga(
            @PathVariable Long idVaga,
            @AuthenticationPrincipal String emailUsuario
    ) {
        vagaService.encerrarVaga(idVaga, emailUsuario);
        return ResponseEntity.noContent().build();
    }
}
