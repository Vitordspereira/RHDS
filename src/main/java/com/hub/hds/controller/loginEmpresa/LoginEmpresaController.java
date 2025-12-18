package com.hub.hds.controller.loginEmpresa;

import com.hub.hds.dto.loginEmpresa.LoginEmpresaRequest;
import com.hub.hds.dto.loginEmpresa.LoginEmpresaResponse;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.service.loginEmpresa.LoginServiceEmpresa;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/login/empresa")
public class LoginEmpresaController {

    private final LoginServiceEmpresa loginServiceEmpresa;
    private final EmpresaRepository empresaRepository;

    public LoginEmpresaController(LoginServiceEmpresa loginServiceEmpresa, EmpresaRepository empresaRepository) {this.loginServiceEmpresa = loginServiceEmpresa; this.empresaRepository = empresaRepository;}

    @PostMapping
    public LoginEmpresaResponse login(@RequestBody LoginEmpresaRequest request) {return loginServiceEmpresa.autenticar(request);}

    @GetMapping("/me")
    public ResponseEntity<Empresa> getEmpresaLogada(Authentication authentication){

        String email = authentication.getName();

        Empresa empresa = empresaRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Empresa não encontrada"));

        return ResponseEntity.ok(empresa);
    }
}
