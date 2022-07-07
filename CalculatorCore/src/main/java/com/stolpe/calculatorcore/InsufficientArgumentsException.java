package com.stolpe.calculatorcore;

public class InsufficientArgumentsException extends RuntimeException {
    public InsufficientArgumentsException() { }

    public InsufficientArgumentsException(String s ) {
        super( s );
    }
}
