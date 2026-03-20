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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Long criar(VagaCreateDTO vagaCreateDTO, String emailUsuario) {
        Recrutador recrutador = getRecrutadorByEmail(emailUsuario);
        Empresa empresa = recrutador.getEmpresa();

        UnidadeEmpresa unidadeMatriz = unidadeEmpresaRepository
                .findByEmpresa_IdEmpresaAndTipoUnidade(empresa.getIdEmpresa(), TipoUnidade.MATRIZ)
                .orElseThrow(() -> new RuntimeException("Empresa não possui unidade matriz cadastrada"));

        Vaga vaga = new Vaga();
        vaga.setEmpresa(empresa);
        vaga.setRecrutador(recrutador);
        vaga.setUnidadeEmpresa(unidadeMatriz);

        mergeIntoEntity(vaga, vagaCreateDTO);
        vaga.setStatusVaga(StatusVaga.ABERTA);
        vaga.setDataPublicacao(LocalDate.now());

        vagaRepository.save(vaga);
        processoSeletivoService.criarProcesso(vaga);
        dispararAlertas(vaga);
        return vaga.getIdVaga();
    }

    @Transactional(readOnly = true)
    public List<VagaListDTO> listar() {
        return vagaRepository.findAll()
                .stream()
                .map(this::attachInteressadosAndMap)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VagaListDTO> listarPorCategoria(CategoriaVaga categoriaVaga) {
        return vagaRepository.findByCategoriaVaga(categoriaVaga)
                .stream()
                .map(this::attachInteressadosAndMap)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VagaListDTO> listarDoRecrutador(String emailUsuario) {
        Recrutador recrutador = getRecrutadorByEmail(emailUsuario);
        return vagaRepository.findByEmpresa_IdEmpresaOrderByCreatedAtDesc(recrutador.getEmpresa().getIdEmpresa())
                .stream()
                .map(this::attachInteressadosAndMap)
                .toList();
    }

    @Transactional(readOnly = true)
    public VagaListDTO buscarPorId(Long idVaga) {
        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        return attachInteressadosAndMap(vaga);
    }

    @Transactional(readOnly = true)
    public VagaListDTO buscarPorIdDoRecrutador(Long idVaga, String emailUsuario) {
        Vaga vaga = getOwnedVaga(idVaga, emailUsuario);
        return attachInteressadosAndMap(vaga);
    }

    @Transactional
    public VagaListDTO atualizar(Long id, VagaUpdateDTO vagaUpdateDTO, String emailUsuario) {
        Vaga vaga = getOwnedVaga(id, emailUsuario);
        mergeIntoEntity(vaga, vagaUpdateDTO);
        if (vagaUpdateDTO.dataPublicacao() != null) {
            vaga.setDataPublicacao(vagaUpdateDTO.dataPublicacao());
        } else if (vaga.getDataPublicacao() == null) {
            vaga.setDataPublicacao(LocalDate.now());
        }

        vagaRepository.save(vaga);
        processoSeletivoService.criarProcesso(vaga);
        return attachInteressadosAndMap(vaga);
    }

    @Transactional
    public void deletarVaga(Long idVaga, String emailUsuario) {
        Vaga vaga = getOwnedVaga(idVaga, emailUsuario);
        boolean possuiCandidaturas = candidaturaRepository.existsByVaga_IdVaga(idVaga);
        if (possuiCandidaturas) {
            throw new RuntimeException("Não é possível excluir a vaga pois existem candidaturas associadas");
        }
        vagaRepository.delete(vaga);
    }

    @Transactional
    public void encerrarVaga(Long idVaga, String emailUsuario) {
        Vaga vaga = getOwnedVaga(idVaga, emailUsuario);
        if (vaga.getStatusVaga() == StatusVaga.ENCERRADA) {
            return;
        }

        vaga.setStatusVaga(StatusVaga.ENCERRADA);
        vagaRepository.save(vaga);

        List<Candidatura> candidaturas = candidaturaRepository.findAllByVaga_IdVaga(idVaga);
        Set<String> emails = candidaturas.stream()
                .map(c -> c.getCandidato().getUsuario().getEmail())
                .filter(e -> e != null && !e.isBlank())
                .collect(Collectors.toSet());

        emailService.notificarVagaEncerrada(vaga, emails);
    }

    private VagaListDTO attachInteressadosAndMap(Vaga vaga) {
        long totalInteressados = candidaturaRepository.countByVaga_IdVaga(vaga.getIdVaga());
        vaga.setTotalInteressados((int) totalInteressados);
        return vagaMapper.toDTO(vaga);
    }

    private Recrutador getRecrutadorByEmail(String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            throw new RuntimeException("Usuário autenticado não encontrado");
        }
        return recrutadorRepository.findByUsuario_Email(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não é um recrutador"));
    }

    private Vaga getOwnedVaga(Long idVaga, String emailUsuario) {
        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        Recrutador recrutador = getRecrutadorByEmail(emailUsuario);
        boolean mesmoRecrutador = vaga.getRecrutador() != null && vaga.getRecrutador().getIdRecrutador().equals(recrutador.getIdRecrutador());
        boolean mesmaEmpresa = vaga.getEmpresa() != null && recrutador.getEmpresa() != null && vaga.getEmpresa().getIdEmpresa().equals(recrutador.getEmpresa().getIdEmpresa());
        if (!mesmoRecrutador && !mesmaEmpresa) {
            throw new RuntimeException("Vaga não pertence à empresa autenticada");
        }
        return vaga;
    }

    private void dispararAlertas(Vaga vaga) {
        Set<String> vagaKeys = new HashSet<>();
        vagaKeys.addAll(KeywordExtractor.extract(vaga.getCargo()));
        vagaKeys.addAll(KeywordExtractor.extract(vaga.getDescricao()));
        alertaService.avisarCandidatos(vaga, vagaKeys);
    }

    private void mergeIntoEntity(Vaga vaga, VagaCreateDTO dto) {
        vaga.setCargo(dto.cargo());
        vaga.setComplemento(dto.complemento());
        vaga.setDescricao(dto.descricao());
        vaga.setJornada(dto.jornada());
        vaga.setEmpresaNome(dto.empresaNome());
        vaga.setEmpresaDescricao(dto.empresaDescricao());
        vaga.setEmpresaSegmento(dto.empresaSegmento());
        vaga.setEmpresaTamanho(dto.empresaTamanho());
        vaga.setEmpresaSite(dto.empresaSite());
        vaga.setEmpresaConfidencial(Boolean.TRUE.equals(dto.empresaConfidencial()));
        vaga.setModalidadeVaga(dto.modalidadeVaga() == null ? null : ModalidadeVaga.valueOf(dto.modalidadeVaga().name()));
        vaga.setTipoContrato(dto.tipoContrato() == null ? null : TipoContrato.valueOf(dto.tipoContrato().name()));
        vaga.setCategoriaVaga(dto.categoriaVaga() == null ? null : CategoriaVaga.valueOf(dto.categoriaVaga().name()));
        vaga.setSalarioTipo(dto.salarioTipo() == null ? SalarioTipo.COMBINAR : SalarioTipo.valueOf(dto.salarioTipo().name()));
        vaga.setSalarioValor(dto.salarioValor());
        vaga.setResponsabilidades(vagaMapper.toJson(dto.responsabilidades()));
        vaga.setRequisitosObrigatorios(vagaMapper.toJson(dto.requisitosObrigatorios()));
        vaga.setRequisitosDesejaveis(vagaMapper.toJson(dto.requisitosDesejaveis()));
        vaga.setBeneficios(vagaMapper.toJson(dto.beneficios()));
        vaga.setObservacoes(dto.observacoes());
        vaga.setContratacaoUrgente(Boolean.TRUE.equals(dto.contratacaoUrgente()));
        applyLocalizacaoCreate(vaga, dto.localizacao());
        applyFormacaoCreate(vaga, dto.formacao());
        applyRequisitosCreate(vaga, dto.requisitos());
        applyIdiomasReplace(vaga, dto.idiomas());
        applyCnhsReplace(vaga, dto.cnhs());
    }

    private void mergeIntoEntity(Vaga vaga, VagaUpdateDTO dto) {
        vaga.setCargo(dto.cargo());
        vaga.setComplemento(dto.complemento());
        vaga.setDescricao(dto.descricao());
        vaga.setJornada(dto.jornada());
        vaga.setEmpresaNome(dto.empresaNome());
        vaga.setEmpresaDescricao(dto.empresaDescricao());
        vaga.setEmpresaSegmento(dto.empresaSegmento());
        vaga.setEmpresaTamanho(dto.empresaTamanho());
        vaga.setEmpresaSite(dto.empresaSite());
        vaga.setEmpresaConfidencial(Boolean.TRUE.equals(dto.empresaConfidencial()));
        vaga.setModalidadeVaga(dto.modalidadeVaga() == null ? null : ModalidadeVaga.valueOf(dto.modalidadeVaga().name()));
        vaga.setTipoContrato(dto.tipoContrato() == null ? null : TipoContrato.valueOf(dto.tipoContrato().name()));
        vaga.setCategoriaVaga(dto.categoriaVaga() == null ? null : CategoriaVaga.valueOf(dto.categoriaVaga().name()));
        vaga.setSalarioTipo(dto.salarioTipo() == null ? SalarioTipo.COMBINAR : SalarioTipo.valueOf(dto.salarioTipo().name()));
        vaga.setSalarioValor(dto.salarioValor());
        vaga.setResponsabilidades(vagaMapper.toJson(dto.responsabilidades()));
        vaga.setRequisitosObrigatorios(vagaMapper.toJson(dto.requisitosObrigatorios()));
        vaga.setRequisitosDesejaveis(vagaMapper.toJson(dto.requisitosDesejaveis()));
        vaga.setBeneficios(vagaMapper.toJson(dto.beneficios()));
        vaga.setObservacoes(dto.observacoes());
        vaga.setContratacaoUrgente(Boolean.TRUE.equals(dto.contratacaoUrgente()));
        applyLocalizacaoCreate(vaga, dto.localizacao());
        applyFormacaoCreate(vaga, dto.formacao());
        applyRequisitosCreate(vaga, dto.requisitos());
        applyIdiomasReplace(vaga, dto.idiomas());
        applyCnhsReplace(vaga, dto.cnhs());
    }

    private void applyLocalizacaoCreate(Vaga vaga, VagaLocalizacaoDTO dto) {
        if (dto == null) {
            vaga.setLocalizacao(null);
            return;
        }
        VagaLocalizacao loc = vaga.getLocalizacao() != null ? vaga.getLocalizacao() : new VagaLocalizacao();
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
        if (dto == null) {
            vaga.setVagaFormacao(null);
            return;
        }
        VagaFormacao f = vaga.getVagaFormacao() != null ? vaga.getVagaFormacao() : new VagaFormacao();
        f.setEscolaridade(dto.escolaridade());
        f.setExperienciaDescricao(dto.experienciaDescricao());
        f.setVaga(vaga);
        vaga.setVagaFormacao(f);
    }

    private void applyRequisitosCreate(Vaga vaga, VagaRequisitoDTO dto) {
        if (dto == null) {
            vaga.setRequisitos(null);
            return;
        }
        VagaRequisitos r = vaga.getRequisitos() != null ? vaga.getRequisitos() : new VagaRequisitos();
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

    @Transactional(readOnly = true)
    public List<VagaListDTO> listarDisponiveisParaCandidato(Long idCandidato) {
        return vagaRepository.listarVagasDisponiveisParaCandidato(idCandidato)
                .stream()
                .map(this::attachInteressadosAndMap)
                .toList();
    }
}
