package com.zup.servicosExternos.sistemaItau

import com.zup.registraChave.TipoConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class InfoClienteResponseItauClient(

    @field: NotBlank val tipo: TipoConta,
    @field: NotNull val instituicao: InstituicaoResponseItauClient,
    @field: NotBlank val agencia: String,
    @field: NotBlank val numero: String,
    @field: NotNull val titular: TitularResponse


) {
}