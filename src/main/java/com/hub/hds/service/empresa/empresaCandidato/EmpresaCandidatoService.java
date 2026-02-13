package com.hub.hds.service.empresa.empresaCandidato;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.empresa.empresaCandidato.EmpresaCandidato;
import com.hub.hds.dto.dashboardEmpresa.candidato.CandidatoEmpresaDTO;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.empresa.empresaCandidato.EmpresaCandidatoRepository;
import com.hub.hds.repository.empresa.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaCandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaCandidatoRepository empresaCandidatoRepository;

    //LISTAR CANDIDATOS
    public List<CandidatoEmpresaDTO> listarParaEmpresa(Long empresaId) {

        if (!empresaRepository.existsById(empresaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Empresa não encontrada"
            );
        }

        return candidatoRepository.listarParaEmpresa(empresaId);
    }

    //BUSCAR CANDIDATO POR ID

    public CandidatoEmpresaDTO BuscarPorId(Long empresaId, Long candidatoId) {

        //VALIDA EMPRESA
        if (!empresaRepository.existsById(empresaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Empresa não encontrada"
            );
        }

        //VALIDA SE O CANDIDATO ESTÁ CANDIDATADO A VAGA DA EMPRESA
        return candidatoRepository.buscarCandidatoPorEmpresaECandidato(empresaId, candidatoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado nessa empresa"));
    }

    //FAVORITAR CANDIDATO
    public void favoritar(Long empresaId, Long candidatoId) {

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empresa não encontrada"
                ));

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidato não encontrado"
                ));

        EmpresaCandidato relacao = empresaCandidatoRepository
                .findByEmpresaIdEmpresaAndCandidatoIdCandidato(empresaId, candidatoId)
                .orElseGet(() -> EmpresaCandidato.builder()
                        .empresa(empresa)
                        .candidato(candidato)
                        .favoritado(true)
                        .build()
                );

        relacao.setFavoritado(true);
        empresaCandidatoRepository.save(relacao);
    }

    // DESFAVORITAR CANDIDATO
    public void desfavoritar(Long empresaId, Long candidatoId) {

        EmpresaCandidato relacao = empresaCandidatoRepository
                .findByEmpresaIdEmpresaAndCandidatoIdCandidato(empresaId, candidatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidato desfavoritado"
                ));

        relacao.setFavoritado(false);
        empresaCandidatoRepository.save(relacao);
    }
}
