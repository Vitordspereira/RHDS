package com.hub.hds.controller.empresa;

import com.hub.hds.dto.empresa.EmpresaRequest;
import com.hub.hds.dto.empresa.EmpresaResponse;
import com.hub.hds.service.empresa.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaResponse> cadastrar(
            @RequestBody @Valid EmpresaRequest request){

        return ResponseEntity.ok(empresaService.cadastrar(request));
    }
}
