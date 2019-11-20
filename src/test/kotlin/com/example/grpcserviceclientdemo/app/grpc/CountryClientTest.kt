package com.example.grpcserviceclientdemo.app.grpc

import com.example.grpcservicedemo.grpc.CountryOuterClass
import com.example.grpcservicedemo.grpc.CountryServiceGrpc
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.StreamObserver
import io.grpc.testing.GrpcCleanupRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class CountryClientTest {
    @get:Rule
    val grpcCleanupRule = GrpcCleanupRule()

    private val serviceImpl = Mockito.mock(
            CountryServiceGrpc.CountryServiceImplBase::class.java,
            AdditionalAnswers.delegatesTo<Any>(
                    object : CountryServiceGrpc.CountryServiceImplBase() {
                        override fun getCountries(request: CountryOuterClass.CountryRequest?, responseObserver: StreamObserver<CountryOuterClass.CountriesResponse>) {
                            val response = CountryOuterClass.CountriesResponse
                                    .newBuilder()
                                    .addAllCountry(
                                            mutableListOf(
                                                    CountryOuterClass
                                                            .Country
                                                            .newBuilder()
                                                            .setId(1)
                                                            .setIdentifier("A")
                                                            .setName("Austria")
                                                            .build()
                                            )
                                    )
                                    .build()
                            responseObserver.onNext(response)
                            responseObserver.onCompleted()
                        }
                    }
            )
    )

    private var client: CountryClient? = null

    @BeforeEach
    fun setup() {
        val serverName = InProcessServerBuilder.generateName()
        grpcCleanupRule.register(
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(serviceImpl)
                        .build()
                        .start()
        )
        val channel = grpcCleanupRule.register(
                InProcessChannelBuilder
                        .forName(serverName)
                        .directExecutor()
                        .build()
        )

        client = CountryClient(channel)
    }

    @Test
    fun doesSomething() {
        val requestCaptor = ArgumentCaptor.forClass(CountryOuterClass.CountryRequest::class.java)
        client!!.all()
        Mockito
                .verify(serviceImpl)
                .getCountries(
                        requestCaptor.capture(),
                        ArgumentMatchers.any<StreamObserver<CountryOuterClass.CountriesResponse>>()
                )

        println("wat")
        println(requestCaptor.value)
        println("wat2")
//        Assertions.assertEquals("A", requestCaptor.value)
    }
}
