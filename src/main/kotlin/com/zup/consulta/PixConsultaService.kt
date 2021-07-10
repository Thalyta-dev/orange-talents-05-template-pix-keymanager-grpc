package com.zup.consulta

import com.zup.Exception.ClienteNaoPertenceClienteException
import com.zup.Exception.ValorNaoEncontradoException
import com.zup.PixConsultaResponse
import com.zup.registraChave.PixRepository
import com.zup.servicosExternos.sistemaBbc.BbcClient
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid


@Singleton
@Validated
class PixConsultaService(
    val repository: PixRepository,
    val sistemaBbc: BbcClient
) {

    fun consultaChavePorId(@Valid requestValida: PixConsultaValida): PixConsultaResponse? {

        if (!repository.existsByClientId(requestValida.clientId)) throw ValorNaoEncontradoException("O cliente nao foi encontrado")

        if (repository.findById(UUID.fromString(requestValida.pixId)).isEmpty) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada")

        val chaveEncontrada = repository.clientDonoDaChave(

            UUID.fromString(requestValida.pixId),
            requestValida.clientId

        ).run { this ?: throw ClienteNaoPertenceClienteException("A chave nao pertence ao cliente") }


        return sistemaBbc.consultaChave(chaveEncontrada.valorChave).let {
            if (it.status.code == 404) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada no Bbc")

            it.body()?.toPixResponse(chaveEncontrada)

        }


//            throw  NaoConectouComServicoExternoException("Nao foi possivel fazer conexao com o BBC")


    }

    fun consultaChavePorValor(@Valid valorChavePix: PixConsultaExternaValida): PixConsultaResponse? {

        val chaveEncontrada = repository.findByValorChave(valorChavePix.chavePix)

        if (chaveEncontrada.isPresent) {
            return PixConsultaResponse.getDefaultInstance().detalhesChavePix(chaveEncontrada.get())
        }

        val consultaChave = sistemaBbc.consultaChave(valorChavePix.chavePix)
        if (consultaChave.status.code == 404) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada no Bbc")
        return consultaChave.body()?.toPixResponse()


    }
}