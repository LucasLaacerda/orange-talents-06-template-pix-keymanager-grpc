package br.com.zup.edu.grpc.handlers

import br.com.zup.edu.grpc.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class PixKeyExistingExceptionHandler: ExceptionHandler<PixKeyExistingException> {
    override fun handle(e: PixKeyExistingException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription("Chave pix invalida")
            .augmentDescription("Pix informado ja foi cadastrado no sistema")
            .asRuntimeException())
    }

    override fun supports(e: Exception): Boolean {
        return e is PixKeyExistingException
    }

}