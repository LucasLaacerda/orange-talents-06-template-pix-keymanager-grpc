package br.com.zup.edu.grpc.handlers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandlerResolver(@Inject private val handlers: List<ExceptionHandler<Exception>>) {

    private var defaultHandler: ExceptionHandler<Exception> = DefaultExceptionHandler()

    constructor(handlers: List<ExceptionHandler<Exception>>, defaultHandler: ExceptionHandler<Exception>): this(handlers){
        this.defaultHandler = defaultHandler
    }

    fun resolve(e: Exception): ExceptionHandler<Exception> {
        val foundHandles = handlers.filter{ h -> h.supports(e)}
        if (foundHandles.size > 1)
            throw IllegalStateException("Mais de um handler em uma unica excpetion")

        return foundHandles.firstOrNull() ?: defaultHandler
    }

}
