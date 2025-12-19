package com.hub.hds.controller.experiencia;

import com.hub.hds.dto.experiencia.ExperienciaRequest;
import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.service.experiencia.ExperienciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/experiencias")
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    public ExperienciaController(ExperienciaService experienciaService) {
        this.experienciaService = experienciaService;
    }

    @PostMapping
    public ExperienciaResponse criar(@RequestBody ExperienciaRequest request) {
        return experienciaService.criar(request);
    }

    @GetMapping
    public List<ExperienciaResponse> listar()
    {
        return experienciaService.listar();
    }

    @GetMapping("/{id}")
    public ExperienciaResponse buscar(@PathVariable Long id){
        return experienciaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ExperienciaResponse atualizar(@PathVariable Long id, @RequestBody ExperienciaRequest request){
        return experienciaService.atualizar(id);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        experienciaService.deletar(id);
        return "Experiência deletada";
    }
}

