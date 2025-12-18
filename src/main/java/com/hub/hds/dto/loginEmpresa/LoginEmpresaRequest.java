package com.hub.hds.dto.loginEmpresa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginEmpresaRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
