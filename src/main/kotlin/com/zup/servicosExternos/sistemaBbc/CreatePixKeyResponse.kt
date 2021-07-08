package com.zup.servicosExternos.sistemaBbc

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class CreatePixKeyResponse(
    @field: NotBlank
    val keyType: KeyType,
    @field: NotBlank
    val key: String,
    @field: NotBlank
    val bankAccount: BankAccountBbcResponse,
    @field: NotBlank
    val owner: OwnerResponse,
    @field: NotBlank
    val createdAt: LocalDateTime
) {
}