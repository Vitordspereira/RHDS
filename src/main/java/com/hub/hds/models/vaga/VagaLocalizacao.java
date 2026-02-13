package com.hub.hds.models.vaga;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vaga_localizacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VagaLocalizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localizacao")
    private Long idVagaLocalizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false, unique = true)
    private Vaga vaga;

    @Column(length = 150)
    private String rua;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 20)
    private String cep;
}
