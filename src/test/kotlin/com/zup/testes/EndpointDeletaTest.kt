package com.zup.testes;


import com.zup.PixDeletaRequest
import com.zup.PixDeletaServiceGrpc
import com.zup.registraChave.*
import com.zup.servicosExternos.sistemaBbc.*
import com.zup.servicosExternos.sistemaItau.*
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*

@MicronautTest(
    transactional = false,
    rollback = false,
    transactionMode = TransactionMode.SINGLE_TRANSACTION
)
class EndpointDeletaTest(
    val grpcClient: PixDeletaServiceGrpc.PixDeletaServiceBlockingStub,
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
    fun deveExcluirChavePix() {

        val request = criaRequest()


        Mockito.`when`(
            sistemaBbcClient.deletarChavePix(
                DeletePixKeyRequest(chavePix.valorChave, chavePix.conta.instituicao.ispb),
                chavePix.valorChave
            )
        )
            .thenReturn(
                HttpResponse.ok(
                    DeletePixKeyResponse(
                        key = "thalyta@hotmail",
                        participant = "607011",
                        deletedAt = LocalDateTime.now()
                    )
                )
            )


        grpcClient.deletaChave(request)

        repository.findById(UUID.fromString(request?.pixId)).let { chavePixEncontrada ->

            Assertions.assertTrue(chavePixEncontrada.isEmpty)

        }

    }
    @Test
    fun naoDeveExcluirChavePoisNaoExisteNoBbc() {


        val request = criaRequest()


            Mockito.`when`(
                sistemaBbcClient.deletarChavePix( DeletePixKeyRequest(chavePix.valorChave, chavePix.conta.instituicao.ispb), chavePix.valorChave))
                .thenReturn(HttpResponse.notFound<DeletePixKeyResponse>())



        val response = assertThrows<StatusRuntimeException>

        { grpcClient.deletaChave(request) }


        with(response){

            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("A chave requerida nao foi encontrada no Bbc", response.status.description)



        }
    }

    @Test
    fun naoDeveExcluirChavePoisNaoTemPermissaoNoBbc() {


        val request = criaRequest()


        Mockito.`when`(
            sistemaBbcClient.deletarChavePix(DeletePixKeyRequest(chavePix.valorChave, chavePix.conta.instituicao.ispb), chavePix.valorChave))
            .thenReturn(HttpResponse.status(HttpStatus.FORBIDDEN))



        assertThrows<StatusRuntimeException> { grpcClient.deletaChave(request) }.let { response ->

            assertEquals(Status.PERMISSION_DENIED.code, response.status.code)
            assertEquals("Acesso no bbc foi negado", response.status.description)


        }
    }

    @Test
    fun naodeveExcluirChavePixPoisClienteNaoExiste() {


        val request = criaRequestClienteInexistente()

        assertThrows<StatusRuntimeException> { grpcClient.deletaChave(request) }.let { response ->

            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("O cliente nao foi encontrado", response.status.description)


        }
    }

    @Test
    fun naodeveExcluirChavePixPoisClienteNaoEDonoDaChave() {


        val request = criaCenarioParaRequestClienteNaoDono()

        assertThrows<StatusRuntimeException> { grpcClient.deletaChave(request) }.let { response ->

            assertEquals(Status.INVALID_ARGUMENT.code, response.status.code)
            assertEquals("A chave nao pertence ao cliente", response.status.description)


        }
    }

    @Test
    fun naodeveExcluirChavePixPoisNaoExiste() {


        val request = criaRequestPixIdInexistente()

        assertThrows<StatusRuntimeException> { grpcClient.deletaChave(request) }.let { response ->

            assertEquals(Status.NOT_FOUND.code, response.status.code)
            assertEquals("A chave requerida nao foi encontrada", response.status.description)


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

    fun criaRequest(): PixDeletaRequest? {

        return PixDeletaRequest.newBuilder()
            .setClientId(chavePix.clientId)
            .setPixId(chavePix.pixId.toString()).build()

    }

    fun criaRequestClienteInexistente(): PixDeletaRequest? {

        return PixDeletaRequest.newBuilder()
            .setClientId("1758f67e-df26-11eb-ba80-0242ac130004")
            .setPixId(chavePix.pixId.toString()).build()

    }

    fun criaRequestPixIdInexistente(): PixDeletaRequest? {

        return PixDeletaRequest.newBuilder()
            .setClientId(chavePix.clientId)
            .setPixId("51224680-df2b-11eb-ba80-0242ac130004").build()

    }

    fun criaCenarioParaRequestClienteNaoDono(): PixDeletaRequest? {

        var chavePixExistenteNoBanco = ChavePix(
            clientId = "1758f67e-df26-11eb-ba80-0242ac130004",
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

        return repository.save(chavePixExistenteNoBanco).run {

            PixDeletaRequest.newBuilder()
                .setClientId("1758f67e-df26-11eb-ba80-0242ac130004")
                .setPixId(chavePix.pixId.toString()).build()

        }


    }

    fun criaRetornoItauValida(): InfoClienteResponseItauClient {

        return InfoClienteResponseItauClient(
            tipo = TipoConta.CONTA_CORRENTE,
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

    @MockBean(BbcClient::class)
    fun `sistemaBbcMock`(): BbcClient {

        return Mockito.mock(BbcClient::class.java)

    }


}
