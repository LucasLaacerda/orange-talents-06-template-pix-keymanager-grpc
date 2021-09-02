package br.com.zup.edu.client.bcb

import br.com.zup.edu.pix.KeyType
import java.time.LocalDateTime

data class CreatePixKeyResponse (
    val keyType: KeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
    ){

    data class BankAccount(val participant: String,
                           val branch:String,
                           val accountNumber: String,
                           val accountType: KeyType
                            ) {
    }

    data class Owner(val type: String,
                     val name:String,
                     val taxIdNumber:String) {
    }

}