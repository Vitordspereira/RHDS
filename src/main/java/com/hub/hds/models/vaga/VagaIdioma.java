package com.hub.hds.models.vaga;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vaga_idioma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VagaIdioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_idioma")
    private Long idIdioma;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(nullable = false, length = 100)
    private String idioma;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel")
    private NivelIdioma nivelIdioma;

    @Column(nullable = false)
    private Boolean obrigatorio = false;
}

