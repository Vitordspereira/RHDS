package com.hub.hds.models.candidatura;

import java.util.Set;

public enum StatusCandidatura {

    ENVIADA,
    EM_ANALISE,
    ENTREVISTA,
    APROVADO,
    REPROVADO,
    CANCELADA;

    public static final Set<StatusCandidatura> STATUS_FINAIS =
            Set.of(APROVADO, REPROVADO, CANCELADA);

    public boolean isFinal() {
        return STATUS_FINAIS.contains(this);
    }


    public boolean podeIrPara(StatusCandidatura destino) {
        if (destino == null) return false;

        return switch (this) {
            case ENVIADA ->
                    destino == EM_ANALISE;

            case EM_ANALISE ->
                    destino == ENTREVISTA || destino == REPROVADO || destino == CANCELADA;

            case ENTREVISTA ->
                    destino == APROVADO || destino == REPROVADO;

            default -> false;
        };
    }
}

