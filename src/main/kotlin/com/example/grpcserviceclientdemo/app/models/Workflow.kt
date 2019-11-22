package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

// testChannel is needed purely for testing purposes at this stage
@Service
class Workflow(private val testChannel: ManagedChannel? = null) {
    fun all() = execute { channel ->
        val stub = WorkflowServiceGrpc.newBlockingStub(channel)
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.workflowList.map { WorkflowDTO(it.id, it.viewId, it.folder, it.name) }
    }

    fun allAsync() = execute { channel ->
        val stub = WorkflowServiceGrpc.newFutureStub(channel)
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.get().workflowList.map { WorkflowDTO(it.id, it.viewId, it.folder, it.name) }
    }

    fun find(id: Long) = execute { channel ->
        val stub = WorkflowServiceGrpc.newBlockingStub(channel)
        val request = WorkflowOuterClass.WorkflowRequest.newBuilder().setId(id).build()
        val response = stub.getWorkflow(request)
        response.workflow.let { WorkflowDTO(it.id, it.viewId, it.folder, it.name) }
    }

    private fun <T> execute(statement: (channel: ManagedChannel) -> T): T {
        val channel = this.testChannel ?: ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()

        return try {
            statement(channel)
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            println("SHUTDOWN")
            channel.shutdown()
        }
    }
}

data class WorkflowDTO(val id: Long, val viewId: Long, val folder: String, val name: String)
