package com.hub.hds.models.processoSeletivo;

import com.hub.hds.models.vaga.Vaga;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processo_seletivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoSeletivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProcessoSeletivo;

    @OneToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @OneToMany(
            mappedBy = "processoSeletivo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("orde ASC")
    private List<EtapaProcesso> etapaProcessos = new ArrayList<>();
}
