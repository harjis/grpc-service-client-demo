package com.example.grpcserviceclientdemo.app.grpc_clients

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service
import kotlinx.coroutines.guava.await

// testChannel is needed purely for testing purposes at this stage
@Service
class Workflow(testChannel: ManagedChannel? = null): BaseModel(testChannel) {
    fun all() = execute { channel ->
        val stub = WorkflowServiceGrpc.newBlockingStub(channel)
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.workflowsList.map { WorkflowDTO(it.workflowId, it.folder, it.name) }
    }

    fun allAsync() = execute { channel ->
        val stub = WorkflowServiceGrpc.newFutureStub(channel)
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        response.get().workflowsList.map { WorkflowDTO(it.workflowId, it.folder, it.name) }
    }

    suspend fun allSuspend(): List<WorkflowDTO> {
        val channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build()
        val stub = WorkflowServiceGrpc.newFutureStub(channel)
        val request = WorkflowOuterClass.WorkflowsRequest.getDefaultInstance()
        val response = stub.getWorkflows(request).await()
        return response.workflowsList.map { WorkflowDTO(it.workflowId, it.folder, it.name) }
    }

    fun findById(id: Long) = execute { channel ->
        val stub = WorkflowServiceGrpc.newBlockingStub(channel)
        val request = WorkflowOuterClass.WorkflowRequest.newBuilder().setId(id).build()
        val response = stub.getWorkflow(request)
        response.workflow.let { WorkflowDTO(it.workflowId, it.folder, it.name) }
    }
}

data class WorkflowDTO(val workflowId: Long, val folder: String, val name: String)
