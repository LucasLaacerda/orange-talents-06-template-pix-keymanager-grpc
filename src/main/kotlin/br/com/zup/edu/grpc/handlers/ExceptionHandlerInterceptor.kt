package br.com.zup.edu.grpc.handlers

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
//@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerInterceptor(@Inject private val resolver: ExceptionHandlerResolver): MethodInterceptor<BindableService,Any> {

    override fun intercept(context: MethodInvocationContext<BindableService,Any?>):Any? {
        try{
            return context.proceed()
        }catch (e: Exception){

//            val status = when(e) {
//                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message).asRuntimeException()
//                is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message).asRuntimeException()
//                is PixKeyExistingException -> Status.ALREADY_EXISTS.withDescription(e.message).asRuntimeException()
//                is ClassNotFoundException -> Status.NOT_FOUND.withDescription(e.message).asRuntimeException()
//                else -> Status.UNKNOWN.asRuntimeException()
//            }
            val handler = resolver.resolve(e) as ExceptionHandler<Exception>
            val status = handler.handle(e)
//
            GrpcEndpointArguments(context).response()
                .onError(status.asRuntimeException())
            return null

        }
        return null
    }

    private class GrpcEndpointArguments(val context: MethodInvocationContext<BindableService, Any?>){
        fun response(): StreamObserver<*>{
            return context.parameterValues[1] as StreamObserver<*>
        }
    }

}