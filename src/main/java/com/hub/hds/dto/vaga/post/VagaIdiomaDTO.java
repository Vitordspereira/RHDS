package com.hub.hds.dto.vaga.post;

import com.hub.hds.models.vaga.NivelIdioma;

public record VagaIdiomaDTO(
        String idioma,
        NivelIdioma nivelIdioma,
        Boolean obrigatorio
) {
}
