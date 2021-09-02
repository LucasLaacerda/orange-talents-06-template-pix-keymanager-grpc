package br.com.zup.edu.client.bcb

import br.com.zup.edu.pix.KeyType
import br.com.zup.edu.pix.PixKey

data class CreatePixKeyRequest (
    val keyType: KeyType? = null,
    val key: String? = null,
    val bankAccount: BankAccount? = null,
    val owner: Owner ? = null,
){

    fun toModel(key: PixKey):CreatePixKeyRequest {
        return CreatePixKeyRequest(
                keyType = KeyType.valueOf(key.type.toString()),
                key = key.keyValue,
                bankAccount = BankAccount(
                    participant = key.account.ownerName,
                    branch = "0001",
                    accountNumber = key.account.accountNumber,
                    accountType = key.accountType.toString()
                ),
                owner = Owner(
                    type = "NATURAL_PERSON",
                    name = key.account.ownerName,
                    taxIdNumber = key.account.ownerCpf
                )
            )
    }

    data class BankAccount(val participant: String,
                           val branch:String,
                           val accountNumber: String,
                           val accountType: String
    ) {
    }

    data class Owner(val type: String,
                     val name:String,
                     val taxIdNumber:String) {
    }

}