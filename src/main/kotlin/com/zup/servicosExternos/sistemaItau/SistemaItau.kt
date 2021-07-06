package com.zup.servicosExternos.sistemaItau

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1/clientes")
interface SistemaItau {

    @Get("/{clienteId}/contas")
    fun retornaDadosCliente(@PathVariable clienteId: String,
                            @QueryValue tipo: String): InfoClienteResponse?


}