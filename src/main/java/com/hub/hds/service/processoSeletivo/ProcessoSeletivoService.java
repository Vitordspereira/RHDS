package com.hub.hds.service.processoSeletivo;

import com.hub.hds.dto.processoSeletivo.EtapaProcessoDTO;
import com.hub.hds.models.processoSeletivo.EtapaProcesso;
import com.hub.hds.models.processoSeletivo.ProcessoSeletivo;
import com.hub.hds.models.vaga.Vaga;
import com.hub.hds.repository.processoSeletivo.ProcessoSeletivoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessoSeletivoService {

    private final ProcessoSeletivoRepository processoSeletivoRepository;

    public void criarProcesso (Vaga vaga) {

        ProcessoSeletivo processoSeletivo = new ProcessoSeletivo();
        processoSeletivo.setVaga(vaga);

        processoSeletivoRepository.save(processoSeletivo);

        criarEtapas(processoSeletivo);
    }

    private void criarEtapas(ProcessoSeletivo processoSeletivo) {

        List<String> etapas = List.of(
                "Inscrição",
                "Triagem",
                "Entrevista com RH",
                "Entrevista Técnica",
                "Proposta",
                "Finalizado"
        );

        int ordem = 1;

        for (String nome : etapas) {
            EtapaProcesso etapaProcesso = new EtapaProcesso();
            etapaProcesso.setNome(nome);
            etapaProcesso.setOrdemProcesso(ordem++);
            etapaProcesso.setProcessoSeletivo(processoSeletivo);

            processoSeletivo.getEtapaProcessos().add(etapaProcesso);
        }
    }



    @Transactional(readOnly = true)
    public List<EtapaProcessoDTO> listarEtapas(Long idVaga) {

        ProcessoSeletivo processoSeletivo = processoSeletivoRepository
                .findByVaga_IdVaga(idVaga)
                .orElseThrow(() -> new RuntimeException("Processo seletivo não encontrado"));

        return processoSeletivo.getEtapaProcessos()
                .stream()
                .map(EtapaProcessoDTO::fromEntity)
                .toList();
    }

}
