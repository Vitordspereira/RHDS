package com.hub.hds.repository.candidato;

import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoEmpresaDTO;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    @Query("""
        select new com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoEmpresaDTO(
            c.idCandidato,
            c.nomeCompleto,
            c.genero,
            c.cidade,
            c.estado,
            coalesce(ec.favoritado, false)
        )
        from Candidato c
        left join EmpresaCandidato ec
          on ec.candidato = c
         and ec.empresa.idEmpresa = :empresaId
    """)
    List<CandidatoEmpresaDTO> listarParaEmpresa(@Param("empresaId") Long empresaId);


    Optional<Candidato> findByUsuario_Email(String email);

    Optional<Candidato> findByUsuario(Usuario usuario);

    @Query("""
        select new com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoEmpresaDTO(
            c.idCandidato,
            c.nomeCompleto,
            c.genero,
            c.cidade,
            c.estado,
            coalesce(ec.favoritado, false)
        )
        from Candidato c
        left join EmpresaCandidato ec
          on ec.candidato = c
         and ec.empresa.idEmpresa = :empresaId
        where c.idCandidato = :candidatoId
    """)
    Optional<CandidatoEmpresaDTO> buscarCandidatoPorEmpresaECandidato(
            @Param("empresaId") Long empresaId,
            @Param("candidatoId") Long candidatoId
    );
}
