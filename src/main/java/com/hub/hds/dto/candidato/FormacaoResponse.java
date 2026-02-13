package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Formacao;
import com.hub.hds.models.candidato.NivelFormacao;

import java.time.LocalDate;

public record FormacaoResponse(

        Long idFormacao,
        String instituicao,
        String curso,
        NivelFormacao nivelFormacao,
        LocalDate dataInicio,
        LocalDate dataFim

) {
    public FormacaoResponse(Formacao formacao) {
        this(
                formacao.getIdFormacao(),
                formacao.getInstituicao(),
                formacao.getCurso(),
                formacao.getNivelFormacao(),
                formacao.getDataInicio(),
                formacao.getDataFim()
        );
    }
}
