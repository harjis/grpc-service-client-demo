package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.CountryOuterClass
import com.example.grpcservicedemo.grpc.CountryServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class Country(private val cchannel: ManagedChannel? = null) {
    fun all(): List<CountryDTO> {
        return execute { stub ->
            val request = CountryOuterClass.CountryRequest.newBuilder().build()
            val response = stub.getCountries(request)
            response.get().countryList.map { CountryDTO(it.id, it.identifier, it.name) }
        }
    }

    private fun <T> execute(statement: (stub: CountryServiceGrpc.CountryServiceFutureStub) -> T): T {
        val channel = this.cchannel ?: ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub = CountryServiceGrpc.newFutureStub(channel)

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

data class CountryDTO(val id: Long, val identifier: String, val name: String)
