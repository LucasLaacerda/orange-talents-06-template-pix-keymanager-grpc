package br.com.zup.edu.pix.account

import javax.persistence.Embeddable

@Embeddable
data class LinkedAccount(
    val institution: String,
    val ownerName: String,
    val ownerCpf: String,
    val agencia: String,
    val accountNumber: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LinkedAccount


        if (accountNumber != other.accountNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accountNumber.hashCode()
        return result
    }
}
