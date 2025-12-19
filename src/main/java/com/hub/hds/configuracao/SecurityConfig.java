package com.hub.hds.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // AUTH
                        .requestMatchers("/auth/login/empresa", "/auth/login").permitAll()

                        // CANDIDATOS
                        .requestMatchers(HttpMethod.POST, "/candidatos").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/candidatos/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/candidatos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/candidatos/**").permitAll()

                        //EXPERIÊNCIA
                        .requestMatchers(HttpMethod.POST, "/experiencias").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/experiencias/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/experiencias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/experiencias/**").permitAll()

                        //FORMAÇÃO
                        .requestMatchers(HttpMethod.POST, "/formacoes").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/formacoes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/formacoes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/formacoes/**").permitAll()

                        //RESET SENHA
                        .requestMatchers("/senha/esqueci-senha").permitAll()
                        .requestMatchers("/senha/resetar").permitAll()
                        .requestMatchers("/teste-email").permitAll()


                        // ROTAS PROTEGIDAS
                        .requestMatchers("/auth/empresa/**").hasAuthority("ROLE_EMPRESA")
                        .requestMatchers("/auth/login/**").hasAuthority("ROLE_CANDIDATO")

                        .anyRequest().authenticated()
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
