package com.hub.hds.models.candidato;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hub.hds.models.experiencia.Experiencia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidatos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidato")
    private Long idCandidato;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    @JsonIgnore
    private String senha;

    @Column(nullable = false, length = 14)
    private String telefone;

    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private LocalDate dataNascimento;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @OneToMany(mappedBy = "candidato", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experiencia> experiencias = new ArrayList<>();

    public void adicionarExperiencia(Experiencia experiencia) {
        experiencias.add(experiencia);
        experiencia.setCandidato(this);
    }
}
