package com.zup.registraChave

import com.zup.PixRegistraRequest

fun PixRegistraRequest.toValida(): PixRequestValida {

    return PixRequestValida(
        clientId = this.idCliente,
        tipoChave = TipoChave.valueOf(this.tipoChave.toString()),
        valorChave = valorChave,
        tipoConta = TipoConta.valueOf(tipoConta.toString())
    )
}