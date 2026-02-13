package com.hub.hds.repository.recrutador;

import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecrutadorRepository extends JpaRepository<Recrutador, Long> {

        Optional<Recrutador> findByUsuario(Usuario usuario);

        Optional<Recrutador> findByUsuario_Email(String email);

}
