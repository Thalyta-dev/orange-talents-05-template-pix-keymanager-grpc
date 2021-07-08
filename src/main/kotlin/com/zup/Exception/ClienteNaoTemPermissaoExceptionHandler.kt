package com.zup.Exception

import com.zup.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ClienteNaoTemPermissaoExceptionHandler: ExceptionHandler<ClienteNaoTemPermissaoException> {
    override fun handle(e: ClienteNaoTemPermissaoException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.PERMISSION_DENIED
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ClienteNaoTemPermissaoException
    }

}