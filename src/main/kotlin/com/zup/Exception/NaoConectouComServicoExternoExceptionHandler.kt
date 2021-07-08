package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class NaoConectouComServicoExternoExceptionHandler: ExceptionHandler<NaoConectouComServicoExternoException> {
    override fun handle(e: NaoConectouComServicoExternoException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.UNAVAILABLE
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is NaoConectouComServicoExternoException
    }

}