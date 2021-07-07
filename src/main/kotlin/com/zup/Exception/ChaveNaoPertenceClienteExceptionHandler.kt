package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChaveNaoPertenceClienteExceptionHandler: ExceptionHandler<ChaveNaoPertenceClienteException> {
    override fun handle(e: ChaveNaoPertenceClienteException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChaveNaoPertenceClienteException
    }

}