package com.zup.consulta

import com.zup.DetalhesChave
import com.zup.Exception.ClienteNaoPertenceClienteException
import com.zup.Exception.NaoConectouComServicoExternoException
import com.zup.Exception.ValorNaoEncontradoException
import com.zup.PixConsultaResponse
import com.zup.consulta.listarChaves.DetalhesChavePix
import com.zup.consulta.listarChaves.PixConsultaTodasChavesValida
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

        if (repository.findByClientId(requestValida.clientId).isEmpty) throw ValorNaoEncontradoException("O cliente nao foi encontrado")

        if (repository.findById(UUID.fromString(requestValida.pixId)).isEmpty) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada")

        val chaveEncontrada = repository.clientDonoDaChave(

            UUID.fromString(requestValida.pixId),
            requestValida.clientId

        ).run { this ?: throw ClienteNaoPertenceClienteException("A chave nao pertence ao cliente") }


        return sistemaBbc.consultaChave(chaveEncontrada.valorChave).let {
            if (it.status.code == 404) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada no Bbc")
            if (it.status.code != 200) throw  NaoConectouComServicoExternoException("Nao foi possivel fazer conexao com o BBC")

            it.body()?.toPixResponse(chaveEncontrada)

        }



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

    fun consultaTodasChaves(@Valid requestValida: PixConsultaTodasChavesValida): List<DetalhesChave> {

        return repository.chavesClientes(requestValida.clientId)
            .map { chave -> DetalhesChavePix(chave) }
            .map { detalhesChavePix -> detalhesChavePix.toResponse() }

    }


}