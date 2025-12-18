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
    private Long id_token;

    @ManyToOne
    @JoinColumn(name = "id_candidato", nullable = false)
    private Candidato candidato;


    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expira_em;

    @Column(nullable = false)
    private boolean utilizado;
}
