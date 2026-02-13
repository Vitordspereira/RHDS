package com.hub.hds.dto.usuario;

public record UsuarioDashboardAuthDTO(
        Long idUsuario,
        Long idCandidato,
        String email,
        String role
) {
}
