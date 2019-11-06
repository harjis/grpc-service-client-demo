package com.example.grpcserviceclientdemo.app.grpc

import com.example.grpcservicedemo.grpc.AnimalOuterClass
import com.example.grpcservicedemo.grpc.AnimalServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class AnimalClient {
    fun all(): List<AnimalDTO> {
        val channel: ManagedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub: AnimalServiceGrpc.AnimalServiceBlockingStub = AnimalServiceGrpc
                .newBlockingStub(channel)
        return try {
            val request = AnimalOuterClass.AnimalRequest.newBuilder().build()
            val response = stub.getAnimals(request)
            return response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            channel.shutdown()
        }
    }

    fun show(id: Int): AnimalDTO {
        val channel: ManagedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub: AnimalServiceGrpc.AnimalServiceBlockingStub = AnimalServiceGrpc
                .newBlockingStub(channel)
        return try {
            val request = AnimalOuterClass.AnimalRequest.newBuilder().setId(id.toString()).build()
            val response = stub.getAnimals(request)
            return response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }.first()
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            channel.shutdown()
        }
    }
}

data class AnimalDTO(val name: String, val color: String, val country: List<String>)
