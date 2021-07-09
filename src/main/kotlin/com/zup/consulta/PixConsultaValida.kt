package com.zup.consulta

import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected

@Introspected
class PixConsultaValida(
    @field: ValidUUID val pixId: String,
    @field: ValidUUID val clientId: String
){

}