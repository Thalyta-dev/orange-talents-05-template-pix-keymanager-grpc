package com.zup.servicosExternos.sistemaItau

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Introspected
@Embeddable
data class TitularResponse(
    @field: NotBlank val id: String,
    @field: NotBlank val nome: String,
    @field: NotBlank val cpf: String
) {

    fun toModel(): Titular {
        return Titular(
            nome = nome,
            cpf = cpf
        )
    }
}
