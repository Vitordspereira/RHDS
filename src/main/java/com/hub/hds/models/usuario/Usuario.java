package com.hub.hds.models.usuario;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idUsuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}

