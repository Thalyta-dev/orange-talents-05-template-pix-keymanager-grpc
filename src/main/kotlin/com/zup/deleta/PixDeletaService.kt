package com.zup.deleta

import com.zup.Exception.ChaveNaoPertenceClienteException
import com.zup.Exception.ClienteNaoEncontradoException
import com.zup.Exception.ValorNaoEncontradoException
import com.zup.registraChave.PixRepository
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid


@Singleton
@Validated
class PixDeletaService(
    val repository: PixRepository
) {

    fun deletaChave(@Valid requestValida: PixDeletaValida) {

        if (!repository.existsByClientId(requestValida.clientId)) throw ValorNaoEncontradoException("O cliente nao foi encontrado")

        if (repository.findById(UUID.fromString(requestValida.pixId)).isEmpty) throw ValorNaoEncontradoException("A chave requerida nao foi encontrada")

        val chaveEncontrada = repository.clientDonoDaChave(

            UUID.fromString(requestValida.pixId),
            requestValida.clientId

        ).run { this ?: throw ChaveNaoPertenceClienteException("A chave nao pertence ao cliente") }


        repository.delete(chaveEncontrada)


    }
}