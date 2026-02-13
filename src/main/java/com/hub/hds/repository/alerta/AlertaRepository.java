package com.hub.hds.repository.alerta;

import com.hub.hds.models.alerta.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    boolean existsByEmailAndCargoAndCidade(
            String email,
            String cargo,
            String cidade
    );

    // usado para enviar alertas (só quem está ativo)
    List<Alerta> findByCargoAndCidadeAndAtivoTrue(
            String cargo,
            String cidade
    );

    // usado para cancelar alertas via link
    Optional<Alerta> findByTokenCancelamento(String tokenCancelamento);
}
