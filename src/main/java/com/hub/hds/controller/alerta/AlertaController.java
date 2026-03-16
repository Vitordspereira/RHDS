package com.hub.hds.controller.alerta;

import com.hub.hds.dto.alerta.AlertaDTO;
import com.hub.hds.service.alerta.AlertaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/alerta")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criarAlerta(@RequestBody AlertaDTO alertaDTO) {
        alertaService.criarAlerta(alertaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Alerta criado com sucesso"));
    }

    @GetMapping("/cancelar")
    public ResponseEntity<Void> cancelar(@RequestParam("token") String token) {
        alertaService.cancelarAlerta(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }
}
