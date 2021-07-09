package com.zup.consulta

import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
class PixConsultaExternaValida(
    @field: Size(max = 77)  @field: NotBlank val chavePix: String

){

}