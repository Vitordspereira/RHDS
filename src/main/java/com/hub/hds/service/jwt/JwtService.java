package com.hub.hds.service.jwt;

import com.hub.hds.dto.usuario.UsuarioDashboardAuthDTO;
import com.hub.hds.models.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // =========================
    // KEY
    // =========================
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ==================================================
    // TOKEN BASE (EMPRESA / CASOS GENÉRICOS)
    // ==================================================
    public String gerarToken(Long idUsuario, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("idUsuario", idUsuario)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    // ==================================================
    // TOKEN COM idCandidato (LOGIN CANDIDATO)
    // ==================================================
    public String gerarToken(
            Long idUsuario,
            String email,
            Long idCandidato,
            String role
    ) {
        var builder = Jwts.builder()
                .setSubject(email)
                .claim("idUsuario", idUsuario)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration));

        if (idCandidato != null) {
            builder.claim("idCandidato", idCandidato);
        }

        return builder
                .signWith(getKey())
                .compact();
    }

    // ==================================================
    // TOKEN A PARTIR DO USUÁRIO (LOGIN EMPRESA)
    // ==================================================
    public String gerarToken(Usuario usuario) {
        return gerarToken(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }

    // =========================
    // VALIDAR TOKEN
    // =========================
    public Claims validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // HELPERS SIMPLES
    // =========================
    public String extrairEmail(String token) {
        return validarToken(token).getSubject();
    }

    public String extrairRole(String token) {
        return validarToken(token).get("role", String.class);
    }

    public Long extrairIdUsuario(String token) {
        return validarToken(token).get("idUsuario", Long.class);
    }

    public Long extrairIdCandidato(String token) {
        return validarToken(token).get("idCandidato", Long.class);
    }

    // =========================
    // 🔥 USADO PELO JwtAuthFilter
    // =========================
    public UsuarioDashboardAuthDTO extrairUsuario(String token) {

        Claims claims = validarToken(token);

        Long idUsuario = claims.get("idUsuario", Long.class);
        Long idCandidato = claims.get("idCandidato", Long.class); // pode ser null
        String role = claims.get("role", String.class);
        String email = claims.getSubject();

        return new UsuarioDashboardAuthDTO(
                idUsuario,
                idCandidato,
                email,
                role
        );
    }
}
