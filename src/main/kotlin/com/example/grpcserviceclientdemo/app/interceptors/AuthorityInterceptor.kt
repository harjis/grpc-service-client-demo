package com.example.grpcserviceclientdemo.app.interceptors

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GrpcGlobalClientInterceptor
class AuthorityInterceptor : ClientInterceptor {
    @Autowired
    private lateinit var grpcCustomProperties: GrpcCustomProperties

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
            method: MethodDescriptor<ReqT, RespT>,
            callOptions: CallOptions,
            next: Channel
    ): ClientCall<ReqT, RespT> {
        println("In AuthorityInterceptor: ${grpcCustomProperties.authority}")
        return AuthorityInjector(grpcCustomProperties.authority, next.newCall(method, callOptions))
    }
}

class AuthorityInjector<ReqT, RespT>(authority: String, clientCall: ClientCall<ReqT, RespT>) : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
    private val _authority = authority
    private val authorityKey: Metadata.Key<String> = Metadata.Key.of("authority", Metadata.ASCII_STRING_MARSHALLER)
    override fun start(responseListener: Listener<RespT>, headers: Metadata) {
        println("Start injector with: ${_authority}")
        headers.put(authorityKey, _authority)
        super.start(
                object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onHeaders(headers: Metadata) {
                        println("Headers received from server: $headers")
                        super.onHeaders(headers)
                    }
                }, headers)
    }
}
