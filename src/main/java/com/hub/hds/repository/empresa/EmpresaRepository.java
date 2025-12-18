package com.hub.hds.repository.empresa;

import com.hub.hds.models.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByEmail(String email);

    Optional<Empresa> findByCnpj(String cnpj);

    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}
