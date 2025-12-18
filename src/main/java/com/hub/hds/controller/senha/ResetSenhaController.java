package com.hub.hds.controller.senha;

import com.hub.hds.dto.senha.ResetSenhaRequest;
import com.hub.hds.dto.senha.ResetSenhaResponse;
import com.hub.hds.service.senha.ResetSenhaService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/senha")
public class ResetSenhaController {

    private final ResetSenhaService resetSenhaService;

    public ResetSenhaController(ResetSenhaService resetSenhaService) {
        this.resetSenhaService = resetSenhaService;
    }

    @PostMapping("/reset")
    public String solicitar(@RequestBody ResetSenhaRequest request) {
        return resetSenhaService.solicitarReset(request);
    }

    @PostMapping("/reset/confirmar")
    public String confirmar(@RequestBody ResetSenhaResponse request) {
        return resetSenhaService.confirmarReset(request);
    }
}
