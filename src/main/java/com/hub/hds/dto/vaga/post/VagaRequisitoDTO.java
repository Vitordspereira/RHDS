package com.hub.hds.dto.vaga.post;

public  record VagaRequisitoDTO(

        Boolean habilitacao,
        Boolean veiculoProprio,
        Boolean viajar,
        Boolean mudarResidencia,
        String observacao
) {
}
