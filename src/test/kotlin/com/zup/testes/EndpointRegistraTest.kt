package com.zup.testes

import com.zup.PixRegistraRequest
import com.zup.PixRegistraServiceGrpc
import com.zup.TipoChave
import com.zup.TipoConta
import com.zup.registraChave.ChavePix
import com.zup.registraChave.Conta
import com.zup.registraChave.PixRepository
import com.zup.registraChave.toValida
import com.zup.servicosExternos.sistemaBbc.*
import com.zup.servicosExternos.sistemaItau.*
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest(
    transactional = false,
    rollback = false,
    transactionMode = TransactionMode.SINGLE_TRANSACTION
)
class EndpointRegistraTest(
    val grpcClient: PixRegistraServiceGrpc.PixRegistraServiceBlockingStub,
    val sistemaItau: SistemaItau,
    val sistemaBbcClient: BbcClient
) {

    @Inject
    lateinit var repository: PixRepository


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
            assertEquals(
                Status.INVALID_ARGUMENT.code,
                response.status.code
            )

            assertEquals("Dados inválidos", response.status.description)
        }

    }

    @Test
    fun naoDeveGerarChavePixPoisDadosInvalidos() {


        val request = criaRequestInvalida()


        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(
                Status.INVALID_ARGUMENT.code,
                response.status.code
            )

            assertEquals("Dados inválidos", response.status.description)
        }

    }

    @Test
    fun naoDeveGerarChavePoisTipoChaveCpfMasValorTelefone() {


        val request = criaRequestValorChaveInvalido()

        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(
                Status.INVALID_ARGUMENT.code,
                response.status.code
            )

            assertEquals("Dados inválidos", response.status.description)
        }

    }


    @Test
    fun deveGerarChavePixPoisTipoDeContaDiferente() {

        repository.save(criaChavePix())

        val request = criaRequestContaPoupanca()

        println(request.toString())

        Mockito.`when`(sistemaItau.retornaDadosCliente(request!!.idCliente, request.tipoConta.toString()))
            .thenReturn(criaRetornoItauValidaPoupanca())

        val teste = criaRetornoBbcValido()

        Mockito.`when`(
            sistemaBbcClient.cadastraChavePix(
                CreatePixKeyRequest(
                    criaRetornoItauValida(),
                    request.toValida()
                )
            )
        )
            .thenReturn(teste)


        val response = grpcClient.cadastraChave(request)
        println(response.toString())

        with(response) {
            assertNotNull(response.clientId)
            assertNotNull(response.pixId)
            assertEquals(request.idCliente, response.clientId)
        }


    }

    @Test
    fun deveCadastrarChavePix() {


        val request = criaRequest()


        Mockito.`when`(sistemaItau.retornaDadosCliente(request!!.idCliente, request.tipoConta.toString()))
            .thenReturn(criaRetornoItauValida())


        Mockito.`when`(
            sistemaBbcClient.cadastraChavePix(
                CreatePixKeyRequest(
                    criaRetornoItauValida(),
                    request.toValida()
                )
            )
        )
            .thenReturn(criaRetornoBbcValido())


        val response = grpcClient.cadastraChave(request)

        with(response) {
            assertNotNull(response.clientId)
            assertNotNull(response.pixId)
            assertEquals(response.clientId, request.idCliente)
        }
    }


    @Test
    fun naoDeveGerarChavePixPoisJaExisteNoSistema() {


        repository.save(criaChavePix())

        val request = criaRequest()
        Mockito.`when`(sistemaItau.retornaDadosCliente(request!!.idCliente, request.tipoConta.toString()))
            .thenReturn(criaRetornoItauValida())

        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.FAILED_PRECONDITION.code, response.status.code)
            assertEquals("Chave Pix '${request?.valorChave}' existente", response.status.description)
        }

    }

    @Test
    fun naoDeveGerarChavePixPoisJaExisteNoBbc() {


        repository.save(criaChavePix())

        val request = criaRequest()
        Mockito.`when`(sistemaItau.retornaDadosCliente(request!!.idCliente, request.tipoConta.toString()))
            .thenReturn(criaRetornoItauValida())

        Mockito.`when`(
            sistemaBbcClient.cadastraChavePix(
                CreatePixKeyRequest(
                    criaRetornoItauValida(),
                    request.toValida()
                )
            )
        )
            .thenReturn(HttpResponse.unprocessableEntity<CreatePixKeyResponse>())


        val response = assertThrows<StatusRuntimeException>
        { grpcClient.cadastraChave(request) }

        with(response) {
            assertEquals(Status.FAILED_PRECONDITION.code, response.status.code)
            assertEquals("Chave Pix '${request.valorChave}' existente", response.status.description)
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

    private fun criaRequestValorChaveInvalido(): PixRegistraRequest? {

        return PixRegistraRequest.newBuilder()
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CPF)
            .setValorChave("34850850828")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()

    }

    fun criaChavePix(): ChavePix {

        return ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = com.zup.registraChave.TipoConta.CONTA_CORRENTE,
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

    fun criaRetornoItauValida(): InfoClienteResponseItauClient {

        return InfoClienteResponseItauClient(
            tipo = com.zup.registraChave.TipoConta.CONTA_CORRENTE,
            titular = TitularResponse(
                id = "5260263c-a3c1-4727-ae32-3bdb2538841b",
                nome = "Rafael M C Ponte",
                cpf = "02467781054"
            ),
            instituicao = InstituicaoResponseItauClient("ITAÚ UNIBANCO S.A.", "60701190"),
            agencia = "0001",
            numero = "123455",
        )
    }

    fun criaRetornoBbcValido(): HttpResponse<CreatePixKeyResponse> {

        return HttpResponse.created(
            CreatePixKeyResponse(
                key = "thalytamaely@hotmail.com",
                bankAccount = BankAccountBbcResponse(
                    "60701190",
                    "0001",
                    "123455",
                    TypeAccount.CACC
                ),
                createdAt = LocalDateTime.now(),
                keyType = KeyType.EMAIL,
                owner = OwnerResponse(
                    type = TypePerson.NATURAL_PERSON,
                    "Rafael M C Ponte",
                    "02467781054",
                )
            )
        )

    }

    fun criaRetornoItauValidaPoupanca(): InfoClienteResponseItauClient {

        return InfoClienteResponseItauClient(
            tipo = com.zup.registraChave.TipoConta.CONTA_POUPANCA,
            titular = TitularResponse(
                id = "5260263c-a3c1-4727-ae32-3bdb2538841b",
                nome = "Rafael M C Ponte",
                cpf = "02467781054"
            ),
            instituicao = InstituicaoResponseItauClient("ITAÚ UNIBANCO S.A.", "60701190"),
            agencia = "0001",
            numero = "123455",
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
            .setValorChave("123-853-036.57")
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

    @MockBean(SistemaItau::class)
    fun `sistemaItauMock`(): SistemaItau {

        return Mockito.mock(SistemaItau::class.java)

    }

    @MockBean(BbcClient::class)
    fun `sistemaBbcMock`(): BbcClient {

        return Mockito.mock(BbcClient::class.java)

    }


}
