package com.zup

import com.zup.registraChave.ChavePix
import com.zup.registraChave.Conta
import com.zup.registraChave.PixRepository
import com.zup.servicosExternos.sistemaItau.Instituicao
import com.zup.servicosExternos.sistemaItau.Titular
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Singleton

@MicronautTest(
    transactional = false,
    rollback = false,
    transactionMode = TransactionMode.SINGLE_TRANSACTION
)
class PixTest(
    val grpcClient: PixRegistraServiceGrpc.PixRegistraServiceBlockingStub,
    val repository: PixRepository,
) {

    @AfterEach
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    fun naoDeveGerarChavePixPoisIdClientInvalido() {

        repository.deleteAll()

        val request = criaRequestIdInvalido()


        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code,
                response.status.code)

            assertEquals("Dados inválidos", response.status.description)
        }

    }
    @Test
    fun naoDeveGerarChavePixPoisDadosInvalidos() {


        val request = criaRequestInvalida()


        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code,
                response.status.code)

            assertEquals("Dados inválidos", response.status.description)
        }

    }
    @Test
    fun naoDeveGerarChavePoisTipoChaveCpfMasValorTelefone() {


        val request = criaRequestValorChaveInvalido()

        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code,
                response.status.code)

            assertEquals("Dados inválidos", response.status.description)
        }

    }

    private fun criaRequestValorChaveInvalido(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CPF)
            .setValorChave("34850850828")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()

    }

    @Test
    fun deveGerarChavePixPoisTipoDeContaDiferente() {

        repository.save(criaChavePix())

        var request = criaRequestContaPoupanca()

        val response = grpcClient.cadastraChave(request)

        with(response) {
            assertNotNull(response.clientId)
            assertNotNull(response.pixId)
            assertEquals(response.clientId, request?.idCliente)
        }

    }

    @Test
    fun deveCadastrarChavePix() {

        val request = criaRequest()

        val response = grpcClient.cadastraChave(request)

        with(response) {
            assertNotNull(response.clientId)
            assertNotNull(response.pixId)
            assertEquals(response.clientId, request?.idCliente)
        }

    }

    @Test
    fun naoDeveGerarChavePixPoisJaExiste() {

        repository.save(criaChavePix())

        val request = criaRequest()
        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
            assertEquals("Chave Pix '${request?.valorChave}' existente", response.status.description)
        }

    }

    @Test
    fun naoDeveGerarChavePixPoisClienteNaoExiste() {
        
        val request = criaRequestClientInexistente()

        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("Cliente não encontrado", response.status.description)
        }

    }

    fun criaChavePix(): ChavePix{

        return ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = "CONTA_CORRENTE",
                instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                agencia = "0001",
                numero = "123455",
                titular = Titular("Rafael M C Ponte", "02467781054")
            ),
            tipoChave = com.zup.registraChave.TipoChave.TELEFONE,
            valorChave = "34850850828",
            tipoConta = com.zup.registraChave.TipoConta.CONTA_CORRENTE

        )
    }

    fun criaRequest(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setTipoChave(TipoChave.TELEFONE)
            .setValorChave("34850850828")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()

    }

    fun criaRequestInvalida(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.EMAIL)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .setValorChave("").build()

    }


    fun criaRequestContaPoupanca(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setTipoChave(TipoChave.CPF)
            .setValorChave("124-853-036.57")
            .setTipoConta(TipoConta.CONTA_POUPANCA).build()

    }

    fun criaRequestIdInvalido(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb")
            .setTipoChave(TipoChave.TELEFONE)
            .setValorChave("34850850828")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()

    }


    fun criaRequestClientInexistente(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157892")
            .setTipoChave(TipoChave.TELEFONE)
            .setValorChave("34850850828")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()

    }


    @Factory
    class Clients {
        @Singleton
        fun blockStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixRegistraServiceGrpc.PixRegistraServiceBlockingStub {
            return PixRegistraServiceGrpc.newBlockingStub((channel))

        }
    }





}
