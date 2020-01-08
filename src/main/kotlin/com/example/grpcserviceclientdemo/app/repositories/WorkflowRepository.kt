package com.example.grpcserviceclientdemo.app.repositories

import com.example.grpcserviceclientdemo.app.models.Workflow
import org.springframework.stereotype.Repository

@Repository
class WorkflowRepository {
    fun all() = Workflow.all()
    fun save(block: Workflow.() -> Unit) = Workflow.new(block)
}
