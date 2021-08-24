package br.com.zup.edu.pix


import java.time.LocalDateTime
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
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var registerDate: LocalDateTime = LocalDateTime.now()
}