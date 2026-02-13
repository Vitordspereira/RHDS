package com.hub.hds.models.recrutador;

import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recrutador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recrutador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recrutador")
    private Long idRecrutador;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "usuario_id",nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "email_corporativo", nullable = false, length = 150)
    private String emailCorporativo;

    @Column(length = 20)
    private String telefone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
