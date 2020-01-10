package com.example.grpcserviceclientdemo.app.grpc_clients

import com.example.grpcservicedemo.grpc.WorkflowOuterClass
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.StreamObserver
import io.grpc.testing.GrpcCleanupRule
import net.devh.boot.grpc.client.config.GrpcChannelsProperties
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WorkflowWithStarterTest {
    @get:Rule
    val grpcCleanupRule = GrpcCleanupRule()

    @Autowired
    private lateinit var grpcChannelsProperties: GrpcChannelsProperties

    private val serviceImpl = Mockito.mock(
            WorkflowServiceGrpc.WorkflowServiceImplBase::class.java,
            AdditionalAnswers.delegatesTo<Any>(
                    object : WorkflowServiceGrpc.WorkflowServiceImplBase() {
                        override fun getWorkflows(request: WorkflowOuterClass.WorkflowsRequest?, responseObserver: StreamObserver<WorkflowOuterClass.WorkflowsResponse>) {
                            val response = WorkflowOuterClass.WorkflowsResponse
                                    .newBuilder()
                                    .addAllWorkflow(
                                            mutableListOf(
                                                    WorkflowOuterClass.Workflow
                                                            .newBuilder()
                                                            .setId(1)
                                                            .setViewId(1)
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

    @BeforeEach
    fun setup() {
        grpcCleanupRule.register(
                InProcessServerBuilder
                        .forName(grpcChannelsProperties.globalChannel.address.schemeSpecificPart)
                        .directExecutor()
                        .addService(serviceImpl)
                        .build()
                        .start()
        )
    }

    @Autowired
    private lateinit var workflowWithStarter: WorkflowWithStarter

    @Test
    fun doesSomo() {
        val workflows = workflowWithStarter.all()
        Assertions.assertEquals(1, workflows.size)
        val workflow = workflows.first()
        Assertions.assertEquals("Folder 1", workflow.folder)
        Assertions.assertEquals("Workflow 1", workflow.name)
        Assertions.assertEquals(1, workflow.id)
        Assertions.assertEquals(1, workflow.viewId)
    }
}
