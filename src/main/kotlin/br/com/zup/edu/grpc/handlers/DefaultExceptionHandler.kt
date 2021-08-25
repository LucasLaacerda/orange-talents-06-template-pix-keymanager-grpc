package br.com.zup.edu.grpc.handlers

class DefaultExceptionHandler : ExceptionHandler<Exception> {
    override fun handle(e: Exception): ExceptionHandler.StatusWithDetails {
        val status = when(e){
            is IllegalArgumentException -> invalidArgumentHandler(e.message)
            is IllegalStateException -> failedPreconditionHandler(e.message)
            is PixKeyExistingException -> alreadyExistsHandler(e.message)
            else -> defaultHandler(e.message)
        }
        return ExceptionHandler.StatusWithDetails(status)
    }

    override fun supports(e: Exception): Boolean {
        return true
    }

}
