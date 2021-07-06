package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ClienteNaoEncontradoExceptionHandler: ExceptionHandler<ClienteNaoEncontradoException> {
    override fun handle(e: ClienteNaoEncontradoException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ClienteNaoEncontradoException
    }

}