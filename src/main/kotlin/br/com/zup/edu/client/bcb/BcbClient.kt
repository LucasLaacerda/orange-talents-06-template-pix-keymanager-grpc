package br.com.zup.edu.client.bcb

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${client.bcb.url}")
interface BcbClient {

    @Get("keys/{key}")
    fun findPixKey(@PathVariable key: String): HttpResponse<PixKeyDetailsResponse>

    @Post("keys")
    fun registerPixKey(@Body key: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("keys/{key}")
    fun deletePixKey(@PathVariable key: String): HttpResponse<Any>

}