package com.zup.deleta

import br.com.zup.edu.shared.grpc.ErrorHandler
import com.zup.PixDeletaRequest
import com.zup.PixDeletaResponse
import com.zup.PixDeletaServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
@ErrorHandler
class EndPointDelete(
    val service: PixDeletaService
) : PixDeletaServiceGrpc.PixDeletaServiceImplBase() {

    override fun deletaChave(
        request: PixDeletaRequest?,
        responseObserver: StreamObserver<PixDeletaResponse>?
    ) {

        request?.toValida().run {service.deletaChave(this!!) }

        responseObserver?.onNext(PixDeletaResponse.getDefaultInstance())
        responseObserver?.onCompleted()


    }


}