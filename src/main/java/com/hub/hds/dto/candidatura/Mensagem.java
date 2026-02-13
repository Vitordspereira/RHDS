package com.hub.hds.dto.candidatura;

public record Mensagem <T>(
        String mensagem,
        T dados
) {
}
