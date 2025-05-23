package com.meudroz.backend_test_java.utils;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ResponseUtil {

    public static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse) {
        return maybeResponse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
