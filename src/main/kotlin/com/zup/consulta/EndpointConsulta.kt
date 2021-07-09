package com.zup.consulta

import br.com.zup.edu.shared.grpc.ErrorHandler
import com.zup.PixConsultaPorChaveRequest
import com.zup.PixConsultaRequest
import com.zup.PixConsultaResponse
import com.zup.PixConsultaServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
@ErrorHandler
class EndpointConsulta(
    val service: PixConsultaService

) : PixConsultaServiceGrpc.PixConsultaServiceImplBase() {
    override fun consulta(request: PixConsultaRequest?, responseObserver: StreamObserver<PixConsultaResponse>?) {

        val requestValida = request?.toValida()


        service.consultaChavePorId(requestValida!!).let {
            responseObserver!!.onNext(it)
            responseObserver.onCompleted()

        }





    }

    override fun consultaParaSistemasExternos(
        request: PixConsultaPorChaveRequest?,
        responseObserver: StreamObserver<PixConsultaResponse>?
    ) {

        val requestValida = request?.toValida()


        service.consultaChavePorValor(requestValida!!).let {
            responseObserver!!.onNext(it)
            responseObserver.onCompleted()

        }

    }
}