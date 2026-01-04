package com.hub.hds.service.empresaRecrutador;

import com.hub.hds.dto.empresaRecrutador.EmpresaRecrutadorRequest;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.usuario.*;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.repository.recrutador.RecrutadorRepository;
import com.hub.hds.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmpresaRecrutadorService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final RecrutadorRepository recrutadorRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaRecrutadorService(
            UsuarioRepository usuarioRepository,
            EmpresaRepository empresaRepository,
            RecrutadorRepository recrutadorRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.recrutadorRepository = recrutadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrar(EmpresaRecrutadorRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.RECRUTADOR);
        usuarioRepository.save(usuario);

        Empresa empresa = new Empresa();
        empresa.setNome(request.nomeEmpresa());
        empresa.setCnpj(request.cnpj());
        empresa.setRamo(request.ramo());
        empresa.setPossuiFiliais(request.possuiFiliais());
        empresa.setNumeroFuncionarios(request.numeroFuncionarios());
        empresaRepository.save(empresa);

        Recrutador recrutador = new Recrutador();
        recrutador.setUsuario(usuario);
        recrutador.setEmpresa(empresa);
        recrutador.setNome(request.nomeRecrutador());
        recrutador.setTelefone(request.telefone());
        recrutadorRepository.save(recrutador);
    }
}
