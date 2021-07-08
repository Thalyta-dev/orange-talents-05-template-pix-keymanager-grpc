package com.zup.registraChave

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix(

    @Enumerated(EnumType.STRING)
    @field: NotBlank val tipoChave: TipoChave,

    @field: NotBlank val valorChave: String,

    @Enumerated(EnumType.STRING)
    @field: NotBlank val tipoConta: TipoConta,

    @field: NotBlank val criadaEm: LocalDateTime = LocalDateTime.now(),

    @field: ManyToOne(cascade = [CascadeType.ALL])
    val conta: Conta,

    @field: NotBlank val clientId: String,


    ) {
    @field:Id
    @field:GeneratedValue
    @Column(nullable = false,length = 16)
    var pixId: UUID? = null


}
