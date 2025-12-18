package com.hub.hds.dto.senha;

public record ResetSenhaResponse(
        String token,
        String novaSenha) {
}
