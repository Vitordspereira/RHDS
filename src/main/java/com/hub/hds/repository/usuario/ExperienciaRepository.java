package com.hub.hds.repository.usuario;
import com.hub.hds.models.candidato.Experiencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienciaRepository extends JpaRepository<Experiencia, Long> {
}
