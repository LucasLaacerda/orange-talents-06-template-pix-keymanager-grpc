package br.com.zup.edu.pix

import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.pix.account.AccountType.UNKNOWN_ACCOUNT_TYPE
import br.com.zup.edu.pix.KeyType.UNKNOWN_KEY_TYPE
import br.com.zup.edu.pix.account.AccountType
import br.com.zup.edu.pix.register.NewPixKey

fun RegisterPixKeyRequest.toModel(): NewPixKey {
    return NewPixKey(
        clientId = clientId,
        type = if (this.keyType==null)
            UNKNOWN_KEY_TYPE else KeyType.valueOf(this.keyType.name),
        keyValue = keyValue,
        accountType = if (this.accountType==null)
            UNKNOWN_ACCOUNT_TYPE else AccountType.valueOf(this.accountType.name),
    )
}