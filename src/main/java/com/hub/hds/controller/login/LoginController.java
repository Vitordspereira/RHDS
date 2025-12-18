package com.hub.hds.controller.login;

import com.hub.hds.dto.login.LoginRequest;
import com.hub.hds.dto.login.LoginResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.login.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/login")
public class LoginController {

    private final LoginService loginService;
    private final CandidatoRepository candidatoRepository;

    public LoginController(LoginService loginService, CandidatoRepository candidatoRepository) {
        this.loginService = loginService;
        this.candidatoRepository = candidatoRepository;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.autenticar(request);
    }

    @GetMapping("/me")
    public ResponseEntity<Candidato> getCandidatoLogado(Authentication authentication){

        String email= authentication.getName();

        Candidato candidato = candidatoRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Candidato não encontrado"));

        return ResponseEntity.ok(candidato);
    }
}
