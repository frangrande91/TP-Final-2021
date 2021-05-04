package edu.utn.TPFinal.model;

public enum TypeUser {

    EMPLOYEE("Employee"),
    CLIENT("Client");

    private String description;

    TypeUser(String description) {
        this.description = description;
    }

    public static TypeUser find(final String value) {

        for (TypeUser p : values()) {
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
