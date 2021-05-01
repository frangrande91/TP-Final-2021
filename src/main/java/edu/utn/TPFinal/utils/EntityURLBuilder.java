package edu.utn.TPFinal.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EntityURLBuilder {

    public static String buildURL(String entity, Integer id){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{entity}/{id}")
                .buildAndExpand(entity, id)
                .toUriString();
    }
}
