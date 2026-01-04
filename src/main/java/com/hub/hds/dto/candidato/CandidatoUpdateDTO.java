package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CandidatoUpdateDTO(

        @NotBlank(message = "Nome obrigatório")
        String nomeCompleto,

        @Pattern(
                regexp = "\\d{10,11}",
                message = "Telefone inválido"
        )
        String telefone,

        Genero genero,

        String cidade,

        @Pattern(
                regexp = "[A-Z]{2}",
                message = "Estado inválido"
        )
        String estado
) {}
