package com.hub.hds.models.alerta;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "cidade", length = 50)
    private String cidade;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "token_cancelamento", nullable = false, unique = true, length = 64)
    private String tokenCancelamento;

    @Column(name = "criado_em", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp criadoEm;
}
