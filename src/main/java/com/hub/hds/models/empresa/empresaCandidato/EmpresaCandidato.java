package com.hub.hds.models.empresa.empresaCandidato;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.empresa.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "empresa_candidato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaCandidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa_candidato")
    private Long idEmpresaCandidato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_candidato", nullable = false)
    private Candidato candidato;

    @Column(nullable = false)
    @Builder.Default
    private Boolean favoritado = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
