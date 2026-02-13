package com.hub.hds.models.senha;

import com.hub.hds.models.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_senha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long idToken;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token", nullable = false, unique = true, length = 100)
    private String token;

    @Column(name = "criado_em", insertable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean utilizado;
}
