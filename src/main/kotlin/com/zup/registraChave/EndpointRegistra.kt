package com.zup.registraChave

import br.com.zup.edu.shared.grpc.ErrorHandler
import com.zup.PixRegistraRequest
import com.zup.PixRegistraResponse
import com.zup.PixRegistraServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ErrorHandler
class EndpointRegistra() : PixRegistraServiceGrpc.PixRegistraServiceImplBase() {


    @Inject
    lateinit var service: PixRegistraService

    override fun cadastraChave(request: PixRegistraRequest?, responseObserver: StreamObserver<PixRegistraResponse>?) {


        request?.toValida().run {  service.cadastraChave(this!!) }.let { cadastraChave ->

            responseObserver!!.onNext(PixRegistraResponse.newBuilder()

                .setClientId(cadastraChave?.clientId.toString())
                .setPixId(cadastraChave?.pixId.toString()).build())

            responseObserver!!.onCompleted()
        }

    }



}