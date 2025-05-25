package com.meudroz.backend_test_java.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@UtilityClass
public final class ResponseUtil {

    public static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse) {
        return maybeResponse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
