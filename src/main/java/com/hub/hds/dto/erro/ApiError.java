package com.hub.hds.dto.erro;

import java.time.Instant;


public record ApiError(
        Instant timestamp,
        String status,
        String error,
        String message,
        String path
) {
}
