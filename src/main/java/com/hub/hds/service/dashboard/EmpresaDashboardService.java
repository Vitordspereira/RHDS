package com.hub.hds.service.dashboard;

import com.hub.hds.dto.dashboardEmpresa.EmpresaDashboardDTO;
import com.hub.hds.dto.dashboardEmpresa.RecrutadoresDTO;
import com.hub.hds.dto.dashboardEmpresa.UnidadeEmpresaDTO;
import com.hub.hds.dto.dashboardEmpresa.atualizar.EmpresaUpdateDTO;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.repository.empresa.EmpresaRepository;
import com.hub.hds.repository.recrutador.RecrutadorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaDashboardService {

    private final EmpresaRepository empresaRepository;
    private final RecrutadorRepository recrutadorRepository;

    public EmpresaDashboardService(EmpresaRepository empresaRepository, RecrutadorRepository recrutadorRepository) {
        this.empresaRepository = empresaRepository;
        this.recrutadorRepository = recrutadorRepository;
    }

    @Transactional
    public EmpresaDashboardDTO buscarEmpresaLogada(Long idEmpresa) {

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        List<UnidadeEmpresaDTO> unidades = empresa.getUnidadeEmpresas()
                .stream()
                .map(unidade -> new UnidadeEmpresaDTO(
                        unidade.getIdUnidadeEmpresa(),
                        unidade.getNumeroFuncionarios()
                ))
                .toList();

        List<RecrutadoresDTO> recrutadores = empresa.getRecrutadores()
                .stream()
                .map(recrutador -> new RecrutadoresDTO(
                        recrutador.getIdRecrutador(),
                        recrutador.getNome(),
                        recrutador.getEmailCorporativo(),
                        recrutador.getTelefone()
                ))
                .toList();

        return new EmpresaDashboardDTO(
                empresa.getIdEmpresa(),
                empresa.getNomeEmpresa(),
                empresa.getCnpj(),
                empresa.getRamo(),
                empresa.getPossuiFiliais(),
                unidades,
                recrutadores
        );
    }

    @Transactional
    public void atualizarEmpresaDoUsuario(EmpresaUpdateDTO empresaUpdateDTO, String emailUsuario) {

        Recrutador recrutador = recrutadorRepository
                .findByUsuario_Email(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Empresa empresa = recrutador.getEmpresa();

        empresa.setNomeEmpresa(empresaUpdateDTO.nomeEmpresa());
        empresa.setRamo(empresaUpdateDTO.ramo());
        empresa.setPossuiFiliais(empresaUpdateDTO.possuiFiliais());
    }
}
