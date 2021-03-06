package com.example.grpcserviceclientdemo.app.grpc_clients

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class WorkflowWithStarter {
    @GrpcClient("my-client")
    private lateinit var workflowServiceBlockingStub: WorkflowServiceGrpc.WorkflowServiceBlockingStub

    fun all(): List<WorkflowDTO> {
        return try {
            val request = WorkflowOuterClass.WorkflowsRequest.getDefaultInstance()
            return workflowServiceBlockingStub
                    .getWorkflows(request)
                    .workflowsList
                    .map { WorkflowDTO(it.workflowId, it.folder, it.name) }
        } catch (e: Exception) {
            throw e
        }
    }
}
