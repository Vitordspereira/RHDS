package com.hub.hds.dto.empresa;

import java.util.List;

public record EmpresaCadastroDTO(
        String nomeEmpresa,
        String cnpj,
        String ramo,
        Boolean possuiFiliais,
        List<UnidadeEmpresaDTO> unidade,
        RecrutadorDTO recrutador
) {
}
