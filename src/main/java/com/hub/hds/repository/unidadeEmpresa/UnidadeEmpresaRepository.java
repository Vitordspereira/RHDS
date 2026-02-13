package com.hub.hds.repository.unidadeEmpresa;

import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import com.hub.hds.models.unidadeEmpresa.TipoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnidadeEmpresaRepository extends JpaRepository<UnidadeEmpresa, Long> {

    Optional<UnidadeEmpresa> findByEmpresa_IdEmpresaAndTipoUnidade(
            Long idEmpresa,
            TipoUnidade tipoUnidade
    );

    boolean existsByEmpresa_IdEmpresaAndTipoUnidade(Long idEmpresa, TipoUnidade tipoUnidade);
}
