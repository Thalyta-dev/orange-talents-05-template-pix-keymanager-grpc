package com.zup.registraChave

import com.zup.Exception.ChavePixExistenteException
import com.zup.Exception.ClienteNaoEncontradoException
import com.zup.Exception.NaoConectouComServicoExternoException
import com.zup.servicosExternos.sistemaBbc.BbcClient
import com.zup.servicosExternos.sistemaBbc.CreatePixKeyRequest
import com.zup.servicosExternos.sistemaItau.SistemaItau
import io.micronaut.http.HttpStatus
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
    lateinit var sistemaBbc: BbcClient

    @Inject
    lateinit var repository: PixRepository


    @Transactional
    fun cadastraChave(@Valid requestValidada: PixRequestValida): ChavePix? {


        val findByValorChave = repository.findByValorChave(requestValidada.valorChave!!)

        if (findByValorChave.isPresent) throw ChavePixExistenteException("Chave Pix '${requestValidada.valorChave}' existente")

        val dadosClient =
            sistemaItau.retornaDadosCliente(requestValidada.clientId.toString(), requestValidada.tipoConta.toString())
                .run {
                    this ?: throw  ClienteNaoEncontradoException("Cliente não encontrado")
                }



        return sistemaBbc.cadastraChavePix(CreatePixKeyRequest(dadosClient, requestValidada)).run {

            if (this.status == HttpStatus.UNPROCESSABLE_ENTITY) throw ChavePixExistenteException("Chave Pix '${requestValidada.valorChave}' existente no BBC")
            if (this.status != HttpStatus.CREATED) throw NaoConectouComServicoExternoException("Não foi possivel conectar com o BBC")
            requestValidada.toModel(dadosClient, this.body()!!, repository).let { chave -> repository.save(chave) }
        }


    }
}


