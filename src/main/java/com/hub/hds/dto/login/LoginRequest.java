package com.hub.hds.dto.login;

public record LoginRequest(
        String email,
        String senha
) {
}
