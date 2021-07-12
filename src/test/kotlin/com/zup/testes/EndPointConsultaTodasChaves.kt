package com.zup.testes

import com.zup.PixConsultaChavesRequest
import com.zup.PixConsultaServiceGrpc
import com.zup.PixRegistraServiceGrpc
import com.zup.registraChave.ChavePix
import com.zup.registraChave.Conta
import com.zup.registraChave.PixRepository
import com.zup.registraChave.TipoConta
import com.zup.servicosExternos.sistemaItau.Instituicao
import com.zup.servicosExternos.sistemaItau.Titular
import io.grpc.Grpc
import io.micronaut.data.annotation.Repository
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@MicronautTest(
    transactional = false,
    transactionMode = TransactionMode.SINGLE_TRANSACTION,
    rollback = false
)
class EndPointConsultaTodasChaves(
    val repository: PixRepository,
    val grpcClient: PixConsultaServiceGrpc.PixConsultaServiceBlockingStub,
){
    val clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b"

    lateinit var chavesPix: List<ChavePix>

    @BeforeEach
    fun clenUp(){

        repository.deleteAll()

        chavesPix = repository.saveAll(criaChavesPix())

    }

    @Test
    fun deveRetornarChavesPix(){

        val consultaTodasChavesCliente = grpcClient.consultaTodasChavesCliente(
            PixConsultaChavesRequest.newBuilder()
                .setClientId(clientId).build()
        )

        with(consultaTodasChavesCliente){
            assertTrue(this.chavesPixList.isNotEmpty())
            assertEquals(this.chavesPixList.count(),3)
            assertEquals(this.chavesPixList[0].clientId.toString(), chavesPix[0].clientId)
            assertEquals(this.chavesPixList[1].clientId.toString(), chavesPix[1].clientId)
            assertEquals(this.chavesPixList[2].clientId.toString(), chavesPix[2].clientId)
            assertEquals(this.chavesPixList[0].pixId.toString(), chavesPix[0].pixId.toString())
            assertEquals(this.chavesPixList[1].pixId.toString(), chavesPix[1].pixId.toString())
            assertEquals(this.chavesPixList[2].pixId.toString(), chavesPix[2].pixId.toString())

        }



    }

    @Test
    fun naodeveRetornarChavesPix(){

        val consultaTodasChavesCliente = grpcClient.consultaTodasChavesCliente(
            PixConsultaChavesRequest.newBuilder()
                .setClientId("ef589e18-39c9-4f37-8d22-90fb66650323").build()
        )
        with(consultaTodasChavesCliente){
            assertTrue(this.chavesPixList.isEmpty())
        }

    }

    fun criaChavesPix(): List<ChavePix> {

        val chave1 = ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = TipoConta.CONTA_CORRENTE,
                instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                agencia = "0001",
                numero = "123455",
                titular = Titular("Rafael M C Ponte", "02467781054")
            ),
            tipoChave = com.zup.registraChave.TipoChave.TELEFONE,
            valorChave = "34850850828",
            tipoConta = com.zup.registraChave.TipoConta.CONTA_CORRENTE

        )

        val chave2 =ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = TipoConta.CONTA_CORRENTE,
                instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                agencia = "0001",
                numero = "123455",
                titular = Titular("Rafael M C Ponte", "02467781054")
            ),
            tipoChave = com.zup.registraChave.TipoChave.TELEFONE,
            valorChave = "34850850828",
            tipoConta = com.zup.registraChave.TipoConta.CONTA_CORRENTE

        )

        val chave3 = ChavePix(
            clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            conta = Conta(
                tipo = TipoConta.CONTA_CORRENTE,
                instituicao = Instituicao("ITAÚ UNIBANCO S.A.", "60701190"),
                agencia = "0001",
                numero = "123455",
                titular = Titular("Rafael M C Ponte", "02467781054")
            ),
            tipoChave = com.zup.registraChave.TipoChave.CPF,
            valorChave = "124-853-036.57",
            tipoConta = com.zup.registraChave.TipoConta.CONTA_CORRENTE

        )

        return listOf(chave1,chave2,chave3)

    }


}