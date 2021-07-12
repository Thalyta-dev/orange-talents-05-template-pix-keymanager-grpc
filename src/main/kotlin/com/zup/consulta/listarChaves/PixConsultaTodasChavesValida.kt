package com.zup.consulta.listarChaves

import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected

@Introspected
class PixConsultaTodasChavesValida(
    @field: ValidUUID val clientId: String
){

}