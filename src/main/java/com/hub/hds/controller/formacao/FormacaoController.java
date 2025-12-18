package com.hub.hds.controller.formacao;

import com.hub.hds.dto.formacao.FormacaoRequest;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.service.formacao.FormacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/formacoes")
public class FormacaoController {

    private final FormacaoService formacaoService;

    public FormacaoController(FormacaoService formacaoService){
        this.formacaoService = formacaoService;
    }

    @PostMapping
    public FormacaoResponse criar(@RequestBody FormacaoRequest request){
        return formacaoService.criar(request);
    }

    @GetMapping
    public List<FormacaoResponse> listar()
    {
        return formacaoService.listar();
    }

    @GetMapping("/{id}")
    public FormacaoResponse buscar(@PathVariable Long id){
        return formacaoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public FormacaoResponse atualizar(@PathVariable Long id, @RequestBody FormacaoRequest request){
        return formacaoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        formacaoService.deletar(id);
        return "Formação deletada";
    }
}
