package com.hub.hds.repository.processoSeletivo;

import com.hub.hds.models.processoSeletivo.ProcessoSeletivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessoSeletivoRepository extends JpaRepository<ProcessoSeletivo, Long> {

    Optional<ProcessoSeletivo> findByVaga_IdVaga(Long idVaga);

}
