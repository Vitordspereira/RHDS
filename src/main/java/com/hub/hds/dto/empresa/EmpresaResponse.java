package com.hub.hds.dto.empresa;

import com.hub.hds.models.empresa.NumeroFuncionarios;

import java.time.LocalDateTime;

public record EmpresaResponse(
        Long id_empresa,
        Boolean matrizFilial,
        NumeroFuncionarios numeroFuncionarios,
        NumeroFuncionarios filial_numero_funcionarios,
        String cnpj,
        String ramo,
        String nome_responsavel,
        String email,
        String celular,
        LocalDateTime criadoEm
) {
}
