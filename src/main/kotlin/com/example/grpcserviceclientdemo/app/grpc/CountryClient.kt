package com.example.grpcserviceclientdemo.app.grpc

import com.example.grpcservicedemo.grpc.Country
import com.example.grpcservicedemo.grpc.CountryServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class CountryClient {
    fun all(): List<CountryDTO> {
        return CompletableFuture.supplyAsync {
            execute { stub ->
                val request = Country.CountryRequest.newBuilder().build()
                val response = stub.getCountries(request)
                response.get().countryList.map { CountryDTO(it) }
            }
        }.get()
    }

    private fun <T> execute(statement: (stub: CountryServiceGrpc.CountryServiceFutureStub) -> T): T {
        val channel = ManagedChannelBuilder
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

data class CountryDTO(val country: String)
