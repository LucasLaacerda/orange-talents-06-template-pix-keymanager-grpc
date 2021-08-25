package br.com.zup.edu.pix.account


import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

//@Embeddable
data class Account(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipo: AccountType,

    @Column(nullable = false)
    val clientId: String,

    @Column(nullable = false)
    val agencia: String,

    @Column(nullable = false)
    val numero: String,

    @Column(nullable = false)
    val instituicao: String,

    @Column(nullable = false)
    val titular: String,
){

}
