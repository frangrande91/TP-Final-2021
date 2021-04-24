package edu.utn.TPFinal.model;

public enum TypePerson {

    EMPLOYEE("Employee"),
    CLIENT("Client");

    private String description;

    TypePerson(String description) {
        this.description = description;
    }

    public static TypePerson find(final String value) {

        for (TypePerson p : values()) {
            if(p.toString().equalsIgnoreCase(value)) {
                return p;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid TypePerson: %s",value));
    }

    public String getDescription() {
        return this.description;
    }

}
