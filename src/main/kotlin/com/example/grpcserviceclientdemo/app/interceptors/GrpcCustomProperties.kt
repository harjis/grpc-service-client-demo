package com.example.grpcserviceclientdemo.app.interceptors

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "custom-grpc-properties")
@Configuration("grpcCustomProperties")
class GrpcCustomProperties {
    lateinit var authority: String
}
