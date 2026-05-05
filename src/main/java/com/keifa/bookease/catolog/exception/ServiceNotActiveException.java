package com.keifa.bookease.catolog.exception;

public class ServiceNotActiveException extends RuntimeException {
    public ServiceNotActiveException() {
        super("O serviço não foi encontrado ou está inativo.");
    }
}
