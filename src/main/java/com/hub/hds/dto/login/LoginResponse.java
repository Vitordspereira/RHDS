package com.hub.hds.dto.login;

public record LoginResponse(
        Long idUsuario,
        String nome,
        String email,
        String token
) {
}
