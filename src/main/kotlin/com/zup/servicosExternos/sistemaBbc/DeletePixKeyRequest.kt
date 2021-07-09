package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.ChavePix
import io.micronaut.core.annotation.Introspected

@Introspected
data class DeletePixKeyRequest(
    val chavePix: ChavePix
) {

    val key = chavePix.valorChave
    val participant = chavePix.conta.instituicao.ispb

}
