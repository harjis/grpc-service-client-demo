package com.example.grpcserviceclientdemo.app.configuration

import com.example.grpcserviceclientdemo.app.interceptors.AuthorityInterceptor
import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
class GlobalClientInterceptorConfiguration {
    @Bean
    fun globalInterceptorConfigurerAdapter(): (GlobalClientInterceptorRegistry) -> GlobalClientInterceptorRegistry {
        println("Add authority interceptor")
        return { registry: GlobalClientInterceptorRegistry ->
            registry.addClientInterceptors(AuthorityInterceptor())
        }
    }
}
