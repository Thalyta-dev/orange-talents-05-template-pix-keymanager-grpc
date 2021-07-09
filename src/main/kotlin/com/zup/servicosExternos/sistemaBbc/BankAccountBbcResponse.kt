package com.zup.servicosExternos.sistemaBbc

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class BankAccountBbcResponse(
    @field: NotBlank
    val participant: String,

    @field: NotBlank
    val branch: String,

    @field: NotBlank
    val accountNumber: String,

    @field: NotNull
    val accountType: TypeAccount
) {


}
