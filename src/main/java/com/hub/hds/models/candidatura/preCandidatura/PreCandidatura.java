package com.hub.hds.models.candidatura.preCandidatura;

import com.hub.hds.models.vaga.Vaga;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "pre_candidaturas",
        indexes =  {
                @Index(name = "idx_pre_candidatura_email", columnList = "email"),
                @Index(name = "idx_pre_candidatura_status", columnList = "status"),
                @Index(name = "idx_pre_candidatua_expires", columnList = "expires_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pre_candidatura_token", columnNames = "token_confirmacao")
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreCandidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pre_candidatura")
    private Long idPreCandidatura;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(name = "token_confirmacao", nullable = false, length = 64, unique = true)
    private String tokenConfirmacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPreCandidatura statusPreCandidatura;

    @Column(name = "email_lembrete_enviado", nullable = false)
    private Boolean emailLembreteEnviado = false;

    @Column(nullable = false)
    private boolean tokenEnviado = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.expiresAt == null) {
            this.expiresAt = this.createdAt.plusHours(24);
        }

        if (this.statusPreCandidatura == null) {
            this.statusPreCandidatura = StatusPreCandidatura.INICIADA;
        }

        if (this.emailLembreteEnviado == null) {
            this.emailLembreteEnviado =false;
        }
    }
}
