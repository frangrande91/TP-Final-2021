package edu.utn.TPFinal.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class EntityResponse{

    public static <T> ResponseEntity<List<T>> response(Page<T> page) {
        if (!page.getContent().isEmpty()) {
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(page.getTotalElements())).
                    header("X-Total-Pages", Long.toString(page.getTotalPages())).
                    body(page.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(page.getContent());
        }

    }
}
