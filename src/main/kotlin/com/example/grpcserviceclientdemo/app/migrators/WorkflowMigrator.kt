package com.example.grpcserviceclientdemo.app.migrators

import com.example.grpcserviceclientdemo.app.repositories.WorkflowRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import com.example.grpcserviceclientdemo.app.grpc_clients.Workflow as WorkflowClient

@Service
class WorkflowMigrator(
        private val workflowClient: WorkflowClient,
        private val workflowRepository: WorkflowRepository
) {
    fun migrate() {
        transaction {
            val workflows = workflowClient.all()
            val workflows2 = workflowRepository.all()

            println("Workflows grpc: $workflows")
            println("Workflows db: ${workflows2.map { it.toString() }}")
        }
    }
}