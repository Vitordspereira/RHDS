package com.hub.hds.repository.candidato;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.usuario.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    @EntityGraph(attributePaths = {"experiencias", "formacoes"})
    Optional<Candidato> findById(Long id);
    Optional<Candidato> findByUsuario(Usuario usuario);
}
