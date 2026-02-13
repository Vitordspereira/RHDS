package com.hub.hds.controller.vaga;


import com.hub.hds.dto.vaga.match.MatchVagaDTO;
import com.hub.hds.dto.vaga.match.MatchVagaRecomendadoDTO;
import com.hub.hds.service.vaga.MatchVagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vagas")
public class  MatchVagaController {

    private final MatchVagaService matchVagaService;

    @GetMapping("/{idVaga}/match/{idCandidato}")
    public ResponseEntity<MatchVagaDTO> calcularMatch(
            @PathVariable Long idVaga,
            @PathVariable Long idCandidato
    ){

        MatchVagaDTO resultado  = matchVagaService.calcular(idVaga, idCandidato);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/candidatos/{idCandidato}/match")
    public ResponseEntity<List<MatchVagaRecomendadoDTO>> vagasRecomendadas(
            @PathVariable Long idCandidato
    ) {
        return ResponseEntity.ok(
                matchVagaService.vagasRecomendadas(idCandidato)
        );
    }
}
