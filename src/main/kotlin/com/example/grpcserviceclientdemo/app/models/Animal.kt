package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.AnimalOuterClass
import com.example.grpcservicedemo.grpc.AnimalServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class Animal {
    fun all(): List<AnimalDTO> {
        return execute { stub ->
            val request = AnimalOuterClass.AnimalRequest.newBuilder().build()
            val response = stub.getAnimals(request)
            response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }
        }
    }

    fun show(id: Int): AnimalDTO {
        return execute { stub ->
            val request = AnimalOuterClass.AnimalRequest.newBuilder().setId(id.toString()).build()
            val response = stub.getAnimals(request)
            response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }.first()
        }
    }

    private fun <T> execute(statement: (stub: AnimalServiceGrpc.AnimalServiceBlockingStub) -> T): T {
        val channel: ManagedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub: AnimalServiceGrpc.AnimalServiceBlockingStub = AnimalServiceGrpc
                .newBlockingStub(channel)

        return try {
            statement(stub)
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            println("SHUTDOWN")
            channel.shutdown()
        }
    }
}

data class AnimalDTO(val name: String, val color: String, val country: List<String>)
