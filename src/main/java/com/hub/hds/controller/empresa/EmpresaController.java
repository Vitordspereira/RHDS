package com.hub.hds.controller.empresa;

import com.hub.hds.dto.empresa.EmpresaCadastroDTO;
import com.hub.hds.dto.mensagem.MensagemDTO;
import com.hub.hds.service.empresa.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // ✅ CADASTRO DA EMPRESA
    @PostMapping("/cadastro")
    public ResponseEntity<MensagemDTO> cadastrarEmpresa(
            @RequestBody @Valid EmpresaCadastroDTO dto
    ) {
        empresaService.cadastrarEmpresa(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MensagemDTO("Cadastro realizado com sucesso"));
    }


}
