package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.grpc.AnimalClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/animals")
class AnimalController(val animalClient: AnimalClient) {
    @GetMapping
    fun index() = animalClient.all()

    @GetMapping("/{id}")
    fun show(@PathVariable id: Int) = animalClient.show(id)
}
