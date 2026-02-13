package com.hub.hds.dto.empresa;

import com.hub.hds.models.unidadeEmpresa.TipoUnidade;

public record UnidadeEmpresaDTO(
        TipoUnidade tipoUnidade,
        Integer numeroFuncionarios
) {
}
