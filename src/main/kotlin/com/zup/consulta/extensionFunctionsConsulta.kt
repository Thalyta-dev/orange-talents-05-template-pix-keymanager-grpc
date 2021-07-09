package com.zup.consulta

import com.google.protobuf.Timestamp
import com.zup.*
import com.zup.registraChave.ChavePix
import java.time.Instant
import java.time.ZoneOffset

fun PixConsultaRequest.toValida(): PixConsultaValida {
    return PixConsultaValida(
        pixId = pixId,
        clientId = clientId
    )
}

fun PixConsultaPorChaveRequest.toValida(): PixConsultaExternaValida {
    return PixConsultaExternaValida(
        chavePix = chavePix
    )
}


fun PixConsultaResponse.detalhesChavePix(chavePix: ChavePix): PixConsultaResponse {
    val instant: Instant = chavePix.criadaEm.toInstant(ZoneOffset.UTC)
    val result: Timestamp = Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
        .build()



    return PixConsultaResponse.newBuilder()
        .setTitular(
            Titular.newBuilder()
                .setCpf(chavePix.conta.titular.cpf).setNome(chavePix.conta.titular.nome).build()
        )
        .setValorChave(chavePix.valorChave)
        .setTipoChave(TipoChave.valueOf(chavePix.tipoChave.toString()))

        .setConta(
            Conta.newBuilder()
                .setNomeInstituicao(chavePix.conta.instituicao.nomeInstituicao)
                .setTipoConta(TipoConta.valueOf(chavePix.tipoConta.toString()))
                .setAgencia(conta.agencia)
                .setNumero(conta.numero).build()
        ).setCriadoEm(result).build()

}
