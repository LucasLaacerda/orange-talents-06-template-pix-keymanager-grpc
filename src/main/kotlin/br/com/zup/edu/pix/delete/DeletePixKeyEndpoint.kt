package br.com.zup.edu.pix.delete


import br.com.zup.edu.*
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

//@ErrorHandler
@Singleton
class DeletePixKeyEndpoint(
   @Inject private var service: DeletePixKeyService
) : KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceImplBase() {

    private val logger = LoggerFactory.getLogger(KeyManagerDeleteServiceGrpc::class.java)

    override fun deletePixKey(
        request: DeletePixKeyRequest,
        responseObserver: StreamObserver<DeletePixKeyResponse>?) {

        logger.info("Deletando chave para o request: $request")
        //validar dados de entrada

            service.delete(request)

            val response = DeletePixKeyResponse
                .newBuilder()
                .setInfo("Chave de id '${request.pixId}' excluida")
                .build()

            logger.info("Chave excluida: $response")

            responseObserver!!.onNext(response)
            responseObserver.onCompleted()

    }




}



