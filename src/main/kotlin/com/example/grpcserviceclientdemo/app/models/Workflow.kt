package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

// testChannel is needed purely for testing purposes at this stage
@Service
class Workflow(private val testChannel: ManagedChannel? = null) {
    fun all() = execute { stub ->
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.workflowList.map { WorkflowDTO(it.viewId, it.folder, it.name) }
    }

    fun allAsync() = executeAsync { stub ->
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.get().workflowList.map { WorkflowDTO(it.viewId, it.folder, it.name) }
    }

    private fun <T> execute(statement: (stub: WorkflowServiceGrpc.WorkflowServiceBlockingStub) -> T): T {
        val channel = this.testChannel ?: ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub = WorkflowServiceGrpc.newBlockingStub(channel)
        return try {
            statement(stub)
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            println("SHUTDOWN")
            channel.shutdown()
        }
    }

    private fun <T> executeAsync(statement: (stub: WorkflowServiceGrpc.WorkflowServiceFutureStub) -> T): T {
        val channel = this.testChannel ?: ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build()
        val stub = WorkflowServiceGrpc.newFutureStub(channel)
        return try {
            statement(stub)
        } catch (e: Exception) {
            println("Joq meni pielee")
            throw e
        } finally {
            println("SHUTDOWN")
            channel.shutdown()
        }
    }
}

data class WorkflowDTO(val viewId: Long, val folder: String, val name: String)
