package com.zup.registraChave

import com.zup.servicosExternos.sistemaItau.InfoClienteResponse
import com.zup.servicosExternos.sistemaItau.InstituicaoResponse
import com.zup.servicosExternos.sistemaItau.TitularResponse
import io.micronaut.core.annotation.Introspected
import javax.persistence.Embedded
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Introspected
class ContaRequest(

    val dadosClient: InfoClienteResponse

) {

    @field: NotBlank val tipo: String = dadosClient.tipo

    @field: NotNull val instituicao= dadosClient.instituicao

    @field: NotBlank val agencia: String = dadosClient.agencia

    @field: NotBlank val numero: String =dadosClient.numero

    @field: NotNull val titular: TitularResponse = dadosClient.titular

    fun toModel(): Conta {

        return Conta(
            titular = titular.toModel(),
            numero = numero,
            agencia = agencia,
            instituicao = instituicao.toModel(),
            tipo = tipo

        )
    }


}
