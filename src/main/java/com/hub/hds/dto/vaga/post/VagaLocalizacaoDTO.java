package com.hub.hds.dto.vaga.post;

public record VagaLocalizacaoDTO(

        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
) {
}
