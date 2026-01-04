package com.hub.hds.models.formacao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hub.hds.models.candidato.Candidato;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormacao;

    @Column(nullable = false, length = 150)
    private String nomeCurso;

    @Column(nullable = false, length = 150)
    private String instituicao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(length = 7)
    private String periodoInicio;

    @Column(length = 7)
    private String periodoFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato")
    private Candidato candidato;
}
