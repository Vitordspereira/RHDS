package com.hub.hds.repository.candidatura.PreCandidatura;

import com.hub.hds.models.candidatura.preCandidatura.PreCandidatura;
import com.hub.hds.models.candidatura.preCandidatura.StatusPreCandidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreCandidaturaRepository extends JpaRepository<PreCandidatura, Long> {

    Optional<PreCandidatura> findByTokenConfirmacao(String tokenConfirmacao);

    boolean existsByEmailAndVaga_IdVagaAndStatusPreCandidatura(
            String email,
            Long idVaga,
            StatusPreCandidatura statusPreCandidatura
    );

    Optional<PreCandidatura> findByEmailAndVaga_IdVagaAndStatusPreCandidatura(
            String email,
            Long idVaga,
            StatusPreCandidatura statusPreCandidatura
    );
}