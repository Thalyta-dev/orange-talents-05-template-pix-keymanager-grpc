package com.zup.servicosExternos.sistemaBbc

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OwnerResponse(

    @field: NotNull val type: TypePerson,
    @field: NotBlank val name: String,
    @field: NotBlank val taxIdNumber: String,


    ) {}

