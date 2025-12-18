package com.hub.hds.models.formacao;

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
    private Long id_formacao;

    @Column(nullable = false, length = 150)
    private String nome_curso;

    @Column(nullable = false, length = 150)
    private String instituicao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(length = 7)
    private String periodo_inicio;

    @Column(length = 7)
    private String periodo_fim;
}
