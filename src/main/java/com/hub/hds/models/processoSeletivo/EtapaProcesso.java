package com.hub.hds.models.processoSeletivo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "etapa_processo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtapaProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtapaProcesso;

    private String nome;

    @Column(name = "ordem_do_processo")
    private Integer ordemProcesso;

    @ManyToOne
    @JoinColumn(name = "processo_seletivo_id", nullable = false)
    private ProcessoSeletivo processoSeletivo;
}
