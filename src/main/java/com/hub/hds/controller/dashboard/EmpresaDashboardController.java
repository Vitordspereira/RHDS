package com.hub.hds.controller.dashboard;

import com.hub.hds.dto.dashboardEmpresa.EmpresaDashboardDTO;
import com.hub.hds.dto.dashboardEmpresa.atualizar.EmpresaUpdateDTO;
import com.hub.hds.service.dashboard.EmpresaDashboardService;
import com.hub.hds.service.empresa.EmpresaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresa")
public class EmpresaDashboardController {

    private final EmpresaDashboardService empresaDashboardService;
    private final EmpresaService empresaService;

    public EmpresaDashboardController(
            EmpresaDashboardService empresaDashboardService,
            EmpresaService empresaService
    ) {
        this.empresaDashboardService = empresaDashboardService;
        this.empresaService = empresaService;
    }

    /**
     * Retorna os dados da empresa do usuário logado
     */
    @GetMapping("/me")
    public EmpresaDashboardDTO minhaEmpresa(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String emailUsuarioLogado = authentication.getName();

        Long idEmpresa = empresaService.buscarIdEmpresaPorEmail(emailUsuarioLogado);

        return empresaDashboardService.buscarEmpresaLogada(idEmpresa);
    }

    @PutMapping("/atualizar")
    public void atualizarEmpresa(
            @RequestBody EmpresaUpdateDTO empresaUpdateDTO,
            Authentication authentication
            ){
        empresaDashboardService.atualizarEmpresaDoUsuario(empresaUpdateDTO, authentication.getName());
    }
}
