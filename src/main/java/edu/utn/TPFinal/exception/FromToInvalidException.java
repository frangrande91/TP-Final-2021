package edu.utn.TPFinal.exception;

public class FromToInvalidException extends RuntimeException {
    public FromToInvalidException() {
        super("The from can not be after than to");
    }
}
