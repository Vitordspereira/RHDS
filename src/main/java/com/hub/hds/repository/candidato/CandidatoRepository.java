package com.hub.hds.repository.candidato;

import com.hub.hds.models.candidato.Candidato;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    @EntityGraph(attributePaths = {"experiencias"})
    Optional<Candidato> findComExperienciasById(Long id);
    Optional<Candidato> findByEmail(String email);
}
