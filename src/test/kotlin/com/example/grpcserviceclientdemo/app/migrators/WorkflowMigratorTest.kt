package com.example.grpcserviceclientdemo.app.migrators


import com.example.grpcserviceclientdemo.app.grpc_clients.WorkflowDTO
import com.example.grpcserviceclientdemo.app.grpc_clients.WorkflowWithStarter
import com.example.grpcserviceclientdemo.app.repositories.WorkflowRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
class WorkflowMigratorTest {
    private lateinit var workflowMigrator: WorkflowMigrator
    private lateinit var workflow: WorkflowWithStarter

    @Autowired
    private lateinit var workflowRepository: WorkflowRepository

    @BeforeEach
    fun setup() {
        workflow = mockk()
        every { workflow.all() } returns listOf(WorkflowDTO(1, 1, "Mocked Folder 1,", "Mocked Workflow 1"))
        workflowMigrator = WorkflowMigrator(workflow, workflowRepository)
    }

    @Test
    fun doesSomething() {
        workflowMigrator.migrate()
        Assertions.assertEquals(1, workflowRepository.all().count())
    }
}
