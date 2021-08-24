package br.com.zup.edu.grpc

import br.com.zup.edu.pix.ErrorHandler
import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerInterceptor(@Inject private val resolver: ExceptionHandlerResolver): MethodInterceptor<BindableService,Any> {

    override fun intercept(context: MethodInvocationContext<BindableService,Any?>):Any? {
        try{
            return context.proceed()
        }catch (e: Exception){

            val handler = resolver.resolve(e) as ExceptionHandler<Exception>
            val status = handler.handle(e)

            GrpcEndpointArguments(context).response()
                .onError(status.asRuntimeException())

            return null
        }
    }

    private class GrpcEndpointArguments(val context: MethodInvocationContext<BindableService, Any?>){
        fun response(): StreamObserver<*>{
            return context.parameterValues[1] as StreamObserver<*>
        }
    }

}