package com.zup.deleta

import com.zup.Exception.ClienteNaoPertenceClienteException
import com.zup.Exception.ClienteNaoTemPermissaoException
import com.zup.Exception.ValorNaoEncontradoException
import com.zup.registraChave.PixRepository
import com.zup.servicosExternos.sistemaBbc.BbcClient
import com.zup.servicosExternos.sistemaBbc.DeletePixKeyRequest
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid


@Singleton
@Validated
class PixDeletaService(
    val repository: PixRepository,
    val sistemaBbc: BbcClient
) {

    fun deletaChave(@Valid requestValida: PixDeletaValida) {

        if (!repository.existsByClientId(requestValida.clientId)) throw ValorNaoEncontradoException("O cliente nao foi encontrado")

        if (repository.findById(UUID.fromString(requestValida.pixId)).isEmpty) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada")

        val chaveEncontrada = repository.clientDonoDaChave(

            UUID.fromString(requestValida.pixId),
            requestValida.clientId

        ).run { this ?: throw ClienteNaoPertenceClienteException("A chave nao pertence ao cliente") }


            sistemaBbc.deletarChavePix(DeletePixKeyRequest(chaveEncontrada), chaveEncontrada.valorChave).run {
                if(this.status.code == 404) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada no Bbc")
                if(this.status.code == 403) throw ClienteNaoTemPermissaoException("Acesso no bbc foi negado")
            }
        
        repository.deleteById(chaveEncontrada.pixId!!)


    }
}