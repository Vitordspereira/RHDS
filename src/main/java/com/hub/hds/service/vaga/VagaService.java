package com.hub.hds.service.vaga;

import com.hub.hds.AlertaPalavras.KeywordExtractor;
import com.hub.hds.dto.vaga.get.VagaListDTO;
import com.hub.hds.dto.vaga.post.*;
import com.hub.hds.dto.vaga.put.VagaUpdateDTO;
import com.hub.hds.mapper.VagaMapper;
import com.hub.hds.models.candidatura.Candidatura;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.unidadeEmpresa.TipoUnidade;
import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import com.hub.hds.models.vaga.*;
import com.hub.hds.repository.candidatura.CandidaturaRepository;
import com.hub.hds.repository.recrutador.RecrutadorRepository;
import com.hub.hds.repository.unidadeEmpresa.UnidadeEmpresaRepository;
import com.hub.hds.repository.vaga.VagaRepository;
import com.hub.hds.service.EmailService;
import com.hub.hds.service.alerta.AlertaService;
import com.hub.hds.service.processoSeletivo.ProcessoSeletivoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;
    private final UnidadeEmpresaRepository unidadeEmpresaRepository;
    private final RecrutadorRepository recrutadorRepository;
    private final VagaMapper vagaMapper;
    private final AlertaService alertaService;
    private final ProcessoSeletivoService processoSeletivoService;
    private final CandidaturaRepository candidaturaRepository;
    private final EmailService emailService;

    public VagaService(
            VagaRepository vagaRepository,
            UnidadeEmpresaRepository unidadeEmpresaRepository,
            RecrutadorRepository recrutadorRepository,
            ObjectMapper objectMapper,
            AlertaService alertaService,
            ProcessoSeletivoService processoSeletivoService,
            CandidaturaRepository candidaturaRepository,
            EmailService emailService
    ) {
        this.vagaRepository = vagaRepository;
        this.unidadeEmpresaRepository = unidadeEmpresaRepository;
        this.recrutadorRepository = recrutadorRepository;
        this.vagaMapper = new VagaMapper(objectMapper);
        this.alertaService = alertaService;
        this.processoSeletivoService = processoSeletivoService;
        this.candidaturaRepository = candidaturaRepository;
        this.emailService = emailService;
    }

    /* =====================================================
       CRIAR VAGA (POST)
    ===================================================== */
    @Transactional
    public Long criar(VagaCreateDTO vagaCreateDTO, String emailUsuario) {

        Recrutador recrutador = recrutadorRepository
                .findByUsuario_Email(emailUsuario)
                .orElseThrow(() ->
                        new RuntimeException("Usuário autenticado não é um recrutador")
                );

        Empresa empresa = recrutador.getEmpresa();

        UnidadeEmpresa unidadeMatriz = unidadeEmpresaRepository
                .findByEmpresa_IdEmpresaAndTipoUnidade(
                        empresa.getIdEmpresa(),
                        TipoUnidade.MATRIZ
                )
                .orElseThrow(() ->
                        new RuntimeException("Empresa não possui unidade matriz cadastrada")
                );

        Vaga vaga = new Vaga();

        // 🔗 RELACIONAMENTOS
        vaga.setEmpresa(empresa);
        vaga.setRecrutador(recrutador);
        vaga.setUnidadeEmpresa(unidadeMatriz);

        // 🏷 CAMPOS BÁSICOS
        vaga.setCargo(vagaCreateDTO.cargo());
        vaga.setComplemento(vagaCreateDTO.complemento());
        vaga.setDescricao(vagaCreateDTO.descricao());
        vaga.setJornada(vagaCreateDTO.jornada());

        // 🏢 SNAPSHOT EMPRESA
        vaga.setEmpresaNome(vagaCreateDTO.empresaNome());
        vaga.setEmpresaDescricao(vagaCreateDTO.empresaDescricao());
        vaga.setEmpresaSegmento(vagaCreateDTO.empresaSegmento());
        vaga.setEmpresaTamanho(vagaCreateDTO.empresaTamanho());
        vaga.setEmpresaSite(vagaCreateDTO.empresaSite());
        vaga.setEmpresaConfidencial(
                Boolean.TRUE.equals(vagaCreateDTO.empresaConfidencial())
        );

        // 📌 ENUMS
        vaga.setModalidadeVaga(
                ModalidadeVaga.valueOf(vagaCreateDTO.modalidadeVaga().name())
        );
        vaga.setTipoContrato(
                TipoContrato.valueOf(vagaCreateDTO.tipoContrato().name())
        );
        vaga.setCategoriaVaga(
                CategoriaVaga.valueOf(vagaCreateDTO.categoriaVaga().name())
        );
        vaga.setSalarioTipo(
                SalarioTipo.valueOf(vagaCreateDTO.salarioTipo().name())
        );
        vaga.setSalarioValor(vagaCreateDTO.salarioValor());

        // 📦 LISTAS → JSON
        vaga.setResponsabilidades(
                vagaMapper.toJson(vagaCreateDTO.responsabilidades())
        );
        vaga.setRequisitosObrigatorios(
                vagaMapper.toJson(vagaCreateDTO.requisitosObrigatorios())
        );
        vaga.setRequisitosDesejaveis(
                vagaMapper.toJson(vagaCreateDTO.requisitosDesejaveis())
        );
        vaga.setBeneficios(
                vagaMapper.toJson(vagaCreateDTO.beneficios())
        );

        // ⚙ OUTROS
        vaga.setObservacoes(vagaCreateDTO.observacoes());
        vaga.setContratacaoUrgente(
                Boolean.TRUE.equals(vagaCreateDTO.contratacaoUrgente())
        );
        vaga.setStatusVaga(StatusVaga.ABERTA);
        vaga.setDataPublicacao(LocalDate.now());

        // 📍 RELAÇÕES
        applyLocalizacaoCreate(vaga, vagaCreateDTO.localizacao());
        applyFormacaoCreate(vaga, vagaCreateDTO.formacao());
        applyRequisitosCreate(vaga, vagaCreateDTO.requisitos());
        applyIdiomasReplace(vaga, vagaCreateDTO.idiomas());
        applyCnhsReplace(vaga, vagaCreateDTO.cnhs());

        // 💾 SALVA VAGA
        vagaRepository.save(vaga);

        // 🔑 PALAVRAS-CHAVE
        Set<String> vagaKeys = new HashSet<>();
        vagaKeys.addAll(KeywordExtractor.extract(vaga.getCargo()));
        vagaKeys.addAll(KeywordExtractor.extract(vaga.getDescricao()));

        // 🔔 ALERTA
        alertaService.avisarCandidatos(vaga, vagaKeys);

        return vaga.getIdVaga();
    }

    /* =====================================================
       GET - LISTAR
    ===================================================== */
    @Transactional
    public List<VagaListDTO> listar() {
        return vagaRepository.findAll()
                .stream()
                .map(vaga -> {
                    long totalInteressados =
                            candidaturaRepository.countByVaga_IdVaga(vaga.getIdVaga());

                    vaga.setTotalInteressados((int) totalInteressados);

                    return vagaMapper.toDTO(vaga);
                })
                .toList();
    }

    @Transactional
    public List<VagaListDTO> listarPorCategoria(CategoriaVaga categoriaVaga) {
        return vagaRepository.findByCategoriaVaga(categoriaVaga)
                .stream()
                .map(vaga -> {
                    long totalInteressados =
                            candidaturaRepository.countByVaga_IdVaga(vaga.getIdVaga());

                    vaga.setTotalInteressados((int)totalInteressados);

                    return vagaMapper.toDTO(vaga);
                })
                .toList();
    }


    /* =====================================================
       GET - BUSCAR POR ID
    ===================================================== */
    @Transactional
    public VagaListDTO buscarPorId(Long idVaga) {
        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        long totalInteressados =
                candidaturaRepository.countByVaga_IdVaga(vaga.getIdVaga());

        vaga.setTotalInteressados((int)totalInteressados);

        return vagaMapper.toDTO(vaga);
    }


    /* =====================================================
       MÉTODOS AUXILIARES
    ===================================================== */
    private void applyLocalizacaoCreate(Vaga vaga, VagaLocalizacaoDTO dto) {
        if (dto == null) return;

        VagaLocalizacao loc = new VagaLocalizacao();
        loc.setRua(dto.rua());
        loc.setNumero(dto.numero());
        loc.setComplemento(dto.complemento());
        loc.setBairro(dto.bairro());
        loc.setCidade(dto.cidade());
        loc.setEstado(dto.estado());
        loc.setCep(dto.cep());
        loc.setVaga(vaga);

        vaga.setLocalizacao(loc);
    }

    private void applyFormacaoCreate(Vaga vaga, VagaFormacaoDTO dto) {
        if (dto == null) return;

        VagaFormacao f = new VagaFormacao();
        f.setEscolaridade(dto.escolaridade());
        f.setExperienciaDescricao(dto.experienciaDescricao());
        f.setVaga(vaga);

        vaga.setVagaFormacao(f);
    }

    private void applyRequisitosCreate(Vaga vaga, VagaRequisitoDTO dto) {
        if (dto == null) return;

        VagaRequisitos r = new VagaRequisitos();
        r.setHabilitacao(dto.habilitacao());
        r.setVeiculoProprio(dto.veiculoProprio());
        r.setViajar(dto.viajar());
        r.setMudarResidencia(dto.mudarResidencia());
        r.setObservacoes(dto.observacao());
        r.setVaga(vaga);

        vaga.setRequisitos(r);
    }

    private void applyIdiomasReplace(Vaga vaga, List<VagaIdiomaDTO> dtos) {
        vaga.getIdiomas().clear();

        if (dtos == null || dtos.isEmpty()) return;

        for (VagaIdiomaDTO d : dtos) {
            VagaIdioma i = new VagaIdioma();
            i.setIdioma(d.idioma());
            i.setNivelIdioma(d.nivelIdioma());
            i.setObrigatorio(Boolean.TRUE.equals(d.obrigatorio()));
            i.setVaga(vaga);
            vaga.getIdiomas().add(i);
        }
    }

    private void applyCnhsReplace(Vaga vaga, List<VagaCnhDTO> dtos) {
        vaga.getCnhs().clear();

        if (dtos == null || dtos.isEmpty()) return;

        for (VagaCnhDTO d : dtos) {
            VagaCnh c = new VagaCnh();
            c.setCategoriaCnh(d.tipoCnh());
            c.setVaga(vaga);
            vaga.getCnhs().add(c);
        }
    }

    @Transactional
    public VagaListDTO atualizar(Long id, VagaUpdateDTO vagaUpdateDTO) {

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

    /* =====================
       DADOS PRINCIPAIS
       ===================== */
        vaga.setCargo(vagaUpdateDTO.cargo());
        vaga.setComplemento(vagaUpdateDTO.complemento());

    /* =====================
       EMPRESA (SNAPSHOT)
       ===================== */
        vaga.setEmpresaNome(vagaUpdateDTO.empresaNome());
        vaga.setEmpresaDescricao(vagaUpdateDTO.empresaDescricao());
        vaga.setEmpresaSegmento(vagaUpdateDTO.empresaSegmento());
        vaga.setEmpresaTamanho(vagaUpdateDTO.empresaTamanho());
        vaga.setEmpresaSite(vagaUpdateDTO.empresaSite());
        vaga.setEmpresaConfidencial(
                vagaUpdateDTO.empresaConfidencial() != null
                        ? vagaUpdateDTO.empresaConfidencial()
                        : false
        );

    /* =====================
       CLASSIFICAÇÃO (ENUM)
       ===================== */
        vaga.setModalidadeVaga(
                ModalidadeVaga.valueOf(vagaUpdateDTO.modalidadeVaga().name())
        );

        vaga.setTipoContrato(
                TipoContrato.valueOf(vagaUpdateDTO.tipoContrato().name())
        );

        vaga.setCategoriaVaga(
                CategoriaVaga.valueOf(vagaUpdateDTO.categoriaVaga().name())
        );

    /* =====================
       SALÁRIO
       ===================== */
        vaga.setSalarioTipo(
                SalarioTipo.valueOf(vagaUpdateDTO.salarioTipo().name())
        );
        vaga.setSalarioValor(vagaUpdateDTO.salarioValor());

    /* =====================
       DESCRIÇÃO
       ===================== */
        vaga.setDescricao(vagaUpdateDTO.descricao());
        vaga.setJornada(vagaUpdateDTO.jornada());

    /* =====================
       LISTAS (STRING → ;)
       ===================== */
        vaga.setResponsabilidades(
                vagaUpdateDTO.responsabilidades() != null
                        ? String.join(";", vagaUpdateDTO.responsabilidades())
                        : null
        );

        vaga.setRequisitosObrigatorios(
                vagaUpdateDTO.requisitosObrigatorios() != null
                        ? String.join(";", vagaUpdateDTO.requisitosObrigatorios())
                        : null
        );

        vaga.setRequisitosDesejaveis(
                vagaUpdateDTO.requisitosDesejaveis() != null
                        ? String.join(";", vagaUpdateDTO.requisitosDesejaveis())
                        : null
        );

        vaga.setBeneficios(
                vagaUpdateDTO.beneficios() != null
                        ? String.join(";", vagaUpdateDTO.beneficios())
                        : null
        );

    /* =====================
       OUTROS
       ===================== */
        vaga.setObservacoes(vagaUpdateDTO.observacoes());
        vaga.setContratacaoUrgente(vagaUpdateDTO.contratacaoUrgente());
        vaga.setDataPublicacao(vagaUpdateDTO.dataPublicacao());

    /* =====================
       ONE TO ONE — FORMAÇÃO
       ===================== */
        if (vagaUpdateDTO.formacao() != null) {
            VagaFormacao formacao = new VagaFormacao();
            formacao.setEscolaridade(vagaUpdateDTO.formacao().escolaridade());
            formacao.setExperienciaDescricao(vagaUpdateDTO.formacao().experienciaDescricao());
            formacao.setVaga(vaga);

            vaga.setVagaFormacao(formacao);
        } else {
            vaga.setVagaFormacao(null);
        }

    /* =====================
       ONE TO ONE — REQUISITOS
       ===================== */
        if (vagaUpdateDTO.requisitos() != null) {
            VagaRequisitos requisitos = new VagaRequisitos();
            requisitos.setHabilitacao(vagaUpdateDTO.requisitos().habilitacao());
            requisitos.setVeiculoProprio(vagaUpdateDTO.requisitos().veiculoProprio());
            requisitos.setViajar(vagaUpdateDTO.requisitos().viajar());
            requisitos.setMudarResidencia(vagaUpdateDTO.requisitos().mudarResidencia());
            requisitos.setObservacoes(vagaUpdateDTO.requisitos().observacao());
            requisitos.setVaga(vaga);

            vaga.setRequisitos(requisitos);
        } else {
            vaga.setRequisitos(null);
        }

    /* =====================
       ONE TO MANY — IDIOMAS
       ===================== */
        vaga.getIdiomas().clear();

        if (vagaUpdateDTO.idiomas() != null) {
            for (VagaIdiomaDTO dtoIdioma : vagaUpdateDTO.idiomas()) {
                VagaIdioma idioma = new VagaIdioma();
                idioma.setIdioma(dtoIdioma.idioma());
                idioma.setNivelIdioma(
                        NivelIdioma.valueOf(dtoIdioma.nivelIdioma().name())
                );
                idioma.setVaga(vaga);

                vaga.getIdiomas().add(idioma);
            }
        }

    /* =====================
       ONE TO ONE — LOCALIZAÇÃO
       ===================== */
        if (vagaUpdateDTO.localizacao() != null) {
            VagaLocalizacao localizacao = new VagaLocalizacao();
            localizacao.setCidade(vagaUpdateDTO.localizacao().cidade());
            localizacao.setEstado(vagaUpdateDTO.localizacao().estado());
            localizacao.setVaga(vaga);

            vaga.setLocalizacao(localizacao);
        } else {
            vaga.setLocalizacao(null);
        }

    /* =====================
       ONE TO MANY — CNH
       ===================== */
        vaga.getCnhs().clear();

        if (vagaUpdateDTO.cnhs() != null) {
            for (VagaCnhDTO vagaCnhDTO : vagaUpdateDTO.cnhs()) {

                VagaCnh cnh = new VagaCnh();

                cnh.setCategoriaCnh(
                        CategoriaCnh.valueOf(vagaCnhDTO.tipoCnh().name())
                );

                cnh.setVaga(vaga);
                vaga.getCnhs().add(cnh);
            }
        }

        vagaRepository.save(vaga);

        processoSeletivoService.criarProcesso(vaga);

        return VagaListDTO.fromEntity(vaga);
    }


    /* =====================================================
       DELETAR / ENCERRAR
    ===================================================== */
    @Transactional
    public void deletarVaga(Long idVaga) {

        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() ->
                        new RuntimeException("Vaga não encontrada")
                );

        boolean possuiCandidaturas =
                candidaturaRepository.existsByVaga_IdVaga(idVaga);

        if (possuiCandidaturas) {
            throw new RuntimeException(
                    "Não é possível excluir a vaga pois existem candidaturas associadas"
            );
        }

        vagaRepository.delete(vaga);
    }

    @Transactional
    public void encerrarVaga(Long idVaga) {

        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() ->
                        new RuntimeException("Vaga não encontrada")
                );

        if (vaga.getStatusVaga() == StatusVaga.ENCERRADA) {
            return;
        }

        vaga.setStatusVaga(StatusVaga.ENCERRADA);
        vagaRepository.save(vaga);

        List<Candidatura> candidaturas =
                candidaturaRepository.findAllByVaga_IdVaga(idVaga);

        Set<String> emails = candidaturas.stream()
                .map(c -> c.getCandidato().getUsuario().getEmail())
                .filter(e -> e != null && !e.isBlank())
                .collect(Collectors.toSet());

        emailService.notificarVagaEncerrada(vaga, emails);
    }
}
