package com.hub.hds.models.candidatura;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.processoSeletivo.EtapaProcesso;
import com.hub.hds.models.vaga.Vaga;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidaturas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"candidato_id", "vaga_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidatura")
    private Long idCandidatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @ManyToOne
    @JoinColumn(name = "etapa_processo_id")
    private EtapaProcesso etapaAtual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private StatusCandidatura statusCandidatura;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "data_decisao")
    private LocalDateTime dataDecisao;

    @Column(name = "avaliado_por")
    private Long avaliadoPor;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.statusCandidatura == null) {
            this.statusCandidatura = StatusCandidatura.ENVIADA;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updateAt = LocalDateTime.now();

        if (this.statusCandidatura == StatusCandidatura.APROVADO
                || this.statusCandidatura == StatusCandidatura.REPROVADO) {

            if (this.dataDecisao == null) {
                this.dataDecisao = LocalDateTime.now();
            }
        }
    }

}

