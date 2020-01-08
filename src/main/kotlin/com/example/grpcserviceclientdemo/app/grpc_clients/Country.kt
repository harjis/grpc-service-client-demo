package com.example.grpcserviceclientdemo.app.grpc_clients

import com.example.grpcservicedemo.grpc.CountryOuterClass
import com.example.grpcservicedemo.grpc.CountryServiceGrpc
import io.grpc.ManagedChannel
import org.springframework.stereotype.Service

@Service
class Country(testChannel: ManagedChannel? = null) : BaseModel(testChannel) {
    fun all() = execute { channel ->
        val stub = CountryServiceGrpc.newFutureStub(channel)
        val request = CountryOuterClass.CountryRequest.newBuilder().build()
        val response = stub.getCountries(request)
        response.get().countryList.map { CountryDTO(it.id, it.identifier, it.name) }
    }
}

data class CountryDTO(val id: Long, val identifier: String, val name: String)
