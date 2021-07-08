package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.ChavePix
import io.micronaut.core.annotation.Introspected

@Introspected
class DeletePixKeyRequest(
    chavePix: ChavePix
) {

    val key = chavePix.valorChave
    val participant = chavePix.conta.instituicao.ispb

}
