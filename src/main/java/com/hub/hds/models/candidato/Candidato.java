package com.hub.hds.models.candidato;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hub.hds.models.experiencia.Experiencia;
import com.hub.hds.models.formacao.Formacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private Long id_candidato;

    @Column(nullable = false, length = 150)
    private String nome_completo;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String senha;

    @Column(nullable = false, length = 14)
    private String telefone;

    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private LocalDate data_nascimento;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @OneToMany(mappedBy = "candidatos", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Formacao> formacaos;

    @OneToMany(mappedBy = "candidatos", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Experiencia> experiencias;


}
