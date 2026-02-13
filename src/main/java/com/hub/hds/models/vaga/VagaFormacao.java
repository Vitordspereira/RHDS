package com.hub.hds.models.vaga;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vaga_formacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VagaFormacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_formacao")
        private Long idFormacao;

        @OneToOne
        @JoinColumn(name = "vaga_id", nullable = false)
        private Vaga vaga;

        @Column(length = 100)
        private String escolaridade;

        @Column(name = "experiencia_descricao", columnDefinition = "TEXT")
        private String experienciaDescricao;
}


