package com.zup.servicosExternos.sistemaItau

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embeddable

@Embeddable
@Introspected
class Titular(val nome: String, val cpf: String) {

}
