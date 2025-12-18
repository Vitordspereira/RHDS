package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CandidatoRequest(

        Long id_candidato,

        @NotBlank(message = "Informe seu nome completo")
        @Size(max = 150)
        String nome_completo,

        @NotNull
        @Size(max = 150)
        String email,

        @NotNull
        @Size(max = 10)
        String senha,

        @Size(max = 20)
        String telefone,

        @NotBlank(message = "CPF obrigatório")
        @Size(min = 2, max = 14, message = "CPF inválido")
        String cpf,

        Genero genero,

        @NotNull(message = "A data de nascimento é obrigatória")
        java.time.LocalDate data_nascimento,

        @Size(max = 100)
        String cidade,

        @Size(max = 2)
        String estado
) {
}
