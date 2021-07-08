package com.zup.registraChave


import br.com.zup.edu.shared.validation.ValidUUID
import com.zup.annotationValidacoes.ValidChavePix
import com.zup.servicosExternos.sistemaBbc.CreatePixKeyResponse
import com.zup.servicosExternos.sistemaItau.InfoClienteResponseItauClient
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

    fun toModel(dadosClient: InfoClienteResponseItauClient, chavePixValor: CreatePixKeyResponse,repository: PixRepository): ChavePix {

        val contaExistente = repository.findByConta(dadosClient.numero, dadosClient.tipo.toString())

        return ChavePix(
            tipoChave = tipoChave,
            valorChave = chavePixValor.key,
            tipoConta = tipoConta,
            conta = contaExistente ?: ContaRequest(dadosClient).toModel(),
            clientId = clientId.toString()

        )
    }
}


