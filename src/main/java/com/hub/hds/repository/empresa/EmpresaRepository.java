package com.hub.hds.repository.empresa;

import com.hub.hds.models.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
