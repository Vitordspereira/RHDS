package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CandidatoCadastroDTO(

        @NotBlank(message = "Informe seu nome completo")
        @Pattern(
                regexp = "^([A-Za-zÀ-ÿ]{2,})(\\s+[A-Za-zÀ-ÿ]{2,})+",
                message = "Informe nome e sobrenome"
        )
        @Size(max = 150, message = "Nome pode ter no máximo 150 caracteres")
                String nomeCompleto,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido.")
        @Size(max = 150, message = "E-mail pode ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "Senha obrigatória")
        @Size(min = 8, max = 60, message = "Senha deve ter entre 8 e 60 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).+$",
                message = "A senha deve conter letra maiúscula, minúscula, número e símbolo"
        )
        String senha,

        @Pattern(
                regexp = "(\\(\\d{2}\\)\\s?)?9?\\d{4}-?\\d{4}",
                message = "Telefone inválido."
        )
        String telefone,

        @NotBlank(message = "CPF obrigatório")
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF inválido. Use apenas números"
        )
        String cpf,

        @NotNull(message = "Gênero é obrigatório")
        Genero genero,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "Data de nascimento inválida")
        LocalDate dataNascimento,

        @Size(max = 100, message = "Cidade pode ter no máximo 100 caracteres")
        String cidade,

        @Pattern(
                regexp = "[A-Z]{2}",
                message = "Estado inválido. Use a sigla."
        )
        String estado
) {}

