package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.grpc.CountryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/countries")
class CountryController(val countryClient: CountryClient) {
    @GetMapping("/{ids}")
    fun show(@PathVariable ids: List<String>) = countryClient.show(ids)
}
