package com.zup.deleta

import com.zup.PixDeletaRequest

fun PixDeletaRequest.toValida(): PixDeletaValida {
    return PixDeletaValida(
        clientId = clientId,
        pixId = pixId
    )
}