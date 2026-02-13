package com.hub.hds.controller.candidato;
import com.hub.hds.models.candidato.Candidato;
import com.hub.hds.repository.candidato.CandidatoRepository;
import com.hub.hds.service.video.CandidatoVideoService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequiredArgsConstructor
@RequestMapping("/candidatos")
public class CandidatoVideoController {

    private final CandidatoVideoService candidatoVideoService;
    private final CandidatoRepository candidatoRepository;

    @Value("${app.upload.video-dir}")
    private String baseDir;

    // Retorna o filename do vídeo salvo para o candidato
    @GetMapping("/{id}/video")
    public ResponseEntity<String> getVideo(@PathVariable Long id) {

        Candidato c = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (c.getVideoApresentacao() == null) {
            return ResponseEntity.noContent().build();
        }

        // retorna SOMENTE o filename
        return ResponseEntity.ok(c.getVideoApresentacao());
    }

    // Upload do vídeo (salvamento definitivo)
    @PostMapping(
            value = "/{idCandidato}/video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> uploadVideo(
            @PathVariable Long idCandidato,
            @RequestParam("video") MultipartFile video
    ) {
        candidatoVideoService.upload(idCandidato, video);
        return ResponseEntity.ok().build();
    }

    // Streaming do vídeo
    @GetMapping("/video/stream/{filename:.+}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String filename) {

        Path path = Paths.get(baseDir)
                .resolve(filename)
                .toAbsolutePath();

        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }

    @DeleteMapping("/{idCandidato}/video/deletar")
    public ResponseEntity<Void> deletarVideo(@PathVariable Long idCandidato) {

        candidatoVideoService.deletarVideo(idCandidato);

        return ResponseEntity.noContent().build(); // 204
    }
}

