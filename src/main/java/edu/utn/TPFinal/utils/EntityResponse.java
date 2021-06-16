package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.exception.FromToInvalidException;
import edu.utn.TPFinal.model.response.Response;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.zip.DataFormatException;

public class EntityResponse{

    public static <T> ResponseEntity<List<T>> listResponse(Page<T> page) {
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

    public static Response messageResponse(String message) {
        return Response.builder().message(message).build();
    }
}
