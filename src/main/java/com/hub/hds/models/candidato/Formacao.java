package com.hub.hds.models.candidato;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "formacoes")
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formacao")
    private Long idFormacao;

    @Column(name = "instituicao", nullable = false)
    private String instituicao;

    @Column(name = "curso", nullable = false)
    private String curso;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private NivelFormacao nivelFormacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusFormacao statusFormacao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;
}
