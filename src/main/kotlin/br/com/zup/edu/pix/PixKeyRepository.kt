package br.com.zup.edu.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import javax.xml.crypto.dsig.keyinfo.KeyValue

@Repository
interface PixKeyRepository: JpaRepository<PixKey,Long> {

    fun existsByKeyValue(KeyValue: String): Boolean
    fun existsByClientId(clientId: String?): Boolean

}