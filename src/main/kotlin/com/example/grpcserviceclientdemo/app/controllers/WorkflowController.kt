package com.example.grpcserviceclientdemo.app.controllers

import com.example.grpcserviceclientdemo.app.grpc_clients.Workflow
import com.example.grpcserviceclientdemo.app.grpc_clients.WorkflowDTO
import com.example.grpcserviceclientdemo.app.grpc_clients.WorkflowWithStarter
import com.example.grpcserviceclientdemo.app.migrators.WorkflowMigrator
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workflows")
class WorkflowController(
        private val workflow: Workflow,
        private val workflowWithStarter: WorkflowWithStarter,
        private val workflowMigrator: WorkflowMigrator
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

    @GetMapping("/migrator")
    fun migrator() = workflowMigrator.migrate()
}
