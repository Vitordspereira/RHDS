package com.hub.hds.models.vaga;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vaga_requisitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VagaRequisitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisitos")
    private Long idRequisitos;

    @OneToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    private Boolean habilitacao;
    private Boolean veiculoProprio;
    private Boolean viajar;
    private Boolean mudarResidencia;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}


