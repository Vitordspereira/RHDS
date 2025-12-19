package com.hub.hds.models.senha;

import com.hub.hds.models.candidato.Candidato;
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
    @JoinColumn(name = "id_candidato", nullable = false)
    private Candidato candidato;


    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "expira_em",nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean utilizado = false;
}
