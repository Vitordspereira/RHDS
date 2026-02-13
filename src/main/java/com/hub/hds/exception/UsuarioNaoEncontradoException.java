package com.hub.hds.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado");
    }
}
