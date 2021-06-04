package edu.utn.TPFinal.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class EntityURLBuilder {
    public static <T> URI buildURL2(final String entity, final T id) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("{entity}/{id}")
                .buildAndExpand(entity,id)
                .toUri();
    }
}
