package com.hub.hds.controller.usuario;

import com.hub.hds.dto.usuario.UsuarioRequest;
import com.hub.hds.dto.usuario.UsuarioResponse;
import com.hub.hds.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================
    // LOGIN CANDIDATO
    // =========================
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(
            @Valid @RequestBody UsuarioRequest usuarioRequest
    ) {
        return ResponseEntity.ok(
                usuarioService.loginCandidato(usuarioRequest)
        );
    }

    // =========================
    // LOGIN EMPRESA
    // =========================
    @PostMapping("/login/empresa")
    public ResponseEntity<UsuarioResponse> loginEmpresa(
            @Valid @RequestBody UsuarioRequest usuarioRequest
    ) {
        return ResponseEntity.ok(
                usuarioService.loginEmpresa(usuarioRequest)
        );
    }

    // =========================
    // LOGOUT (JWT: front-end descarta token)
    // =========================
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}


