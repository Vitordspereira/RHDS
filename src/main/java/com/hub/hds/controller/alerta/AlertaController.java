package com.hub.hds.controller.alerta;

import com.hub.hds.dto.alerta.AlertaDTO;
import com.hub.hds.service.alerta.AlertaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerta")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @PostMapping
    public ResponseEntity<Void> criarAlerta(@RequestBody AlertaDTO alertaDTO) {
        alertaService.criarAlerta(alertaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
