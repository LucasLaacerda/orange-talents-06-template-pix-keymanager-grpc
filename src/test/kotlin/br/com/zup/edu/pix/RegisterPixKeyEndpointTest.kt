package br.com.zup.edu.pix

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.RegisterPixKeyRequest
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Singleton
import org.junit.jupiter.api.*

@MicronautTest(transactional = false)
internal class RegisterPixKeyEndpointTest(val repository: PixKeyRepository,val grpcClient:KeyManagerServiceGrpc.KeyManagerServiceBlockingStub){

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    @Test
    fun `deve registrar uma nova chave pix com email`() {
        //cenario

        //acao
        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1@email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
        )
        //validacao
        with(response) {
            assertNotNull(clientId)
            assertTrue(repository.existsByClientId(clientId))
            assertNotNull(pixKey)
            assertTrue(repository.existsByKeyValue(pixKey))
        }
    }

    @Test
    fun `deve registrar uma nova chave pix com cpf`() {
        //cenario

        //acao
        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("11596872004")
                .setKeyTypeValue(1)
                .setAccountTypeValue(1)
                .build()
        )
        //validacao
        with(response) {
            assertNotNull(clientId)
            assertTrue(repository.existsByClientId(clientId))
            assertNotNull(pixKey)
            assertTrue(repository.existsByKeyValue(pixKey))
        }
    }
    @Test
    fun `deve registrar uma nova chave pix com celular`() {
        //cenario

        //acao
        val response = grpcClient.registerPixKey(
            RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("+5585988714077")
                .setKeyTypeValue(2)
                .setAccountTypeValue(1)
                .build()
        )
        //validacao
        with(response) {
            assertNotNull(clientId)
            assertTrue(repository.existsByClientId(clientId))
            assertNotNull(pixKey)
            assertTrue(repository.existsByKeyValue(pixKey))
        }
    }

    @Test
    fun `nao deve registrar chave, deve retornar erro email invalido`() {
        //cenario

        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                .newBuilder()
                .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyValue("lucas1email.com")
                .setKeyTypeValue(3)
                .setAccountTypeValue(1)
                .build()
            )
        }
            //validacao
            with(erro) {
                assertEquals(Status.INVALID_ARGUMENT.code, status.code)
                assertEquals("email invalido", status.description)
            }
        }

    @Test
    fun `nao deve registrar chave, deve retornar erro cpf invalida`() {
        //cenario

        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("12344321")
                    .setKeyTypeValue(1)
                    .setAccountTypeValue(1)
                    .build()
            )
        }
        //validacao
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("cpf invalido! formato esperado: 12345678901", status.description)
        }
    }

    @Test
    fun `nao deve registrar chave, deve retornar erro celular invalido`() {
        //cenario

        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("1234")
                    .setKeyTypeValue(2)
                    .setAccountTypeValue(1)
                    .build()
            )
        }
        //validacao
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("celular invalido! formato esperado: +5585988714077", status.description)
        }
    }

    @Test
    fun `nao deve registrar chave, deve retornar erro cliente nao encontrado`() {
        //cenario

        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("1234-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("lucas@email.com")
                    .setKeyTypeValue(3)
                    .setAccountTypeValue(1)
                    .build()
            )
        }
        //validacao
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Informações invalidas\n" +
                    "Não encontramos conta bancaria referente aos dados informados", status.description)
        }
    }

    @Test
    fun `nao deve registrar chave, deve retornar erro chave ja registrada`() {
        //cenario
        setUp()
        `deve registrar uma nova chave pix com email`()
        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("lucas1@email.com")
                    .setKeyTypeValue(3)
                    .setAccountTypeValue(1)
                    .build()
            )
        }
        //validacao
        with(erro) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave Pix 'lucas1@email.com' já existente", status.description)
        }
    }

    @Test
    fun `deve registrar uma chave aleatoria`() {
        //cenario

        //acao
        val response = grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("teste@email")
                    .setKeyTypeValue(4)
                    .setAccountTypeValue(1)
                    .build()
            )

        //validacao
        with(response) {
            assertNotNull(clientId)
            assertTrue(repository.existsByClientId(clientId))
            assertNotNull(pixKey)
            assertTrue(repository.existsByKeyValue(pixKey))
        }
    }

    @Test
    fun `nao deve registrar chave maior que 77`() {
        //cenario

        //acao
        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                RegisterPixKeyRequest
                    .newBuilder()
                    .setClientId("1234-7901-44fb-84e2-a2cefb157890")
                    .setKeyValue("lucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.comlucas@email.com")
                    .setKeyTypeValue(3)
                    .setAccountTypeValue(1)
                    .build()
            )
        }
        //validacao
        with(erro) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Chave Pix invalida! Chave deve ter no maximo 77 caracteres", status.description)
        }
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub? {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }

}