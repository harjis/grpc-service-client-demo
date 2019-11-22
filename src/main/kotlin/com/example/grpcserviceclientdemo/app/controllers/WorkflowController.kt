package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.models.Workflow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workflows")
class WorkflowController(private val workflow: Workflow) {
    @GetMapping
    fun index() = workflow.all()

    @GetMapping("/async")
    fun async() = workflow.allAsync()

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long) = workflow.find(id)
}
