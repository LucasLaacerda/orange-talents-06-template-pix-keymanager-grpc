package br.com.zup.edu.pix

import br.com.zup.edu.client.itau.ErpItauClient
import br.com.zup.edu.grpc.handlers.PixKeyExistingException
import io.grpc.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

//@Validated
@Singleton
class RegisterPixKeyService(@Inject val clientErpItauClient: ErpItauClient,
                            @Inject val repository: PixKeyRepository) {


    //@Transactional
    fun register(newKey: NewPixKey): PixKey{


    if (repository.existsByKeyValue(newKey.keyValue.toString()) &&
        !newKey.type?.name.equals("ALEATORIA"))
        
        throw PixKeyExistingException("Chave Pix '${newKey.keyValue}' já existente")

    val accountResponse = clientErpItauClient.findClient(
        newKey.clientId.toString(),
        newKey.accountType!!.name
    )

    val account = accountResponse.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado")

    val key = newKey.toModel(account = account)
    repository.save(key)

    return key

    }


}