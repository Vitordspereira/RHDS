package com.hub.hds.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException() {
        super("Email já cadastrado");
    }
}