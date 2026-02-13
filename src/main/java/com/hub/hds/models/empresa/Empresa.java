package com.hub.hds.models.empresa;

import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import com.hub.hds.models.vaga.Vaga;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name = "nome_empresa",nullable = false, length = 150)
    private String nomeEmpresa;

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(nullable = false, length = 100)
    private String ramo;

    @Column(name = "possui_filiais", nullable = false)
    private Boolean possuiFiliais;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    private List<UnidadeEmpresa> unidadeEmpresas;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    private List<Recrutador> recrutadores;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

