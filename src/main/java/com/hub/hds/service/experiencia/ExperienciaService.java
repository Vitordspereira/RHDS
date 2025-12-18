package com.hub.hds.service.experiencia;

import com.hub.hds.dto.experiencia.ExperienciaRequest;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.models.experiencia.Experiencia;
import com.hub.hds.repository.experiencia.ExperienciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienciaService {

    private final ExperienciaRepository experienciaRepository;

    public ExperienciaService(ExperienciaRepository experienciaRepository) {
        this.experienciaRepository = experienciaRepository;
    }

    //CRIAR
    public ExperienciaResponse criar(ExperienciaRequest request){

        Experiencia experiencia = Experiencia.builder()
                .nome_empresa(request.nome_empresa())
                .funcao(request.funcao())
                .descricao(request.descricao())
                .outras_experiencias(request.outras_experiencias())
                .habilidades(request.habilidades())
                .periodo_inicio(request.periodo_inicio())
                .periodo_fim(request.periodo_fim())
                .build();

        Experiencia salva = experienciaRepository.save(experiencia);

        return mapToResponse(salva);
    }

    //LISTAR TODOS
    public List<ExperienciaResponse> listar(){
        return experienciaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //BUSCAR POR ID
    public ExperienciaResponse buscarPorId(Long id){
        Experiencia experiencia = experienciaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Experiência não encontrada"));

        return mapToResponse(experiencia);
    }

    //ATUALIZAR
    public ExperienciaResponse atualizar(Long id, ExperienciaRequest request){
        Experiencia experiencia = experienciaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Experiência não encontrada"));

        experiencia.setNome_empresa(request.nome_empresa());
        experiencia.setFuncao(request.funcao());
        experiencia.setDescricao(request.descricao());
        experiencia.setOutras_experiencias(request.outras_experiencias());
        experiencia.setHabilidades(request.habilidades());
        experiencia.setPeriodo_inicio(request.periodo_inicio());
        experiencia.setPeriodo_fim(request.periodo_fim());

        Experiencia atualizada = experienciaRepository.save(experiencia);
        return mapToResponse(atualizada);
    }

    //DELETAR
    public void deletar(Long id) {experienciaRepository.deleteById(id);}

    //MapToResponse
    private ExperienciaResponse mapToResponse(Experiencia experiencia){
        return new ExperienciaResponse(
                experiencia.getId_experiencia(),
                experiencia.getNome_empresa(),
                experiencia.getFuncao(),
                experiencia.getDescricao(),
                experiencia.getOutras_experiencias(),
                experiencia.getHabilidades(),
                experiencia.getPeriodo_inicio(),
                experiencia.getPeriodo_fim()
        );
    }
}

