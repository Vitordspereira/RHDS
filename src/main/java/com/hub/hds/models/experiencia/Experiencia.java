package com.hub.hds.models.experiencia;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long id_experiencia;

    @Column(nullable = false, length = 150)
    private String nome_empresa;

    @Column(nullable = false, length = 150)
    private String funcao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String outras_experiencias;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(length = 7)
    private String periodo_inicio;

    @Column(length = 7)
    private String periodo_fim;

    @ManyToOne
    @JoinColumn(name = "id_candidato")
    @JsonBackReference
     
}



