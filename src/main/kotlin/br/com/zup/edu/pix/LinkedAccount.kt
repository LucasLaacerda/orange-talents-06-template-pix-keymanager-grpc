package br.com.zup.edu.pix

import javax.persistence.Embeddable

@Embeddable
data class LinkedAccount(
    val institution: String,
    val ownerName: String,
    val ownerCpf: String,
    val agencia: String,
    val accountNumber: String
) {

}
