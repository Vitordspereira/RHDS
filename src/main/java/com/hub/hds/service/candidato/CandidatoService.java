package com.hub.hds.service.candidato;

import com.hub.hds.dto.candidato.CandidatoRequest;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import org.springframework.stereotype.Service;

@Service
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;

    public CandidatoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    //ATUALIZAR INFORMAÇÕES DO CANDIDATO MENOS E-MAIL E SENHA
    public CandidatoResponse atualizar(Long id, CandidatoRequest dto){
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Candidato não encontrado"));

        candidato.setNome_completo(dto.nome_completo());
        candidato.setTelefone(dto.telefone());
        candidato.setCidade(dto.cidade());
        candidato.setEstado(dto.estado());
        candidato.setGenero(dto.genero());

        candidatoRepository.save(candidato);

        return new CandidatoResponse(
                candidato.getId_candidato(),
                candidato.getNome_completo(),
                candidato.getEmail(),
                candidato.getTelefone(),
                candidato.getCpf(),
                candidato.getGenero(),
                candidato.getData_nascimento(),
                candidato.getCidade(),
                candidato.getEstado()
        );
    }
}
