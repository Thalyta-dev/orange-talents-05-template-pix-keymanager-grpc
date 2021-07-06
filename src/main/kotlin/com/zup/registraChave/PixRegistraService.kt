package com.zup.registraChave

import com.zup.Exception.ChavePixExistenteException
import com.zup.servicosExternos.sistemaItau.SistemaItau
import io.micronaut.http.HttpResponse
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


        if(repository.existsByValorChave(requestValidada.valorChave))

            return throw ChavePixExistenteException("Chave Pix '${requestValidada.valorChave}' existente")

        val dadosClient = sistemaItau.retornaDadosCliente(requestValidada.clientId.toString(),requestValidada.tipoConta.toString())

        dadosClient ?: throw  IllegalArgumentException("Cliente não encontrado")

        val chavePix = requestValidada.toModel(dadosClient = dadosClient)

        repository.save(chavePix).run {
            return this
        }

    }



}


