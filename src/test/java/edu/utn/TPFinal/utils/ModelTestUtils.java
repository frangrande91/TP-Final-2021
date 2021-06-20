package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Model;

public class ModelTestUtils {

    public static Model aModel() {
        return Model.builder().id(1).name("Finder").build();
    }
}
