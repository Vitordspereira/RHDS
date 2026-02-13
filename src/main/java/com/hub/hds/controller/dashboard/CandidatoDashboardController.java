package com.hub.hds.controller.dashboard;

import com.hub.hds.dto.dashboardCandidato.ListarVagas.VagaCandidaturaDTO;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.dashboard.CandidatoDashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/candidato")
public class CandidatoDashboardController {

    private final CandidatoDashboardService candidatoDashboardService;
    private final CandidatoRepository candidatoRepository;

    public CandidatoDashboardController(
            CandidatoDashboardService candidatoDashboardService,
            CandidatoRepository candidatoRepository
    ) {
        this.candidatoDashboardService = candidatoDashboardService;
        this.candidatoRepository = candidatoRepository;
    }

    @GetMapping("/minhas-candidaturas")
    public List<VagaCandidaturaDTO> listarVagasCandidatadas(
            @AuthenticationPrincipal String email
    ) {
        Long idCandidato = candidatoRepository
                .findByUsuario_Email(email)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"))
                .getIdCandidato();

        return candidatoDashboardService.listarVagasCandidatadas(idCandidato);
    }
}

