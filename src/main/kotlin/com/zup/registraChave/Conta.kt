package com.zup.registraChave

import com.zup.servicosExternos.sistemaItau.Instituicao
import com.zup.servicosExternos.sistemaItau.Titular
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(

    @Enumerated(EnumType.STRING)
    @field: NotBlank val tipo: TipoConta,

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
