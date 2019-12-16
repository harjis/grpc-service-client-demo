package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.models.Workflow
import com.example.grpcserviceclientdemo.app.models.WorkflowDTO
import com.example.grpcserviceclientdemo.app.models.WorkflowWithStarter
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workflows")
class WorkflowController(
        private val workflow: Workflow,
        private val workflowWithStarter: WorkflowWithStarter
) {
    @GetMapping
    fun index(): List<WorkflowDTO> = workflow.all()

    @GetMapping("/starter")
    fun indexWithStarter(): List<WorkflowDTO> = workflowWithStarter.all()

    @GetMapping("/async")
    fun async(): List<WorkflowDTO> = workflow.allAsync()

    @GetMapping("/suspend")
    fun suspended(): List<WorkflowDTO> = runBlocking {
        workflow.allSuspend()
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long) = workflow.findById(id)
}
