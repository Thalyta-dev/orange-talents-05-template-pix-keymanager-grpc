package com.zup.registraChave

import com.zup.servicosExternos.sistemaItau.Instituicao
import com.zup.servicosExternos.sistemaItau.InstituicaoResponse
import com.zup.servicosExternos.sistemaItau.Titular
import com.zup.servicosExternos.sistemaItau.TitularResponse
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(
    @field: NotBlank val tipo: String,

    @Embedded
    @field: NotNull val instituicao: Instituicao,

    @field: NotBlank val agencia: String,

    @field: NotBlank val numero: String,

    @Embedded
    @field: NotNull val titular: Titular

) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val idConta: Long? = null
}
