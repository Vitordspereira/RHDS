package com.hub.hds.models.vaga;

import com.hub.hds.models.empresa.Empresa;
import com.hub.hds.models.recrutador.Recrutador;
import com.hub.hds.models.unidadeEmpresa.UnidadeEmpresa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vaga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaga {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_vaga")
        private Long idVaga;

        // 🔗 RELACIONAMENTOS (FK)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "empresa_id", nullable = false)
        private Empresa empresa;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recrutador_id", nullable = false)
        private Recrutador recrutador;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "unidade_empresa_id", nullable = false)
        private UnidadeEmpresa unidadeEmpresa;

        // TÍTULO
        @Column(nullable = false, length = 150)
        private String cargo;

        @Column(length = 150)
        private String complemento;

        @Transient
        private Integer totalInteressados;

        // EMPRESA (SNAPSHOT)
        @Column(name = "empresa_nome", nullable = false, length = 150)
        private String empresaNome;

        @Column(name = "empresa_descricao", columnDefinition = "TEXT")
        private String empresaDescricao;

        @Column(name = "empresa_segmento", length = 100)
        private String empresaSegmento;

        @Column(name = "empresa_tamanho", length = 50)
        private String empresaTamanho;

        @Column(name = "empresa_site", length = 150)
        private String empresaSite;

        @Column(name = "empresa_confidencial", nullable = false)
        private Boolean empresaConfidencial = false;

        // MODALIDADE / CONTRATO
        @Enumerated(EnumType.STRING)
        @Column(name = "modalidade")
        private ModalidadeVaga modalidadeVaga;

        @Enumerated(EnumType.STRING)
        @Column(name = "tipo_contrato", nullable = false)
        private TipoContrato tipoContrato;

        @Enumerated(EnumType.STRING)
        @Column(name = "categoria",nullable = false)
        private CategoriaVaga categoriaVaga;


        // 💰 SALÁRIO
        @Enumerated(EnumType.STRING)
        @Column(name = "salario_tipo", nullable = false)
        private SalarioTipo salarioTipo = SalarioTipo.COMBINAR;

        @Column(name = "salario_valor", precision = 10, scale = 2)
        private BigDecimal salarioValor;

        // DESCRIÇÃO
        @Column(nullable = false, columnDefinition = "TEXT")
        private String descricao;


        @Column(columnDefinition = "TEXT")
        private String jornada;


        @Column(columnDefinition = "TEXT")
        private String responsabilidades;

        @Column(name = "requisitos_obrigatorios", columnDefinition = "TEXT")
        private String requisitosObrigatorios;

        @Column(name = "requisitos_desejaveis", columnDefinition = "TEXT")
        private String requisitosDesejaveis;

        @Column(columnDefinition = "TEXT")
        private String beneficios;

        // OUTROS
        @Column(columnDefinition = "TEXT")
        private String observacoes;

        @Column(name = "contratacao_urgente", nullable = false)
        private Boolean contratacaoUrgente = false;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private StatusVaga statusVaga = StatusVaga.ABERTA;

        @Column(name = "data_publicacao")
        private LocalDate dataPublicacao;

        // AUDITORIA
        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        // RELAÇÕES 1:1 / 1:N
        @OneToOne(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
        private VagaFormacao vagaFormacao;

        @OneToOne(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
        private VagaRequisitos requisitos;

        @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<VagaIdioma> idiomas = new ArrayList<>();

        // 📍 LOCALIZAÇÃO (1:1)
        @OneToOne(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
        private VagaLocalizacao localizacao;

        // 🚗 CNH (1:N)
        @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<VagaCnh> cnhs = new ArrayList<>();

    @Transient
    public String getTituloFinal() {

        if (complemento == null || complemento.isBlank()) {
            return cargo;
        }

        return cargo + " - " + complemento;
    }

}


