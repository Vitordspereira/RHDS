package com.hub.hds.repository.usuario;

import com.hub.hds.models.candidato.Formacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormacaoRepository extends JpaRepository<Formacao, Long> {

}

