package br.com.zup.edu.grpc.handlers

import io.grpc.Status
import javax.inject.Singleton

@Singleton
class PixKeyExistingExceptionHandler: ExceptionHandler<Exception> {
    override fun handle(e: Exception): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription(e.message)
            .asRuntimeException())
    }

    override fun supports(e: Exception): Boolean {
        return e is PixKeyExistingException
    }

}