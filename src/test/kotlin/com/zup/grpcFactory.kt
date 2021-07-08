package com.zup
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel

@Factory
class grpcFactory {
    @Bean
    fun registeStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)=PixRegistraServiceGrpc.newBlockingStub(channel)
    @Bean
    fun removeStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)=PixDeletaServiceGrpc.newBlockingStub(channel)


}