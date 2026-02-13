package com.hub.hds.models.candidato;

import com.hub.hds.models.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "candidato")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidato")
    private Long idCandidato;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @Column(name = "data_nascimento", nullable = false)
    private String dataNascimento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "video_apresentacao")
    private String videoApresentacao;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 🔐 Login
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // 📄 Experiências
    @Builder.Default
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experiencia> experiencias = new ArrayList<>();


    // 🎓 Formações
    @Builder.Default
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formacao> formacoes = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
