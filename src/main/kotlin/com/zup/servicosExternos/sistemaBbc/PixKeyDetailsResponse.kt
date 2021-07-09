package com.zup.servicosExternos.sistemaBbc

import com.google.protobuf.Timestamp
import com.zup.Conta
import com.zup.PixConsultaResponse
import com.zup.Titular
import com.zup.consulta.BuscaInstituicoes
import com.zup.registraChave.ChavePix
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.validation.constraints.NotBlank


data class PixKeyDetailsResponse(
    @field: NotBlank
    val keyType: KeyType,
    @field: NotBlank
    val key: String,
    @field: NotBlank
    val bankAccount: BankAccountBbcResponse,
    @field: NotBlank
    val owner: OwnerResponse,
    @field: NotBlank
    val createdAt: LocalDateTime
) {

    fun toPixResponse(chavePix: ChavePix): PixConsultaResponse {
        val instituicao = BuscaInstituicoes()
        val instant: Instant = createdAt.toInstant(ZoneOffset.UTC)
        val result: Timestamp = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()



        return PixConsultaResponse.newBuilder()
            .setTitular(
                Titular.newBuilder()
                    .setCpf(owner.taxIdNumber).setNome(owner.name).build()
            )
            .setValorChave(key)
            .setTipoChave(keyType.retornaTipoContaPixProto())
            .setClientId(chavePix.clientId)
            .setPixId(chavePix.clientId)
            .setConta(
                Conta.newBuilder()
                    .setNomeInstituicao(instituicao.instituicoesPorIspb.get(bankAccount.participant))
                    .setTipoConta(bankAccount.accountType.retornaContaProto())
                    .setAgencia(bankAccount.branch)
                    .setNumero(bankAccount.accountNumber).build()
            ).setCriadoEm(result).build()

    }

    fun toPixResponse(): PixConsultaResponse {
        val instituicao = BuscaInstituicoes()
        val instant: Instant = createdAt.toInstant(ZoneOffset.UTC)
        val result: Timestamp = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()



        return PixConsultaResponse.newBuilder()
            .setTitular(
                Titular.newBuilder()
                    .setCpf(owner.taxIdNumber).setNome(owner.name).build()
            )
            .setValorChave(key)
            .setTipoChave(keyType.retornaTipoContaPixProto())
            .setConta(
                Conta.newBuilder()
                    .setNomeInstituicao(instituicao.instituicoesPorIspb.get(bankAccount.participant))
                    .setTipoConta(bankAccount.accountType.retornaContaProto())
                    .setAgencia(bankAccount.branch)
                    .setNumero(bankAccount.accountNumber).build()
            ).setCriadoEm(result).build()

    }
}
