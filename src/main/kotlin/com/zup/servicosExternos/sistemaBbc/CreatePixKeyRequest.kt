package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.PixRequestValida
import com.zup.servicosExternos.sistemaItau.InfoClienteResponseItauClient
import io.micronaut.core.annotation.Introspected


@Introspected
data class CreatePixKeyRequest(

    val dadosClient: InfoClienteResponseItauClient,
    val chavePix: PixRequestValida

) {

    val keyType: KeyType = chavePix.tipoChave.retornaChaveBbc()
    val key: String? = chavePix.valorChave
    val bankAccount: BankAccountBbc = BankAccountBbc(dadosClient)
    val owner:  OwnerRequest = OwnerRequest(dadosClient)

}
