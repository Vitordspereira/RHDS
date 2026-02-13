package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.candidato.Genero;

import java.time.LocalDateTime;
import java.util.List;

public record CandidatoCadastroResponse(

        // 🔑 identificadores
        Long idCandidato,
        LocalDateTime createdAt,

        // 👤 dados pessoais
        String nomeCompleto,
        String cpf,
        String telefone,
        Genero genero,
        String dataNascimento,
        String cidade,
        String estado,

        // 📄 currículo
        List<ExperienciaResponse> experiencias,
        List<FormacaoResponse> formacoes
) {
    public CandidatoCadastroResponse(Candidato candidato) {
        this(
                candidato.getIdCandidato(),
                candidato.getCreatedAt(),
                candidato.getNomeCompleto(),
                candidato.getCpf(),
                candidato.getTelefone(),
                candidato.getGenero(),
                candidato.getDataNascimento(),
                candidato.getCidade(),
                candidato.getEstado(),
                candidato.getExperiencias()
                        .stream()
                        .map(ExperienciaResponse::new)
                        .toList(),
                candidato.getFormacoes()
                        .stream()
                        .map(FormacaoResponse::new)
                        .toList()
        );
    }
}

