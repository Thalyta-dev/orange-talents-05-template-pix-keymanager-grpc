package com.zup.testes
import com.zup.PixConsultaPorChaveRequest
import com.zup.PixConsultaRequest
import com.zup.PixConsultaServiceGrpc
import com.zup.registraChave.*
import com.zup.servicosExternos.sistemaBbc.*
import com.zup.servicosExternos.sistemaItau.Instituicao
import com.zup.servicosExternos.sistemaItau.Titular
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime


@MicronautTest(
    transactional = false,
    rollback = false,
    transactionMode = TransactionMode.SINGLE_TRANSACTION
)
class EndpointConsultaTest(
    val grpcClient: PixConsultaServiceGrpc.PixConsultaServiceBlockingStub,
    val repository: PixRepository,
    val sistemaBbcClient: BbcClient
) {

    lateinit var chavePix: ChavePix

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        chavePix = repository.save(criaChavePix())
    }

    @Test
    fun deveRetornarDetalhesPixPorId(){


        val request = criaRequestConsultaPorId()

        Mockito.`when`(
            sistemaBbcClient.consultaChave(chavePix.valorChave))
            .thenReturn(HttpResponse.ok(PixKeyDetailsResponse(
                KeyType.PHONE,
                "34850850828",
                owner = OwnerResponse(
                    TypePerson.NATURAL_PERSON,
                    "Rafael M C Ponte",
                    "02467781054"
                ),
                bankAccount = BankAccountBbcResponse(
                    "60701190",
                    "123455",
                    "0001",
                    TypeAccount.CACC
                ),
                createdAt = LocalDateTime.now())
            )
            )

        val response = grpcClient.consulta(request)

        with(response){
            assertNotNull(response)
            assertEquals(response.clientId, request?.clientId)
            assertEquals(response.pixId, request?.clientId)

        }


    }

    @Test
    fun deveRetornarDetalhesPixPorValor(){


        val request = criaRequestConsultaPorValor()

        Mockito.`when`(
            sistemaBbcClient.consultaChave(chavePix.valorChave))
            .thenReturn(HttpResponse.ok(PixKeyDetailsResponse(
                KeyType.PHONE,
                "34850850828",
                owner = OwnerResponse(
                    TypePerson.NATURAL_PERSON,
                    "Rafael M C Ponte",
                    "02467781054"
                ),
                bankAccount = BankAccountBbcResponse(
                    "60701190",
                    "123455",
                    "0001",
                    TypeAccount.CACC
                ),
                createdAt = LocalDateTime.now())
            )
            )

        val response = grpcClient.consultaParaSistemasExternos(request)

        with(response){
            assertNotNull(response)
            assertEquals(response.valorChave, request?.chavePix)

        }


    }
    @Test
    fun deveRetornarDetalhesPixPorValorExistenteSomenteNoBBC(){

        Mockito.`when`(
            sistemaBbcClient.consultaChave("tata@hotmail.com"))
            .thenReturn(HttpResponse.ok(PixKeyDetailsResponse(
                KeyType.PHONE,
                "tata@hotmail.com",
                owner = OwnerResponse(
                    TypePerson.NATURAL_PERSON,
                    "Rafael M C Ponte",
                    "02467781054"
                ),
                bankAccount = BankAccountBbcResponse(
                    "60701190",
                    "123455",
                    "0001",
                    TypeAccount.CACC
                ),
                createdAt = LocalDateTime.now())
            )
            )

        val request = PixConsultaPorChaveRequest.newBuilder().setChavePix("tata@hotmail.com").build()

        val response = grpcClient.consultaParaSistemasExternos(request)

        with(response){
            assertNotNull(response)
            assertEquals(response.valorChave, request?.chavePix)

        }


    }

    @Test
    fun naodeveRetornarDetalhesPixPorIdPoisNaoExiste(){


        val request = PixConsultaRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb25388413")
            .setPixId(chavePix.pixId.toString()).build()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(response) {
            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("O cliente nao foi encontrado", response.status.description)
        }


    }

    @Test
    fun naodeveRetornarDetalhesPixPorIdPoisClienteNaoExiste(){


        val request = PixConsultaRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setPixId("5260263c-a3c1-4727-ae32-3bdb2538841b").build()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(response) {
            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("A chave requerida nao foi encontrada", response.status.description)
        }


    }

    @Test
    fun naodeveRetornarDetalhesPixPorIdPoisChaveNaoExisteNoBbC(){


        val request = criaRequestConsultaPorId()


        Mockito.`when`(
            sistemaBbcClient.consultaChave(chavePix.valorChave)).thenReturn(HttpResponse.notFound())


        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(response) {
            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("A chave requerida nao foi encontrada no Bbc", response.status.description)
        }


    }

    @Test
    fun naodeveRetornarDetalhesPixPorIdPoisClienteNaoEDonoDaChave(){

        val chaveCadastradaNoBanco = repository.save(
            ChavePix(
                clientId = "5260263c-a3c1-4727-ae32-3bdb25388412",
                conta = Conta(
                    tipo = TipoConta.CONTA_CORRENTE,
                    instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                    agencia = "0001",
                    numero = "123455",
                    titular = Titular("Rafael M C Ponte", "02467781054")
                ),
                tipoChave = TipoChave.TELEFONE,
                valorChave = "34850850828",
                tipoConta = TipoConta.CONTA_CORRENTE

            )

        )

        val request = PixConsultaRequest.newBuilder()
            .setClientId(chaveCadastradaNoBanco.clientId)
            .setPixId(chavePix.pixId.toString()).build()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, response.status.code)
            assertEquals("A chave nao pertence ao cliente", response.status.description)
        }


    }

    fun criaChavePix(): ChavePix {

        return ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = TipoConta.CONTA_CORRENTE,
                instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                agencia = "0001",
                numero = "123455",
                titular = Titular("Rafael M C Ponte", "02467781054")
            ),
            tipoChave = TipoChave.TELEFONE,
            valorChave = "34850850828",
            tipoConta = TipoConta.CONTA_CORRENTE

        )
    }

    fun criaRequestConsultaPorId(): PixConsultaRequest? {
        return PixConsultaRequest.newBuilder()
            .setClientId( "5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setPixId(chavePix.pixId.toString()).build()

    }

    fun criaRequestConsultaPorValor(): PixConsultaPorChaveRequest? {
        return PixConsultaPorChaveRequest.newBuilder()
            .setChavePix(chavePix.valorChave).build()
    }

    @MockBean(BbcClient::class)
    fun `sistemaBbcMock`(): BbcClient {

        return Mockito.mock(BbcClient::class.java)

    }



}