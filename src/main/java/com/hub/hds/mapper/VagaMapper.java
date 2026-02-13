package com.hub.hds.mapper;

import com.hub.hds.dto.vaga.enums.*;
import com.hub.hds.dto.vaga.get.*;
import com.hub.hds.dto.vaga.post.*;
import com.hub.hds.models.vaga.Vaga;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;



import java.util.Collections;
import java.util.List;

@Component
public class VagaMapper {

    private final ObjectMapper objectMapper;

    public VagaMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /* =====================================================
       ENTITY → DTO (GET)
       ===================================================== */
    public VagaListDTO toDTO(Vaga vaga) {

        return new VagaListDTO(
                vaga.getIdVaga(),
                vaga.getCargo(),
                vaga.getComplemento(),
                vaga.getTotalInteressados(),

                new EmpresaDTO(
                        vaga.getEmpresaNome(),
                        vaga.getEmpresaDescricao(),
                        vaga.getEmpresaSegmento(),
                        vaga.getEmpresaTamanho(),
                        vaga.getEmpresaSite(),
                        vaga.getEmpresaConfidencial()
                ),

                ModalidadeVagaDTO.valueOf(vaga.getModalidadeVaga().name()),
                TipoContratoDTO.valueOf(vaga.getTipoContrato().name()),
                CategoriaVagaDTO.valueOf(vaga.getCategoriaVaga().name()),
                SalarioTipoDTO.valueOf(vaga.getSalarioTipo().name()),
                vaga.getSalarioValor(),
                vaga.getDescricao(),
                vaga.getJornada(),

                fromJson(vaga.getResponsabilidades()),
                fromJson(vaga.getRequisitosObrigatorios()),
                fromJson(vaga.getRequisitosDesejaveis()),
                fromJson(vaga.getBeneficios()),

                vaga.getObservacoes(),
                vaga.getDataPublicacao(),
                Boolean.TRUE.equals(vaga.getContratacaoUrgente()),

                vaga.getLocalizacao() == null ? null :
                        new VagaLocalizacaoDTO(
                                vaga.getLocalizacao().getRua(),
                                vaga.getLocalizacao().getNumero(),
                                vaga.getLocalizacao().getComplemento(),
                                vaga.getLocalizacao().getBairro(),
                                vaga.getLocalizacao().getCidade(),
                                vaga.getLocalizacao().getEstado(),
                                vaga.getLocalizacao().getCep()
                        ),

                vaga.getVagaFormacao() == null ? List.of() :
                        List.of(new VagaFormacaoDTO(
                                vaga.getVagaFormacao().getEscolaridade(),
                                vaga.getVagaFormacao().getExperienciaDescricao()
                        )),

                vaga.getRequisitos() == null ? List.of() :
                        List.of(new VagaRequisitoDTO(
                                vaga.getRequisitos().getHabilitacao(),
                                vaga.getRequisitos().getVeiculoProprio(),
                                vaga.getRequisitos().getViajar(),
                                vaga.getRequisitos().getMudarResidencia(),
                                vaga.getRequisitos().getObservacoes()
                        )),

                vaga.getIdiomas() == null ? List.of() :
                        vaga.getIdiomas().stream()
                                .map(i -> new VagaIdiomaDTO(
                                        i.getIdioma(),
                                        i.getNivelIdioma(),
                                        i.getObrigatorio()
                                ))
                                .toList(),

                vaga.getCnhs() == null ? List.of() :
                        vaga.getCnhs().stream()
                                .map(c -> new VagaCnhDTO(c.getCategoriaCnh()))
                                .toList()
        );
    }

    /* =====================================================
       JSON HELPERS
       ===================================================== */
    private List<String> fromJson(String json) {
        try {
            if (json == null || json.isBlank()) return Collections.emptyList();
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    /* =====================================================
   DTO → JSON (POST)
   ===================================================== */
    public String toJson(List<String> list) {
        try {
            if (list == null || list.isEmpty()) return "[]";
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }

}
