package com.hub.hds.service.loginEmpresa;

import com.hub.hds.dto.loginEmpresa.LoginEmpresaRequest;
import com.hub.hds.dto.loginEmpresa.LoginEmpresaResponse;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.service.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceEmpresa {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginServiceEmpresa(EmpresaRepository empresaRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    public LoginEmpresaResponse autenticar(LoginEmpresaRequest request) {

        Empresa empresa = empresaRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválido"));

        if (!passwordEncoder.matches(request.senha(), empresa.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválido");
        }

        String token = jwtService.gerarToken(
                empresa.getId_empresa(),
                empresa.getEmail(),
                "EMPRESA",
                "ROLE_EMPRESA"
        );

        return new LoginEmpresaResponse(token);
    }
}
