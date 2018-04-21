package com.d3.exceptions;

public class D3FactoryException extends RuntimeException {

    public D3FactoryException(String message) {
        super(message);
    }

    public D3FactoryException(ReflectiveOperationException e) {
       super(e);
    }
}
