package com.zup.servicosExternos.sistemaBbc

import java.time.LocalDateTime

data class DeletePixKeyResponse(val key: String, val participant: String, val deletedAt: LocalDateTime)