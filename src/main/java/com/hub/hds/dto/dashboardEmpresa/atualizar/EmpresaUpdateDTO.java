package com.hub.hds.dto.dashboardEmpresa.atualizar;

public record EmpresaUpdateDTO(
        String nomeEmpresa,
        String ramo,
        Boolean possuiFiliais
) {
}
