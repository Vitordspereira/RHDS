package com.hub.hds.service.empresa;

import com.hub.hds.dto.empresa.EmpresaCadastroDTO;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import com.hub.hds.models.usuario.Role;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.repository.recrutador.RecrutadorRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final RecrutadorRepository recrutadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaService(
            EmpresaRepository empresaRepository,
            RecrutadorRepository recrutadorRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.empresaRepository = empresaRepository;
        this.recrutadorRepository = recrutadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================
    // CADASTRO DE EMPRESA + RECRUTADOR + USUÁRIO
    // =========================================================
    @Transactional
    public void cadastrarEmpresa(EmpresaCadastroDTO dto) {

        // 1️⃣ Criar USUÁRIO
        // IMPORTANTE: Usei dto.recrutadorDTO() porque é como está no seu DTO/JSON do Front
        Usuario usuario = Usuario.builder()
                .email(dto.recrutadorDTO().emailCorporativo())
                .senha(passwordEncoder.encode(dto.recrutadorDTO().senha()))
                .role(Role.RECRUTADOR)
                .build();

        usuario = usuarioRepository.save(usuario); // Guardamos o usuário salvo

        // 2️⃣ Criar EMPRESA
        Empresa empresa = Empresa.builder()
                .nomeEmpresa(dto.nomeEmpresa())
                .cnpj(dto.cnpj())
                .ramo(dto.ramo())
                .possuiFiliais(dto.possuiFiliais())
                .build();

        // 3️⃣ Criar RECRUTADOR (Vinculando os objetos)
        Recrutador recrutador = Recrutador.builder()
                .nome(dto.recrutadorDTO().nome())
                .emailCorporativo(dto.recrutadorDTO().emailCorporativo())
                .telefone(dto.recrutadorDTO().telefone())
                .empresa(empresa) // Vincula empresa
                .usuario(usuario) // Vincula usuário
                .build();

        // 4️⃣ Preparar as UNIDADES
        if (dto.unidade() == null || dto.unidade().isEmpty()) {
            throw new RuntimeException("A empresa deve possuir ao menos uma unidade (MATRIZ).");
        }

        List<UnidadeEmpresa> unidades = dto.unidade().stream().map(uDto ->
                UnidadeEmpresa.builder()
                        .empresa(empresa)
                        .tipoUnidade(uDto.tipoUnidade())
                        .numeroFuncionarios(uDto.numeroFuncionarios())
                        .build()
        ).collect(Collectors.toList());

        // 5️⃣ O PULO DO GATO: Como você usou CascadeType.ALL nas Entities,
        // basta setar as listas na empresa e salvar a EMPRESA UMA VEZ SÓ.
        empresa.setRecrutadores(Collections.singletonList(recrutador));
        empresa.setUnidadeEmpresas(unidades);

        empresaRepository.save(empresa);
        // Com o Cascade, o save acima vai salvar a Empresa, o Recrutador e as Unidades de uma vez!
    }

    public Long buscarIdEmpresaPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Recrutador recrutador = recrutadorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Recrutador não encontrado"));

        return recrutador.getEmpresa().getIdEmpresa();
    }

}

