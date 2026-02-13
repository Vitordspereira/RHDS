package com.hub.hds.controller.senha;

import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.models.senha.ResetSenhaMessage;
import com.hub.hds.service.senha.ResetSenhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/reset-senha")
public class ResetSenhaController {

    private final ResetSenhaService resetSenhaService;

    public ResetSenhaController(ResetSenhaService resetSenhaService) {
        this.resetSenhaService = resetSenhaService;
    }

    //SOLICITAR RESET (SEM TOKEN JWT)
    @PostMapping
    public ResponseEntity<ResetSenhaMessage> solicitarReset(
            @RequestBody Map<String, String> body
    ) {
        String email = body.get("email");

        resetSenhaService.solicitarReset(email);

        return ResponseEntity.ok(
                new ResetSenhaMessage(
                        "Encaminhamos um link de alteração de senha no seu e-mail"
                )
        );
    }

    //CONFIRMAR NOVA SENHA (SEM TOKEN JWT)
    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarReset(
            @RequestBody ResetSenhaResponse dto
    ) {
        resetSenhaService.redefinirSenha(dto);
        return ResponseEntity.ok().build();
    }
}
