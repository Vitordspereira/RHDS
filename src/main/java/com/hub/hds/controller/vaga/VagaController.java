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

    /* =====================================================
       CRIAR VAGA
       ===================================================== */
    @PostMapping
    public ResponseEntity<VagaCreateResponseDTO> criar(
            @Valid @RequestBody VagaCreateDTO vagaCreateDTO,
            @AuthenticationPrincipal String emailUsuario
    ) {

        Long idVaga = vagaService.criar(vagaCreateDTO, emailUsuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new VagaCreateResponseDTO(
                        idVaga,
                        "Vaga criada com sucesso"
                ));
    }

    /* =====================================================
       LISTAR VAGAS (PÚBLICO)
       ===================================================== */

    @GetMapping("/me")
    public ResponseEntity<List<VagaListDTO>> buscar() {
        List<VagaListDTO> vagas = vagaService.listar();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/me/{idVaga}")
    public ResponseEntity<VagaListDTO> buscarPorId(
            @PathVariable Long idVaga
    ) {
        VagaListDTO vaga = vagaService.buscarPorId(idVaga);
        return ResponseEntity.ok(vaga);
    }

    @PutMapping("/{idVaga}")
    public ResponseEntity<VagaListDTO> atualizarVaga(
            @PathVariable Long idVaga,
            @RequestBody VagaUpdateDTO vagaUpdateDTO
    ) {
        try {
            VagaListDTO vagaAtualizada = vagaService.atualizar(idVaga, vagaUpdateDTO);
            return ResponseEntity.ok(vagaAtualizada);

        } catch (RuntimeException e) {
            // Caso ocorra algum erro (como vaga não encontrada)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/list")
    public List<VagaListDTO> listar(
            @RequestParam(required = false) CategoriaVaga categoriaVaga
    ) {
        if (categoriaVaga != null) {
            return vagaService.listarPorCategoria(categoriaVaga);
        }
        return vagaService.listar();
    }

    //Deletar e Encerrar vaga
    @DeleteMapping("/{idVaga}")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long idVaga) {
        vagaService.deletarVaga(idVaga); // só deleta se NÃO tiver candidaturas
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idVaga}/encerrar")
    public ResponseEntity<Void> encerrarVaga(@PathVariable Long idVaga) {
        vagaService.encerrarVaga(idVaga); // encerra e notifica candidatos
        return ResponseEntity.noContent().build();
    }

}



