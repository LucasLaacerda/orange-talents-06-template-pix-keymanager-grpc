package br.com.zup.edu.pix.register


import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.RegisterPixKeyResponse
import br.com.zup.edu.pix.toModel
import br.com.zup.edu.pix.validation.validated
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

//@ErrorHandler
@Singleton
class RegisterPixKeyEndpoint(
   @Inject private var service: RegisterPixKeyService
) : KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceImplBase() {

    private val logger = LoggerFactory.getLogger(KeyManagerRegisterServiceGrpc::class.java)

    override fun registerPixKey(
        request: RegisterPixKeyRequest,
        responseObserver: StreamObserver<RegisterPixKeyResponse>?
    ) {

        logger.info("Criando chave para o request: $request")
        //validar dados de entrada


//        val keyType = request.keyType
//        val keyValue = request.keyValue
//
        try{

                request.validated() //validacao regex
                val newKey = request.toModel()
                val key = service.register(newKey)
                //criar entity chave
                //criar repository chave
                //injetar repository
                //salvar no banco a chave


                val response = RegisterPixKeyResponse
                    .newBuilder()
                    .setPixKey(key.keyValue)
                    .setClientId(key.clientId)
                    .build()

                logger.info("Chave criada: $response")

                responseObserver!!.onNext(response)
                responseObserver.onCompleted()
        } catch (e: HttpClientResponseException) {
            val e = Status.INVALID_ARGUMENT
                .withDescription("Informações invalidas")
                .augmentDescription("Não encontramos conta bancaria referente aos dados informados")
                .asRuntimeException()
            responseObserver?.onError(e)
        }

    }




}



