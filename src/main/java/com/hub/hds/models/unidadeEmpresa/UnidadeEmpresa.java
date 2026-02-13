package com.hub.hds.models.unidadeEmpresa;

import com.hub.hds.models.empresa.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "unidade_empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidade_empresa")
    private Long idUnidadeEmpresa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoUnidade tipoUnidade;

    @Column(name = "numero_funcionarios", nullable = false)
    private Integer numeroFuncionarios;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
