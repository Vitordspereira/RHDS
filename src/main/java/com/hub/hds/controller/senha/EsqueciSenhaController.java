package com.hub.hds.controller.senha;

import com.hub.hds.dto.senha.ResetSenhaRequest;
import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.service.senha.ResetSenhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/senha")
@CrossOrigin(origins = "*")
public class EsqueciSenhaController {

    private final ResetSenhaService resetSenhaService;

    public EsqueciSenhaController(ResetSenhaService resetSenhaService) {
        this.resetSenhaService = resetSenhaService;
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> esqueciSenha(
            @RequestBody ResetSenhaRequest dto
    ) {
        resetSenhaService.solicitarReset(dto.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetar")
    public ResponseEntity<Void> resetar(@RequestBody ResetSenhaResponse dto) {
        resetSenhaService.redefinirSenha(dto);
        return ResponseEntity.ok().build();
    }
}

