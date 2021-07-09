package com.zup.servicosExternos.sistemaBbc

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client


@Client(value = "\${sistemaExternos.bbc}")
interface BbcClient {

    @Post(produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun cadastraChavePix(@Body dadosCliente: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete(value = "/{key}",produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun deletarChavePix(@Body dadosCliente: DeletePixKeyRequest, @PathVariable key: String): HttpResponse<*>

    @Get(value = "/{key}",produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun consultaChave(@PathVariable key: String): HttpResponse<PixKeyDetailsResponse?>
}