package br.com.zup.edu.pix.register

import br.com.zup.edu.client.bcb.BcbClient
import br.com.zup.edu.client.bcb.CreatePixKeyRequest
import br.com.zup.edu.client.bcb.CreatePixKeyResponse
import br.com.zup.edu.client.itau.ErpItauClient
import br.com.zup.edu.grpc.handlers.PixKeyExistingException
import br.com.zup.edu.pix.KeyType
import br.com.zup.edu.pix.PixKey
import br.com.zup.edu.pix.PixKeyRepository
import io.micronaut.http.HttpResponse
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

//@Validated
@Singleton
class RegisterPixKeyService(@Inject val clientErpItauClient: ErpItauClient,
                            @Inject val clientBcb: BcbClient,
                            @Inject val repository: PixKeyRepository) {


    //@Transactional
    fun register(newKey: NewPixKey): PixKey {


    if (repository.existsByKeyValue(newKey.keyValue.toString()) &&
        !newKey.type?.name.equals("ALEATORIA"))

        throw PixKeyExistingException("Chave Pix '${newKey.keyValue}' já existente")

    val accountResponse = clientErpItauClient.findClient(
        newKey.clientId.toString(),
        newKey.accountType!!.name
    )

    val account = accountResponse.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado")




    val key = newKey.toModel(account = account)
       val responseBcb: HttpResponse<CreatePixKeyResponse> = clientBcb.registerPixKey(
            CreatePixKeyRequest().toModel(key)
        )
    repository.save(key)

    return key

    }


}