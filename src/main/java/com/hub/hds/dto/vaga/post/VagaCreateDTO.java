package com.hub.hds.dto.vaga.post;


import com.hub.hds.dto.vaga.enums.CategoriaVagaDTO;
import com.hub.hds.dto.vaga.enums.ModalidadeVagaDTO;
import com.hub.hds.dto.vaga.enums.SalarioTipoDTO;
import com.hub.hds.dto.vaga.enums.TipoContratoDTO;


import java.math.BigDecimal;
import java.util.List;

public record VagaCreateDTO(

        // 🏷 TÍTULO
        String cargo,
        String complemento,

        // 🏢 EMPRESA (SNAPSHOT)
        String empresaNome,
        String empresaDescricao,
        String empresaSegmento,
        String empresaTamanho,
        String empresaSite,
        Boolean empresaConfidencial,

        // 📌 CLASSIFICAÇÃO
        ModalidadeVagaDTO modalidadeVaga,
        TipoContratoDTO tipoContrato,
        CategoriaVagaDTO categoriaVaga,

        // 💰 SALÁRIO
        SalarioTipoDTO salarioTipo,
        BigDecimal salarioValor,

        // 📝 DESCRIÇÃO
        String descricao,
        String jornada,

        // 📦 LISTAS (ENTRAM COMO LIST, VIRAM JSON)
        List<String> responsabilidades,
        List<String> requisitosObrigatorios,
        List<String> requisitosDesejaveis,
        List<String> beneficios,

        // ⚙ OUTROS
        String observacoes,
        Boolean contratacaoUrgente,

        // 📍 RELAÇÕES
        VagaFormacaoDTO formacao,
        VagaRequisitoDTO requisitos,
        List<VagaIdiomaDTO> idiomas,
        VagaLocalizacaoDTO localizacao,
        List<VagaCnhDTO> cnhs
) {}



