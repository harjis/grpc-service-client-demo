package com.example.grpcserviceclientdemo.app.models;

import com.example.grpcservicedemo.grpc.WorkflowOuterClass;
import com.example.grpcservicedemo.grpc.WorkflowServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Work<T> extends Base {
    public List<WorkflowDTO2> all() {
        return this.execute((ManagedChannel channel) -> {
            WorkflowServiceGrpc.WorkflowServiceBlockingStub stub = WorkflowServiceGrpc.newBlockingStub(channel);
            WorkflowOuterClass.WorkflowsRequest request = WorkflowOuterClass.WorkflowsRequest.newBuilder().build();
            WorkflowOuterClass.WorkflowsResponse response = stub.getWorkflows(request);
            return response.getWorkflowList().stream().map((workflow) -> new WorkflowDTO2(workflow.getId())).collect(Collectors.toList());
        });
    }

    public WorkflowDTO2 find() {
        return this.execute((ManagedChannel channel) -> {
            WorkflowServiceGrpc.WorkflowServiceBlockingStub stub = WorkflowServiceGrpc.newBlockingStub(channel);
            WorkflowOuterClass.WorkflowRequest request = WorkflowOuterClass.WorkflowRequest.newBuilder().build();
            WorkflowOuterClass.WorkflowResponse response = stub.getWorkflow(request);

            return new WorkflowDTO2(response.getWorkflow().getId());
        });
    }
}

class WorkflowDTO2 {
    Long id;

    WorkflowDTO2(Long id) {
        this.id = id;
    }
}

abstract class Base {
    protected <T> T execute(Function<ManagedChannel, T> statement) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        T result;
        try {
            result = statement.apply(channel);
        } catch (Exception e) {
            throw e;
        } finally {
            channel.shutdown();
        }

        return result;
    }
}
