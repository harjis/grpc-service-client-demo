package com.example.grpcserviceclientdemo.app.models

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

abstract class BaseModel(private val testChannel: ManagedChannel? = null) {
    protected fun <T> execute(statement: (channel: ManagedChannel) -> T): T {
        val channel = this.testChannel ?: ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()

        return try {
            statement(channel)
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            println("SHUTDOWN")
            channel.shutdown()
        }
    }
}
