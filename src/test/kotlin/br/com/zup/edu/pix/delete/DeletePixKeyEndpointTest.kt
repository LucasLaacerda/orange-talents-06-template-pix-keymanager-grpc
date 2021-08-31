package br.com.zup.edu.pix.delete

import br.com.zup.edu.DeletePixKeyRequest
import br.com.zup.edu.KeyManagerDeleteServiceGrpc
import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.pix.KeyType
import br.com.zup.edu.pix.PixKey
import br.com.zup.edu.pix.PixKeyRepository
import br.com.zup.edu.pix.account.AccountType
import br.com.zup.edu.pix.account.LinkedAccount
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class DeletePixKeyEndpointTest (
    val repository: PixKeyRepository,
    val grpcClientDelete: KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceBlockingStub,
    val grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub){

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    @Test
    fun `deve excluir uma chave pix existente`(){

        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1@email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
        )

        assertEquals(repository.count(),1)

        val key = repository.findByKeyValue(response.pixKey.toString())

        grpcClientDelete.deletePixKey(
            DeletePixKeyRequest
                .newBuilder()
                .setPixId(key.id)
                .setClientId(response.clientId)
                .build()
        )

        //verifica exclusao
        assertEquals(repository.count(),0)
    }

    @Test
    fun `nao deve excluir uma chave pix inexistente`(){

        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1@email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
        )

        assertEquals(repository.count(),1)

        val erro = assertThrows<StatusRuntimeException> {
            grpcClientDelete.deletePixKey(
                DeletePixKeyRequest
                    .newBuilder()
                    .setPixId("1234")
                    .setClientId(response.clientId)
                    .build()
            )
        }

        //verifica exclusao
        assertEquals(repository.count(),1)
        with(erro){
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Chave Pix com id:'1234' para o cliente de id: '${response.clientId}' não foi encontrada", status.description)

        }

    }

    @Test
    fun `nao deve excluir uma chave pix, informando cliente inexistente`() {

        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1@email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
        )

        assertEquals(repository.count(), 1)
        val key = repository.findByKeyValue(response.pixKey.toString())

        val erro = assertThrows<StatusRuntimeException> {
            grpcClientDelete.deletePixKey(
                DeletePixKeyRequest
                    .newBuilder()
                    .setPixId(key.id)
                    .setClientId("1234")
                    .build()
            )
        }

        //verifica exclusao
        assertEquals(repository.count(), 1)
        with(erro) {

            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Chave Pix com id:'${key.id}' para o cliente de id: '1234' não foi encontrada", status.description)


        }
    }

    @Test
    fun `nao deve excluir uma chave pix, informando cliente existente entretanto o mesmo não é o responsavel pelo pix`() {

        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1@email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
        )

        assertEquals(repository.count(), 1)
        val key = repository.findByKeyValue(response.pixKey.toString())

        val erro = assertThrows<StatusRuntimeException> {
            grpcClientDelete.deletePixKey(
                DeletePixKeyRequest
                    .newBuilder()
                    .setPixId(key.id)
                    .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
                    .build()
            )
        }

        //verifica exclusao
        assertEquals(repository.count(), 1)
        with(erro) {

            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Chave Pix com id:'${key.id}' para o cliente de id: '5260263c-a3c1-4727-ae32-3bdb2538841b' não foi encontrada", status.description)


        }
    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceBlockingStub? {
            return KeyManagerDeleteServiceGrpc.newBlockingStub(channel)
        }
    }
}