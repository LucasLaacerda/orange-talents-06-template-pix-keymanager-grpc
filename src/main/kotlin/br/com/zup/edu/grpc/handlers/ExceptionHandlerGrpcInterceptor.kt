package br.com.zup.edu.grpc.handlers

import io.grpc.*
import javax.inject.Inject
import javax.inject.Singleton
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener

@Singleton
class ExceptionHandlerGrpcInterceptor (@Inject val resolver: ExceptionHandlerResolver): ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>,
    ): ServerCall.Listener<ReqT> {

        fun handlerException(call: ServerCall<ReqT, RespT>, e:Exception){
            val handler = resolver.resolve(e)
            val status = handler.handle(e)
            call.close(status.status,status.metadata)
        }

        val listener: ServerCall.Listener<ReqT> = try {
            next.startCall(call, headers)
        } catch (ex: Exception) {
                handlerException(call, ex)
            throw ex
        }
        return object : SimpleForwardingServerCallListener<ReqT>(listener) {
            // No point in overriding onCancel and onComplete; it's already too late
            override fun onHalfClose() {
                try {
                    super.onHalfClose()
                } catch (ex: Exception) {
                    handlerException(call, ex)
                    throw ex
                }
            }

            override fun onReady() {
                try {
                    super.onReady()
                } catch (ex: Exception) {
                    handlerException(call, ex)
                    throw ex
                }
            }
        }
    }

}