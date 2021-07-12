package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.ChavePix
import io.micronaut.core.annotation.Introspected

@Introspected
data class DeletePixKeyRequest(

    val key: String,
    val participant: String

) {



}
