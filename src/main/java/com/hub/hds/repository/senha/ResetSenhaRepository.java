package com.hub.hds.repository.senha;

import com.hub.hds.models.senha.ResetSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetSenhaRepository extends JpaRepository<ResetSenha, Long> {

    Optional<ResetSenha> findByToken(String token);
}
