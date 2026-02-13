package com.hub.hds.service.video;

import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidatoVideoService {

    private final CandidatoRepository candidatoRepository;

    /**
     * Diretório base configurado no application.yml
     * Ex: ${user.home}/hds/uploads/videos
     */
    @Value("${app.upload.video-dir}")
    private String baseDir;

    private static final long MAX_SIZE = 100L * 1024 * 1024; // 100MB

    /**
     * Upload + salvamento definitivo do vídeo do candidato
     */
    @Transactional
    public void upload(Long candidatoId, MultipartFile video) {

        // 1) valida se veio arquivo
        if (video == null || video.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie um vídeo");
        }

        // 2) valida MIME type
        if (video.getContentType() == null || !video.getContentType().startsWith("video/")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Arquivo inválido (é necessário enviar um vídeo)"
            );
        }

        // 3) valida tamanho
        if (video.getSize() > MAX_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vídeo acima do limite de 100MB"
            );
        }

        // 4) busca candidato
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidato não encontrado"
                ));

        // 5) remove vídeo antigo (se existir)
        if (candidato.getVideoApresentacao() != null && !candidato.getVideoApresentacao().isBlank()) {
            deleteOldVideo(candidato.getVideoApresentacao());
        }

        // 6) gera nome seguro do arquivo
        String extension = getExtension(video.getOriginalFilename());
        if (extension.isBlank()) {
            extension = ".mp4";
        }

        String filename = "cand_" + candidatoId + "_" + UUID.randomUUID() + extension;
        Path target = Paths.get(baseDir).resolve(filename).normalize().toAbsolutePath();

        // 7) salva arquivo no disco
        try {
            Files.createDirectories(target.getParent());
            Files.copy(video.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao salvar o vídeo",
                    e
            );
        }

        // 8) salva SOMENTE o nome do arquivo no banco
        candidato.setVideoApresentacao(filename);
        candidatoRepository.save(candidato);
    }

    @Transactional
    public void deletarVideo(Long candidatoId) {

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidato não encontrado"
                ));

        String filename = candidato.getVideoApresentacao();

        if (filename == null || filename.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Candidato não possui vídeo cadastrado"
            );
        }

        Path path = Paths.get(baseDir)
                .resolve(filename)
                .normalize()
                .toAbsolutePath();

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao deletar o vídeo",
                    e
            );
        }

        candidato.setVideoApresentacao(null);
        candidatoRepository.save(candidato);
    }

    /**
     * Remove o vídeo antigo do disco
     */
    private void deleteOldVideo(String filename) {
        Path path = Paths.get(baseDir).resolve(filename).normalize().toAbsolutePath();
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // não quebra o fluxo se falhar a exclusão
        }
    }

    /**
     * Extrai extensão do arquivo
     */
    private String getExtension(String originalName) {
        if (originalName == null) return "";
        int idx = originalName.lastIndexOf('.');
        return idx < 0 ? "" : originalName.substring(idx).toLowerCase();
    }
}

