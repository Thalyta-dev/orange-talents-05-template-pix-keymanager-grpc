package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChaveNaoPertenceClienteExceptionHandler: ExceptionHandler<ClienteNaoPertenceClienteException> {
    override fun handle(e: ClienteNaoPertenceClienteException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ClienteNaoPertenceClienteException
    }

}