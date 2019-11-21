package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.grpc.CountryClient
import com.example.grpcserviceclientdemo.app.grpc.CountryDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/countries")
class CountryController(val countryClient: CountryClient) {
    @GetMapping("")
    fun index(): List<CountryDTO> {
        val countries = countryClient.all()
        return countries
    }
}
