package com.hub.hds.models.experiencia;

import com.hub.hds.models.candidato.Candidato;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experiencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experiencia")
    private Long idExperiencia;

    @Column(name = "nome_empresa",nullable = false, length = 150)
    private String nomeEmpresa;

    @Column(nullable = false, length = 150)
    private String funcao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "outras_experiencias",columnDefinition = "TEXT")
    private String outrasExperiencias;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(length = 7)
    private String periodoInicio;

    @Column(length = 7)
    private String periodoFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato", nullable = false)
    private Candidato candidato;
}



