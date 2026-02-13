package com.hub.hds.dto.processoSeletivo;

import com.hub.hds.models.processoSeletivo.EtapaProcesso;

public record EtapaProcessoDTO(
        Long idEtapaProcesso,
        String nome,
        Integer ordem
) {
    public static EtapaProcessoDTO fromEntity(EtapaProcesso etapaProcesso) {
        return new EtapaProcessoDTO(
                etapaProcesso.getIdEtapaProcesso(),
                etapaProcesso.getNome(),
                etapaProcesso.getOrdemProcesso()
        );
    }
}
