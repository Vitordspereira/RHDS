package com.hub.hds.models.vaga;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vaga_cnh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VagaCnh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cnh")
    private Long idCnh;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private CategoriaCnh categoriaCnh;
}
