package com.hub.hds.dto.dashboardEmpresa;


import java.util.List;

public record EmpresaDashboardDTO(
        Long idEmpresa,
        String nomeEmpresa,
        String cnpj,
        String ramo,
        Boolean possuiFiliais,
        List<UnidadeEmpresaDTO> unidadeEmpresa,
        List<RecrutadoresDTO> recrutadores
) {
}
