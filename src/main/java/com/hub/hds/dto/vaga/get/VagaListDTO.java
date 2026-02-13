package com.hub.hds.dto.vaga.get;

import com.hub.hds.dto.vaga.enums.CategoriaVagaDTO;
import com.hub.hds.dto.vaga.enums.ModalidadeVagaDTO;
import com.hub.hds.dto.vaga.enums.SalarioTipoDTO;
import com.hub.hds.dto.vaga.enums.TipoContratoDTO;
import com.hub.hds.dto.vaga.post.*;
import com.hub.hds.models.vaga.Vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VagaListDTO(

        Long idVaga,
        String cargo,
        String complemento,
        Integer totalInteressados,


        // ================= EMPRESA =================
        EmpresaDTO empresaDTO,

        // ================= DESCRIÇÃO =================
        ModalidadeVagaDTO modalidadeVagaDTO,
        TipoContratoDTO tipoContrato,
        CategoriaVagaDTO categoriaVagaDTO,
        SalarioTipoDTO salarioTipoDTO,
        BigDecimal salarioValor,
        String descricao,
        String jornada,
        List<String> responsabilidades,
        List<String> requisitosObrigatorios,
        List<String> requisitosDesejaveis,
        List<String> beneficios,
        String observacoes,

        LocalDate dataPublicacao,
        boolean contratacaoUrgente,

        // ================= DETALHES =================
        VagaLocalizacaoDTO localizacao,
        List<VagaFormacaoDTO> formacao,
        List<VagaRequisitoDTO> requisitos,
        List<VagaIdiomaDTO> idiomas,
        List<VagaCnhDTO> cnhs

) {
    public static VagaListDTO fromEntity(Vaga vaga) {
        return new VagaListDTO(
                vaga.getIdVaga(),
                vaga.getCargo(),
                vaga.getComplemento(),
                vaga.getTotalInteressados(),
                null, // EmpresaDTO (vamos ajustar depois)

                ModalidadeVagaDTO.valueOf(vaga.getModalidadeVaga().name()),
                TipoContratoDTO.valueOf(vaga.getTipoContrato().name()),
                CategoriaVagaDTO.valueOf(vaga.getCategoriaVaga().name()),
                SalarioTipoDTO.valueOf(vaga.getSalarioTipo().name()),
                vaga.getSalarioValor(),

                vaga.getDescricao(),
                vaga.getJornada(),

                split(vaga.getResponsabilidades()),
                split(vaga.getRequisitosObrigatorios()),
                split(vaga.getRequisitosDesejaveis()),
                split(vaga.getBeneficios()),

                vaga.getObservacoes(),
                vaga.getDataPublicacao(),
                vaga.getContratacaoUrgente(),

                null,        // localizacao
                List.of(),   // formacao
                List.of(),   // requisitos
                List.of(),   // idiomas
                List.of()    // cnhs
        );
    }

    private static List<String> split(String valor) {
        return valor != null ? List.of(valor.split(";")) : List.of();
    }
}


