package br.com.zup.edu.pix


import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.RegisterPixKeyResponse
import br.com.zup.edu.RegisterPixKeyRequest.KeyType.*
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RegisterPixKeyEndpoint(
   @Inject private val service: RegisterPixKeyService
) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(KeyManagerServiceGrpc::class.java)

    override fun registerPixKey(
        request: RegisterPixKeyRequest,
        responseObserver: StreamObserver<RegisterPixKeyResponse>?
    ) {

        logger.info("Criando chave para o request: $request")
        //validar dados de entrada


//        val keyType = request.keyType
//        val keyValue = request.keyValue


        //verificar no client itau
        try {



                request.validated()
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


    fun RegisterPixKeyRequest.validated(){

        if(keyValue.trim().length>77){
            throw  IllegalArgumentException("Chave Pix invalida! Chave deve ter no maximo 77 caracteres")
        }


            when(keyType){
                CPF -> {
                    if (!this.keyValue.matches("^[0-9]{11}\$".toRegex()))
                            throw  IllegalArgumentException("cpf invalido! formato esperado: 12345678901")
                }
                EMAIL -> {
                    if (!keyValue.matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
                            .toRegex())) throw  IllegalArgumentException("email invalido")
                }
                CELULAR -> {
                    if (!keyValue.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex()))
                        throw  IllegalArgumentException("celular invalido! formato esperado: +5585988714077")
            }

        }
    }

}



