package com.example.grpcserviceclientdemo.app.grpc_clients

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.StreamObserver
import io.grpc.testing.GrpcCleanupRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.Mockito

class WorkflowTest {
    @get:Rule
    val grpcCleanupRule = GrpcCleanupRule()

    private val serviceImpl = Mockito.mock(
            WorkflowServiceGrpc.WorkflowServiceImplBase::class.java,
            AdditionalAnswers.delegatesTo<Any>(
                    object : WorkflowServiceGrpc.WorkflowServiceImplBase() {
                        override fun getWorkflows(request: WorkflowOuterClass.WorkflowsRequest?, responseObserver: StreamObserver<WorkflowOuterClass.WorkflowsResponse>) {
                            val response = WorkflowOuterClass.WorkflowsResponse
                                    .newBuilder()
                                    .addAllWorkflows(
                                            mutableListOf(
                                                    WorkflowOuterClass.Workflow
                                                            .newBuilder()
                                                            .setWorkflowId(1)
                                                            .setFolder("Folder 1")
                                                            .setName("Workflow 1")
                                                            .build()
                                            )
                                    )
                                    .build()

                            responseObserver.onNext(response)
                            responseObserver.onCompleted()
                        }
                    }
            )
    )

    private var workflow: Workflow? = null

    @BeforeEach
    fun setup() {
        val serverName = InProcessServerBuilder.generateName()
        grpcCleanupRule.register(
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(serviceImpl)
                        .build()
                        .start()
        )

        val channel = grpcCleanupRule.register(
                InProcessChannelBuilder
                        .forName(serverName)
                        .directExecutor()
                        .build()
        )

        workflow = Workflow(channel)
    }

    @Test
    fun getsWorkflows() {
        val response = workflow!!.all()
        val firstWorkFlow = response.first()
        Assertions.assertEquals(1, response.size)
        Assertions.assertEquals(1, firstWorkFlow.workflowId)
        Assertions.assertEquals("Folder 1", firstWorkFlow.folder)
        Assertions.assertEquals("Workflow 1", firstWorkFlow.name)
    }

    @Test
    fun getsWorkflowsAsync(){
        val response = workflow!!.allAsync()
        val firstWorkFlow = response.first()
        Assertions.assertEquals(1, response.size)
        Assertions.assertEquals(1, firstWorkFlow.workflowId)
        Assertions.assertEquals("Folder 1", firstWorkFlow.folder)
        Assertions.assertEquals("Workflow 1", firstWorkFlow.name)
    }
}
