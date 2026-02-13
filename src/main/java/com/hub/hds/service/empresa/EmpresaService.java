package com.hub.hds.service.empresa;

import com.hub.hds.dto.empresa.EmpresaCadastroDTO;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import com.hub.hds.models.usuario.Role;
import com.hub.hds.models.usuario.Usuario;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.repository.recrutador.RecrutadorRepository;
import com.hub.hds.repository.unidadeEmpresa.UnidadeEmpresaRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final RecrutadorRepository recrutadorRepository;
    private final UnidadeEmpresaRepository unidadeEmpresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaService(
            EmpresaRepository empresaRepository,
            RecrutadorRepository recrutadorRepository,
            UnidadeEmpresaRepository unidadeEmpresaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.empresaRepository = empresaRepository;
        this.recrutadorRepository = recrutadorRepository;
        this.unidadeEmpresaRepository = unidadeEmpresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================
    // CADASTRO DE EMPRESA + RECRUTADOR + USUÁRIO
    // =========================================================
    @Transactional
    public void cadastrarEmpresa(EmpresaCadastroDTO dto) {

        // 1️⃣ Criar USUÁRIO (LOGIN)
        Usuario usuario = Usuario.builder()
                .email(dto.recrutador().emailCorporativo())
                .senha(passwordEncoder.encode(dto.recrutador().senha()))
                .role(Role.RECRUTADOR)
                .build();

        usuarioRepository.saveAndFlush(usuario);

        // 2️⃣ Criar EMPRESA
        Empresa empresa = Empresa.builder()
                .nomeEmpresa(dto.nomeEmpresa())
                .cnpj(dto.cnpj())
                .ramo(dto.ramo())
                .possuiFiliais(dto.possuiFiliais())
                .build();

        empresaRepository.save(empresa);

        // 3️⃣ Criar RECRUTADOR
        Recrutador recrutador = Recrutador.builder()
                .nome(dto.recrutador().nome())
                .emailCorporativo(dto.recrutador().emailCorporativo())
                .telefone(dto.recrutador().telefone())
                .empresa(empresa)
                .usuario(usuario)
                .build();

        recrutadorRepository.save(recrutador);

        // 4️⃣ Criar UNIDADES DA EMPRESA
        List<com.hub.hds.dto.empresa.UnidadeEmpresaDTO> unidades =
                dto.unidade();

        if (unidades == null || unidades.isEmpty()) {
            throw new RuntimeException(
                    "A empresa deve possuir ao menos uma unidade (MATRIZ)."
            );
        }

        for (com.hub.hds.dto.empresa.UnidadeEmpresaDTO unidadeDTO : unidades) {
            UnidadeEmpresa unidade = UnidadeEmpresa.builder()
                    .empresa(empresa)
                    .tipoUnidade(unidadeDTO.tipoUnidade())
                    .numeroFuncionarios(unidadeDTO.numeroFuncionarios())
                    .build();

            unidadeEmpresaRepository.save(unidade);
        }
    }

    public Long buscarIdEmpresaPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Recrutador recrutador = recrutadorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Recrutador não encontrado"));

        return recrutador.getEmpresa().getIdEmpresa();
    }

}

