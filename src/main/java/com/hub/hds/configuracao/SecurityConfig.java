package com.hub.hds.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (necessário para APIs)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Libera tudo temporariamente para testar
                )
                .httpBasic(Customizer.withDefaults()); // Usa autenticação de dados, não de página

        return http.build();
    }
}
