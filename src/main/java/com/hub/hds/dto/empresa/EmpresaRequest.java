package com.hub.hds.dto.empresa;

import com.hub.hds.models.empresa.NumeroFuncionarios;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpresaRequest(

        @NotNull
        Boolean matrizFilial,

        @NotNull
        NumeroFuncionarios numeroFuncionarios,

        NumeroFuncionarios filial_numero_funcionarios,

        String cnpj,

        @NotBlank
        String ramo,

        @NotBlank
        String nome_responsavel,

        @NotBlank
        String email,

        @NotBlank
        String celular,

        @NotBlank
        String senha
) {
}
