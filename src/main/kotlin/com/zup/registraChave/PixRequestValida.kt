package com.zup.registraChave


import br.com.zup.edu.shared.validation.ValidUUID
import com.zup.annotationValidacoes.ValidChavePix
import com.zup.servicosExternos.sistemaItau.InfoClienteResponse
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ValidChavePix
@Introspected
class PixRequestValida(


    @field: NotBlank @field: ValidUUID val clientId: String,
    @field: NotBlank val tipoChave: TipoChave,
    @field: Size(max = 77) val valorChave: String?,
    @field: NotBlank val tipoConta: TipoConta,

    ) {

    fun toModel(dadosClient: InfoClienteResponse): ChavePix {

        val conta = ContaRequest(dadosClient)
        return ChavePix(
            tipoChave = tipoChave,
            valorChave = if (this.tipoChave != TipoChave.ALEATORIA) this.valorChave!! else UUID.randomUUID().toString(),
            tipoConta = tipoConta,
            conta = conta.toModel(),
            clientId = clientId.toString()

        )
    }
}


