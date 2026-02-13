package com.hub.hds.repository.candidatura;

import com.hub.hds.models.candidatura.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    boolean existsByCandidato_IdCandidatoAndVaga_IdVaga(
            Long candidatoId,
            Long vagaId
    );
    boolean existsByVaga_IdVaga(Long vagaId);

    List<Candidatura> findByVaga_IdVaga(Long idVaga);
    List<Candidatura> findAllByVaga_IdVaga(Long idVaga);
    List<Candidatura> findByCandidato_IdCandidato(Long idCandidato);
    long countByVaga_IdVaga(Long idVaga);
}

