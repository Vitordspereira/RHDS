package com.hub.hds.controller.processoSeletivo;

import com.hub.hds.dto.processoSeletivo.EtapaProcessoDTO;
import com.hub.hds.service.candidatura.CandidaturaService;
import com.hub.hds.service.processoSeletivo.ProcessoSeletivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/empresa/vagas")
public class ProcessoSeletivoController {

    private final ProcessoSeletivoService processoSeletivoService;
    private final CandidaturaService candidaturaService;

    @GetMapping("/{idVaga}/etapas")
    public ResponseEntity<List<EtapaProcessoDTO>> listarEtapas(
            @PathVariable Long idVaga
    ) {
        return ResponseEntity.ok(
                processoSeletivoService.listarEtapas(idVaga)
        );
    }

    @PutMapping("/{idVaga}/fechar")
    public ResponseEntity<Void> fecharVaga(@PathVariable Long idVaga) {
        candidaturaService.fecharVaga(idVaga);
        return ResponseEntity.noContent().build();
    }


}

