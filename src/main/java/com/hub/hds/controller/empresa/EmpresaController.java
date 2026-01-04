package com.hub.hds.controller.empresa;

import com.hub.hds.dto.empresaRecrutador.EmpresaRecrutadorRequest;
import com.hub.hds.service.empresaRecrutador.EmpresaRecrutadorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresa")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaRecrutadorService service;

    public EmpresaController(EmpresaRecrutadorService service) {
        this.service = service;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid EmpresaRecrutadorRequest request
    ) {
        service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
