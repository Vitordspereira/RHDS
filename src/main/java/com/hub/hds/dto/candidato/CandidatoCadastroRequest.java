package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;
import java.util.List;

public record CandidatoCadastroRequest(

        // 🔐 login
        String email,
        String senha,

        // 👤 dados pessoais
        String nomeCompleto,
        String cpf,
        String telefone,
        Genero genero,
        String dataNascimento, // YYYY-MM
        String cidade,
        String estado,

        // 📄 currículo
        List<ExperienciaRequest> experiencias,
        List<FormacaoRequest> formacoes
) {}
