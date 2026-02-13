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
@Table(name = "experiencias")
public class Experiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experiencia")
    private Long idExperiencia;

    @Column(name = "empresa", nullable = false)
    private String empresa;

    @Column(name = "cargo", nullable = false)
    private String cargo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "atual")
    private Boolean atual;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

}


