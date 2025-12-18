package com.hub.hds.controller.candidato;

import com.hub.hds.dto.candidato.CandidatoRequest;
import com.hub.hds.dto.candidato.CandidatoResponse;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;

    public CandidatoController(CandidatoRepository candidatoRepository, PasswordEncoder passwordEncoder) {
        this.candidatoRepository = candidatoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public CandidatoResponse criar(@Valid @RequestBody CandidatoRequest dto){
        Candidato candidato = Candidato.builder()
                .nome_completo(dto.nome_completo())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .telefone(dto.telefone())
                .cpf(dto.cpf())
                .genero(dto.genero())
                .data_nascimento(dto.data_nascimento())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .build();

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

    @PutMapping("/{id}")
    public ResponseEntity<CandidatoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CandidatoRequest dto
    ) {
        return candidatoRepository.findById(id)
                .map(candidato -> {

                    candidato.setNome_completo(dto.nome_completo());
                    candidato.setEmail(dto.email());
                    candidato.setTelefone(dto.telefone());
                    candidato.setCpf(dto.cpf());
                    candidato.setGenero(dto.genero());
                    candidato.setData_nascimento(dto.data_nascimento());
                    candidato.setCidade(dto.cidade());
                    candidato.setEstado(dto.estado());

                    // Só atualiza senha se estiver preenchida
                    if (dto.senha() != null && !dto.senha().isBlank()) {
                        candidato.setSenha(passwordEncoder.encode(dto.senha()));
                    }

                    candidatoRepository.save(candidato);

                    return ResponseEntity.ok(
                            new CandidatoResponse(
                                    candidato.getId_candidato(),
                                    candidato.getNome_completo(),
                                    candidato.getEmail(),
                                    candidato.getTelefone(),
                                    candidato.getCpf(),
                                    candidato.getGenero(),
                                    candidato.getData_nascimento(),
                                    candidato.getCidade(),
                                    candidato.getEstado()
                            )
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!candidatoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        candidatoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<CandidatoResponse> listarTodos() {
        return candidatoRepository.findAll()
                .stream()
                .map(c -> new CandidatoResponse(
                        c.getId_candidato(),
                        c.getNome_completo(),
                        c.getEmail(),
                        c.getTelefone(),
                        c.getCpf(),
                        c.getGenero(),
                        c.getData_nascimento(),
                        c.getCidade(),
                        c.getEstado()
                )).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponse> buscar(@PathVariable Long id){
        return candidatoRepository.findById(id)
                .map(c ->ResponseEntity.ok( new CandidatoResponse(
                        c.getId_candidato(),
                        c.getNome_completo(),
                        c.getEmail(),
                        c.getTelefone(),
                        c.getCpf(),
                        c.getGenero(),
                        c.getData_nascimento(),
                        c.getCidade(),
                        c.getEstado()
                        )
                )).orElse(ResponseEntity.notFound().build());
    }
}
