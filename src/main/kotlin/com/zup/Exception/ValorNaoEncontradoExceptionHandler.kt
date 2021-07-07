package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ValorNaoEncontradoExceptionHandler: ExceptionHandler<ValorNaoEncontradoException> {
    override fun handle(e: ValorNaoEncontradoException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ValorNaoEncontradoException
    }

}