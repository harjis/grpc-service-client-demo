package com.example.grpcserviceclientdemo.app.models

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
                    .workflowList
                    .map { WorkflowDTO(it.id, it.viewId, it.folder, it.name) }
        } catch (e: Exception) {
            throw e
        }
    }
}
