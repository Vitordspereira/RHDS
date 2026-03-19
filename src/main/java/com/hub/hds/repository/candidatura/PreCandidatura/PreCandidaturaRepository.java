package com.hub.hds.repository.candidatura.PreCandidatura;

import com.hub.hds.models.candidatura.preCandidatura.PreCandidatura;
import com.hub.hds.models.candidatura.preCandidatura.StatusPreCandidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreCandidaturaRepository extends JpaRepository<PreCandidatura, Long> {

    // 🔎 Busca por token (confirmação na tela)
    Optional<PreCandidatura> findByTokenConfirmacao(String tokenConfirmacao);


    // 🔐 Evita duplicidade considerando status específico (NOVO)
    boolean existsByEmailAndVaga_IdVagaAndStatusPreCandidatura(
            String email,
            Long idVaga,
            StatusPreCandidatura statusPreCandidatura
    );
}
