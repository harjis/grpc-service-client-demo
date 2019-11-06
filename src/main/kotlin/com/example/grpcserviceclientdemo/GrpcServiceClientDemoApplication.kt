package com.example.grpcserviceclientdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcServiceClientDemoApplication

fun main(args: Array<String>) {
	runApplication<GrpcServiceClientDemoApplication>(*args)
}
