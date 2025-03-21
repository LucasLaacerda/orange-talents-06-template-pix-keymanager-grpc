package br.com.zup.edu.client.itau

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${client.erp.itau.url}")
interface ErpItauClient {

    @Get("clientes/{clienteId}/contas")
    fun findClient(@PathVariable clienteId: String, @QueryValue tipo: String): HttpResponse<ClientAccountResponse>

}