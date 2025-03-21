package br.com.zup.edu.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*
import javax.xml.crypto.dsig.keyinfo.KeyValue

@Repository
interface PixKeyRepository: JpaRepository<PixKey,String> {

    fun existsByKeyValue(KeyValue: String): Boolean
    fun existsByClientId(clientId: String?): Boolean
    fun findByIdAndClientId(pixId: String?,clientId: String?): Optional<PixKey>
    fun findByKeyValue(pixKeyValue: String): PixKey
}