package com.zup.registraChave

import com.zup.Exception.ChavePixExistenteException
import com.zup.Exception.ClienteNaoEncontradoException
import com.zup.servicosExternos.sistemaItau.SistemaItau
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Singleton
@Validated
open class PixRegistraService {

    @Inject
    lateinit var sistemaItau: SistemaItau

    @Inject
    lateinit var repository: PixRepository


    @Transactional
    fun cadastraChave(@Valid requestValidada: PixRequestValida): ChavePix? {


        if (repository.existsByValorChave(requestValidada.valorChave!!)) throw ChavePixExistenteException("Chave Pix '${requestValidada.valorChave}' existente")

        val dadosClient =sistemaItau.retornaDadosCliente(requestValidada.clientId.toString(), requestValidada.tipoConta.toString()).run {
                    this ?: throw  ClienteNaoEncontradoException("Cliente não encontrado")
                }


        requestValidada.toModel(dadosClient = dadosClient, repository).run {
            repository.save(this).let { chavePix ->
                return chavePix
            }
        }

    }


}


