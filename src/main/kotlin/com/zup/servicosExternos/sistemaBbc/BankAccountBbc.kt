package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.TipoConta
import com.zup.servicosExternos.sistemaItau.InfoClienteResponseItauClient
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class BankAccountBbc(
    val dadosClient: InfoClienteResponseItauClient

) {

    @field: NotBlank
    val participant: String = dadosClient.instituicao.ispb

    @field: NotBlank
    val branch: String = dadosClient.agencia

    @field: NotBlank
    val accountNumber: String = dadosClient.numero

    @field: NotNull
    val accountType: TypeAccount = dadosClient.tipo.returnTipoContaBbc()


}
