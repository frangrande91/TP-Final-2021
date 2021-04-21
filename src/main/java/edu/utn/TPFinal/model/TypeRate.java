package edu.utn.TPFinal.model;

public enum TypeRate {
    A(30.0), B(20.0), C(10.0);

    private Double value;

    TypeRate(Double value){
        this.value = value;
    }

    public Double getValue(){
        return this.value;
    }
}
