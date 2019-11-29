package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.AnimalOuterClass
import com.example.grpcservicedemo.grpc.AnimalServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class Animal(testChannel: ManagedChannel? = null) : BaseModel(testChannel) {
    fun all() = execute { channel ->
        val stub: AnimalServiceGrpc.AnimalServiceBlockingStub = AnimalServiceGrpc
                .newBlockingStub(channel)
        val request = AnimalOuterClass.AnimalRequest.newBuilder().build()
        val response = stub.getAnimals(request)
        response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }
    }

    fun findById(id: Int) = execute { channel ->
        val stub: AnimalServiceGrpc.AnimalServiceBlockingStub = AnimalServiceGrpc
                .newBlockingStub(channel)
        val request = AnimalOuterClass.AnimalRequest.newBuilder().setId(id.toString()).build()
        val response = stub.getAnimals(request)
        response.animalList.map { AnimalDTO(it.name, it.color, it.countryList) }.first()
    }
}

data class AnimalDTO(val name: String, val color: String, val country: List<String>)
