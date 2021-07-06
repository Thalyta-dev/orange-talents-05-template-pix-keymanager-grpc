package com.zup.servicosExternos.sistemaItau

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class InfoClienteResponse(

    @field: NotBlank val tipo: String,
    @field: NotNull val instituicao: InstituicaoResponse,
    @field: NotBlank val agencia: String,
    @field: NotBlank val numero: String,
    @field: NotNull val titular: TitularResponse


) {
}