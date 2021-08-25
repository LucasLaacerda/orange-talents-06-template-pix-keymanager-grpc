package br.com.zup.edu.grpc.handlers

import io.grpc.Status
import javax.inject.Singleton

@Singleton
class PixKeyExistingExceptionHandler: ExceptionHandler<Exception> {
    override fun handle(e: Exception): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription("Chave pix invalida")
            .augmentDescription("Pix informado ja foi cadastrado no sistema")
            .asRuntimeException())
    }

    override fun supports(e: Exception): Boolean {
        return e is PixKeyExistingException
    }

}