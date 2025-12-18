package com.hub.hds.models.empresa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_empresa;

    @Column(nullable = false)
    private Boolean matrizFilial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NumeroFuncionarios numeroFuncionarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private NumeroFuncionarios filial_numero_funcionarios;

    @Column(length = 18, unique = true, nullable = true)
    private String cnpj;

    @Column(length = 100, nullable = false)
    private String ramo;

    @Column(length = 150, nullable = false)
    private String nome_responsavel;

    @Column(length = 150,nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String celular;

    @Column(length = 255, nullable = false)
    private String senha;

    @Column(updatable = false)
    private LocalDateTime criado_em;

    private LocalDateTime atualizado_em;

    @PrePersist
    protected void onCreate() {
        this.criado_em = LocalDateTime.now();
        this.atualizado_em = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizado_em = LocalDateTime.now();
    }
}
