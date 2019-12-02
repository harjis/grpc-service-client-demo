package com.example.grpcserviceclientdemo.app.models

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service
import kotlinx.coroutines.guava.await
import net.devh.boot.grpc.client.inject.GrpcClient

// testChannel is needed purely for testing purposes at this stage
@Service
class Workflow(testChannel: ManagedChannel? = null) {
    @GrpcClient("default-server")
    private lateinit var stub: WorkflowServiceGrpc.WorkflowServiceBlockingStub

    fun all(): List<WorkflowDTO> {
        val request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build()
        val response = stub.getWorkflows(request)
        return response.workflowList.map { WorkflowDTO(it.id, it.viewId, it.folder, it.name) }
    }
}

data class WorkflowDTO(val id: Long, val viewId: Long, val folder: String, val name: String)
