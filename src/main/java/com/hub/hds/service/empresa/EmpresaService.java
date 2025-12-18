package com.hub.hds.service.empresa;

import com.hub.hds.dto.empresa.EmpresaRequest;
import com.hub.hds.dto.empresa.EmpresaResponse;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.repository.empresa.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaResponse cadastrar(EmpresaRequest request){

        if(empresaRepository.existsByEmail(request.email())) {
            throw new RuntimeException("E-mail já possui cadastrado");
        }

        Empresa empresa = Empresa.builder()
                .matrizFilial(request.matrizFilial())
                .numeroFuncionarios(request.numeroFuncionarios())
                .filial_numero_funcionarios(request.filial_numero_funcionarios())
                .cnpj(request.cnpj())
                .ramo(request.ramo())
                .nome_responsavel(request.nome_responsavel())
                .email(request.email())
                .celular(request.celular())
                .senha(passwordEncoder.encode(request.senha()))
                .build();

        Empresa salva = empresaRepository.save(empresa);

        return new EmpresaResponse(
                salva.getId_empresa(),
                salva.getMatrizFilial(),
                salva.getNumeroFuncionarios(),
                salva.getFilial_numero_funcionarios(),
                salva.getCnpj(),
                salva.getRamo(),
                salva.getNome_responsavel(),
                salva.getEmail(),
                salva.getCelular(),
                salva.getCriado_em()
        );
    }
}
