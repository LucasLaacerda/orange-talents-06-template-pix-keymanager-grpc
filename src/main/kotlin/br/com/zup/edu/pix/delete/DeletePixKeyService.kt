package br.com.zup.edu.pix.delete

import br.com.zup.edu.DeletePixKeyRequest
import br.com.zup.edu.client.itau.ErpItauClient
import br.com.zup.edu.pix.PixKey
import br.com.zup.edu.pix.PixKeyRepository
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//@Validated
@Singleton
class DeletePixKeyService(@Inject val clientErpItauClient: ErpItauClient, @Inject val repository: PixKeyRepository) {


    //@Transactional
    fun delete(request: DeletePixKeyRequest) {

    val keyFound: Optional<PixKey>? = repository.findByIdAndClientId(request.pixId,request.clientId)

    if (!(keyFound?.isPresent!!))
        throw IllegalStateException("Chave Pix com id:'${request.pixId}' para o cliente de id: '${request.clientId}' não foi encontrada")

    val key = keyFound.get()

    val clientBank = clientErpItauClient.findClient(
        request.clientId,
        key.accountType!!.name
    ).body()?.toModel() ?:
    throw IllegalStateException("Cliente não encontrado")

    repository.delete(key)

    }


}