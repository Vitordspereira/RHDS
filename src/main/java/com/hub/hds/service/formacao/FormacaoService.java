package com.hub.hds.service.formacao;

import com.hub.hds.dto.formacao.FormacaoRequest;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.models.formacao.Formacao;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.repository.formacao.FormacaoRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class FormacaoService {

    private final FormacaoRepository formacaoRepository;
    private final CandidatoRepository candidatoRepository;

    public FormacaoService(FormacaoRepository formacaoRepository, CandidatoRepository candidatoRepository) {

        this.formacaoRepository = formacaoRepository;
        this.candidatoRepository = candidatoRepository;

    }

    //CRIAR
    public FormacaoResponse criar(FormacaoRequest request){

        Formacao formacao = Formacao.builder()
                .nome_curso(request.nome_curso())
                .instituicao(request.instituicao())
                .status(request.status())
                .periodo_inicio(request.periodo_inicio())
                .periodo_fim(request.periodo_fim())
                .build();

        Formacao salva = formacaoRepository.save(formacao);

        return mapToResponse(salva);
    }

    //LISTAR TODOS
    public List<FormacaoResponse> listar(){
        return formacaoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //BUSCAR POR ID
    public FormacaoResponse buscarPorId(Long id){
        Formacao formacao = formacaoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Formação não encontrada"));

        return mapToResponse(formacao);
    }

    //ATUALIZAR
    public FormacaoResponse atualizar(Long id, FormacaoRequest request){
        Formacao formacao = formacaoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Formação não encontrada"));

        formacao.setNome_curso(request.nome_curso());
        formacao.setInstituicao(request.instituicao());
        formacao.setStatus(request.status());
        formacao.setPeriodo_inicio(request.periodo_inicio());
        formacao.setPeriodo_fim(request.periodo_fim());

        Formacao atualizada = formacaoRepository.save(formacao);
        return mapToResponse(atualizada);
    }

    // DELETAR
    public void deletar(Long id){
        formacaoRepository.deleteById(id);
    }

    //MapToResponse
    private FormacaoResponse mapToResponse(Formacao formacao){
        return new FormacaoResponse(
                formacao.getId_formacao(),
                formacao.getNome_curso(),
                formacao.getInstituicao(),
                formacao.getStatus(),
                formacao.getPeriodo_inicio(),
                formacao.getPeriodo_fim()
        );
    }
}
