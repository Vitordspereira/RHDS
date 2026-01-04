package com.hub.hds.models.empresa;

import com.hub.hds.models.recrutador.Recrutador;
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

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(nullable = false, length = 100)
    private String ramo;

    @Column(name = "possui_filiais", nullable = false)
    private Boolean possuiFiliais;

    @Column(name = "numero_funcionarios", nullable = false)
    private Integer numeroFuncionarios;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "empresa")
    private List<Recrutador> recrutadores;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

