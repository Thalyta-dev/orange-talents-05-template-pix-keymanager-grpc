package com.zup.consulta.listarChaves

import com.google.protobuf.Timestamp
import com.zup.*
import com.zup.registraChave.ChavePix
import java.time.Instant
import java.time.ZoneOffset

data class DetalhesChavePix(
    val chavePix: ChavePix
) {

    val pixId = chavePix.pixId
    val valorChave = chavePix.valorChave
    val idCliente = chavePix.clientId
    val tipoChave = chavePix.tipoChave
    val tipoConta = chavePix.tipoConta
    val criadaEm = chavePix.criadaEm


    fun toResponse(): DetalhesChave {
        val instant: Instant = criadaEm.toInstant(ZoneOffset.UTC)
        val result: Timestamp = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()

        return DetalhesChave
            .newBuilder()
            .setClientId(idCliente)
            .setTipoConta(TipoConta.valueOf(tipoConta.toString()))
            .setPixId(pixId.toString())
            .setValorChave(valorChave)
            .setTipoChave(TipoChave.valueOf(tipoChave.toString()))
            .setCriadoEm(result).build()
    }

}