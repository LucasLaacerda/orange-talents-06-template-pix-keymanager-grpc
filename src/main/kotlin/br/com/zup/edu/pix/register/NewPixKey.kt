package br.com.zup.edu.pix.register

import br.com.zup.edu.pix.KeyType
import br.com.zup.edu.pix.PixKey
import br.com.zup.edu.pix.account.AccountType
import br.com.zup.edu.pix.account.LinkedAccount
import br.com.zup.edu.pix.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

//@ValidPixKey
@Introspected
data class NewPixKey(
    @ValidUUID
    @field:NotBlank
    val clientId: String?,

    @field:NotNull
    val type: KeyType?,

    @field:Size(max = 77)
    val keyValue: String?,

    @field:NotNull
    val accountType: AccountType?
) {

    fun toModel(account: LinkedAccount): PixKey {
        return PixKey(
            clientId = UUID.fromString(this.clientId).toString(),
            type = KeyType.valueOf(this.type!!.name),
            keyValue = (if (this.type.name.equals("ALEATORIA"))
                UUID.randomUUID() else this.keyValue).toString(),
            accountType = AccountType.valueOf(this.accountType!!.name),
            account = account
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewPixKey

        if (clientId != other.clientId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientId?.hashCode() ?: 0
        return result
    }

}