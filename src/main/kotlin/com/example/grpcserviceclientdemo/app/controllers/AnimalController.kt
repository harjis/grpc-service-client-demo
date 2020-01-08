package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.grpc_clients.Animal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/animals")
class AnimalController(val animal: Animal) {
    @GetMapping
    fun index() = animal.all()

    @GetMapping("/{id}")
    fun show(@PathVariable id: Int) = animal.findById(id)
}
