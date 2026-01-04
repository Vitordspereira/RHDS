package com.hub.hds.controller.login;

import com.hub.hds.dto.login.LoginRequest;
import com.hub.hds.dto.login.LoginResponse;
import com.hub.hds.service.login.LoginService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.autenticar(request);
    }
}
