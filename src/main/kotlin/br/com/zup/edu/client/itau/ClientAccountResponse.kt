package br.com.zup.edu.client.itau

import br.com.zup.edu.pix.account.LinkedAccount

data class ClientAccountResponse (
    val tipo: String,
    val instituicao: InstitutionResponse,
    val agencia: String,
    val numero: String,
    val titular: OwnerResponse
    ){

    fun toModel(): LinkedAccount {
        return LinkedAccount(
            institution = this.instituicao.nome,
            ownerName = this.titular.nome,
            ownerCpf = this.titular.cpf,
            agencia = this.agencia,
            accountNumber = this.numero
        )
    }
    data class InstitutionResponse(val nome: String, val ispb:String) {
    }

    data class OwnerResponse(val id: String, val nome:String,val cpf:String) {
    }
}