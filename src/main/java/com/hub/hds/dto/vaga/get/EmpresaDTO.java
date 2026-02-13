package com.hub.hds.dto.vaga.get;

public record EmpresaDTO(

        String empresaNome,
        String empresaDescricao,
        String empresaSegmento,
        String empresaTamanho,
        String empresaSite,
        boolean empresaConfidencial
) {}
