package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.models.Country
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/countries")
class CountryController(val country: Country) {
    @GetMapping("")
    fun index() = country.all()
}
