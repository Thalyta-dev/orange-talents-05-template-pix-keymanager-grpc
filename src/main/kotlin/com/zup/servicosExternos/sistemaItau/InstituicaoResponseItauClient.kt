package com.zup.servicosExternos.sistemaItau

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Introspected
data class InstituicaoResponseItauClient(
    @field: NotBlank val nome: String,
    @field: NotBlank val ispb: String
) {


    fun toModel(): Instituicao {
        return Instituicao(
            nomeInstituicao = nome,
            ispb = ispb
        )
    }
}
