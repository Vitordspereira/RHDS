package com.hub.hds.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ ORIGIN EXATO do XAMPP (o seu)
        config.setAllowedOrigins(List.of(
                "http://localhost",
                "http://127.0.0.1"
        ));

        // ✅ métodos (inclui OPTIONS)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ headers (inclui Authorization)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // (opcional) se você quiser ler algum header na resposta
        config.setExposedHeaders(List.of("Authorization"));

        // ✅ se usar cookies/sessão: true. Se NÃO usa cookies, pode deixar true mesmo.
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
