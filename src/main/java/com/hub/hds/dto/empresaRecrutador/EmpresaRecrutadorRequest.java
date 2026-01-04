package com.hub.hds.dto.empresaRecrutador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpresaRecrutadorRequest(

        @NotBlank
        String nomeEmpresa,

        String cnpj,

        @NotBlank
        String ramo,

        @NotNull
        Boolean possuiFiliais,

        @NotNull
        Integer numeroFuncionarios,

        @NotBlank
        String nomeRecrutador,

        @Email
        @NotBlank
        String email,

        String telefone,

        @NotBlank
        String senha
) {
}
