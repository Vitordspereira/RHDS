package com.hub.hds.repository.empresa.empresaCandidato;

import com.hub.hds.models.empresa.empresaCandidato.EmpresaCandidato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaCandidatoRepository extends JpaRepository<EmpresaCandidato, Long> {

    Optional<EmpresaCandidato> findByEmpresaIdEmpresaAndCandidatoIdCandidato(
            Long empresaId,
            Long idCandidato
    );

}
