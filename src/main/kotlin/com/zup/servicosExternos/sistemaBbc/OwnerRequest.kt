package com.zup.servicosExternos.sistemaBbc

import com.zup.servicosExternos.sistemaItau.InfoClienteResponseItauClient
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OwnerRequest(

        val dadosClient: InfoClienteResponseItauClient


) {
    @field: NotNull val type: TypePerson = TypePerson.NATURAL_PERSON
    @field: NotBlank val name: String = dadosClient.titular.nome
    @field: NotBlank val taxIdNumber: String = dadosClient.titular.cpf

}
