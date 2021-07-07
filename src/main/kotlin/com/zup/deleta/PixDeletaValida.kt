package com.zup.deleta

import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
@Validated
class PixDeletaValida(
    @field: ValidUUID  @field: NotBlank val pixId: String,
    @field: ValidUUID @field: NotBlank val clientId: String
) {

}