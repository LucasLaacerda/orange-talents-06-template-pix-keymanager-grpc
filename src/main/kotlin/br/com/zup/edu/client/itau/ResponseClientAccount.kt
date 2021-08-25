package br.com.zup.edu.client.itau

import br.com.zup.edu.pix.account.Account
import br.com.zup.edu.pix.account.AccountType

data class ResponseClientAccount(
    val tipo: AccountType,
    val instituicao: Instituicao,
    val agencia: String,
    val numero: String,
    val titular: Titular
){

    fun toModel(): Account {
        return Account(
            this.tipo,
            this.titular.id,
            this.agencia,
            this.numero,
            this.instituicao.nome,
            this.titular.nome
        )
    }

    data class Instituicao(val nome: String, val ispb:String) {
    }

    data class Titular(val id: String, val nome:String,val cpf:String) {
    }
}
