package br.com.zup.edu.pix


import br.com.zup.edu.pix.account.AccountType
import br.com.zup.edu.pix.account.LinkedAccount
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Entity
class PixKey(
    @field:NotBlank
    @Column(nullable = false)
    val clientId: String,

    @field:NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: KeyType,

    @field:NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val accountType: AccountType,

    @field:NotBlank
    @Column(nullable = false,unique = true)
    val keyValue: String,

    @field:Valid
    @Embedded
    val account: LinkedAccount,

    ){

    @Id
    var id: String? = UUID.randomUUID().toString()

    @Column(nullable = false)
    var registerDate: LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PixKey


        if (account != other.account) return false

        return true
    }

    override fun hashCode(): Int {
        var result = account.hashCode()
        return result
    }
}